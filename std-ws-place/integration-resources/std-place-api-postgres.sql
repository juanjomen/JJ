-- DDL USING SYNTAX FOR POSTGRES - USE DEFAULT TABLESPACE FOR EVERYTHING ...

-- ----------------------------------------------------------------------------
-- Set the schema which is to be used
-- ----------------------------------------------------------------------------
SET SCHEMA 'sams_place';


-- ----------------------------------------------------------------------------
-- TRANSACTION table, and implied sequence
-- ----------------------------------------------------------------------------
CREATE TABLE transaction (
    tran_id        SERIAL        NOT NULL  UNIQUE,
    create_ts      TIMESTAMP     NOT NULL  DEFAULT now(),
    create_id      VARCHAR(30)   NOT NULL  DEFAULT session_user,

    CONSTRAINT pk_transaction PRIMARY KEY (tran_id)
);


-- ----------------------------------------------------------------------------
-- TYPE table, and implied sequence
-- ----------------------------------------------------------------------------
CREATE TABLE type (
    type_id        SERIAL        NOT NULL  UNIQUE,
    code           VARCHAR(30)   NOT NULL,
    type_cat       VARCHAR(10)   NOT NULL,
    pub_flag       BOOLEAN       NOT NULL  DEFAULT TRUE,
    create_ts      TIMESTAMP     NOT NULL  DEFAULT now(),
    create_id      VARCHAR(30)   NOT NULL  DEFAULT session_user,

    CONSTRAINT pk_type PRIMARY KEY (type_id),
    CONSTRAINT ck_type_cat CHECK (type_cat IN ('PLACE', 'NAME', 'ATTRIBUTE', 'CITATION')),
    CONSTRAINT nq_code UNIQUE (code)
);


-- ----------------------------------------------------------------------------
-- TERM table, and implied sequence
-- ----------------------------------------------------------------------------
CREATE TABLE type_term (
    term_id        SERIAL        NOT NULL  UNIQUE,
    type_id        INTEGER       NOT NULL,
    locale         VARCHAR(30)   NOT NULL,
    text           VARCHAR(255)  NOT NULL,
    description    VARCHAR(255),
    update_ts      TIMESTAMP     NOT NULL  DEFAULT now(),
    update_id      VARCHAR(30)   NOT NULL  DEFAULT session_user,

    CONSTRAINT pk_term PRIMARY KEY (term_id),
    CONSTRAINT fk_type_id_to_type FOREIGN KEY(type_id) REFERENCES type(type_id),
    CONSTRAINT nq_type_term UNIQUE (type_id,locale)
);


-- ----------------------------------------------------------------------------
-- PLACE table, sequence, trigger
-- ----------------------------------------------------------------------------
CREATE TABLE place
(
   place_id             INTEGER         NOT NULL,
   tran_id              INTEGER         NOT NULL,
   from_year            INTEGER,
   to_year              INTEGER,
   delete_id            INTEGER,

   CONSTRAINT pk_place PRIMARY KEY (place_id,tran_id),
   CONSTRAINT fk_pl_tranid_to_tran_tranid FOREIGN KEY (tran_id) REFERENCES sams_place.transaction (tran_id)
);

CREATE SEQUENCE seq_place INCREMENT BY 1 START WITH 1 NO MAXVALUE;

CREATE OR REPLACE FUNCTION fn_bi_place() RETURNS TRIGGER AS
$BODY$
BEGIN
  SET SCHEMA 'sams_place';

  -- If this is a new place, then set "delete_id" to NULL and use the latest transaction ID
  IF (NEW.place_id IS NULL) THEN
    NEW.place_id = NEXTVAL('seq_place');
    NEW.delete_id = NULL;
    NEW.tran_id = MAX(tran_id) FROM sams_place.transaction;

  -- An existing place requires additional validation
  ELSE
    -- If no 'tran_id" specified use the latest transaction id
    IF (NEW.tran_id IS NULL) THEN
      NEW.tran_id = MAX(tran_id) FROM sams_place.transaction;
    ELSEIF NOT EXISTS (SELECT 1 FROM sams_place.transaction WHERE sams_place.transaction.tran_id = NEW.tran_id) THEN
      RAISE EXCEPTION 'Invalid tran_id specified.';
    END IF;

    -- 'place_id' must reference an existing place and have the most recent transaction ID
    IF NOT EXISTS (SELECT 1 FROM place WHERE place_id = NEW.place_id) THEN
      RAISE EXCEPTION 'Invalid place_id specified -- no matching row found.';
    ELSEIF EXISTS (SELECT 1 FROM place WHERE place_id = NEW.place_id AND tran_id >= NEW.tran_id) THEN
      RAISE EXCEPTION 'Invalid place_id/tran_id combination specified.';
    END IF;

    -- 'delete_id' must reference an existing place
    IF (NEW.delete_id IS NOT NULL) THEN
      IF NOT EXISTS (SELECT 1 FROM place WHERE place_id = NEW.delete_id) THEN
        RAISE EXCEPTION 'Invalid delete_id specified -- no matching place_id.';
      END IF;
    END IF;
  END IF;

  -- 'from_year' must precede [inclusively] 'to_year'
  IF (NEW.from_year IS NOT NULL  AND  NEW.to_year IS NOT NULL  AND  NEW.from_year > NEW.to_year) THEN
    RAISE EXCEPTION 'from_year can not be after to_year';
  END IF;

  -- Return the NEW-ed row so the caller can get the values set here
  RETURN NEW;
END; 
$BODY$
LANGUAGE plpgsql;

CREATE TRIGGER bi_place BEFORE INSERT ON place FOR EACH ROW EXECUTE PROCEDURE fn_bi_place();


-- ----------------------------------------------------------------------------
-- PLACE_NAME table, sequence, trigger
-- ----------------------------------------------------------------------------
CREATE TABLE place_name
(
   name_id              INTEGER         NOT NULL,
   tran_id              INTEGER         NOT NULL,
   text                 VARCHAR(255)    NOT NULL,
   locale               VARCHAR(30)     NOT NULL,
   type_id              INTEGER         NOT NULL,
   place_id             INTEGER         NOT NULL,
   delete_flag          BOOLEAN         DEFAULT FALSE,

   CONSTRAINT pk_place_name PRIMARY KEY (name_id,tran_id),
   CONSTRAINT fk_pn_tranid_to_tran_tranid FOREIGN KEY (tran_id) REFERENCES sams_place.transaction (tran_id),
   CONSTRAINT fk_pn_typeid_to_type_typeid FOREIGN KEY (type_id) REFERENCES sams_place.type (type_id)
);

CREATE SEQUENCE seq_place_name INCREMENT BY 1 START WITH 1 NO MAXVALUE;

CREATE OR REPLACE FUNCTION fn_bi_place_name() RETURNS TRIGGER AS
$BODY$
BEGIN
  SET SCHEMA 'sams_place';

  -- If this is a new name, then set 'delete_flag' to FALSE and use the latest transaction ID
  IF (NEW.name_id IS NULL) THEN
    NEW.name_id = NEXTVAL('seq_place_name');
    NEW.delete_flag = FALSE;
    NEW.tran_id = MAX(tran_id) FROM sams_place.transaction;

  -- An existing name requires additional validation
  ELSE
    -- If no 'tran_id" specified use the latest transaction id
    IF (NEW.tran_id IS NULL) THEN
      NEW.tran_id = MAX(tran_id) FROM sams_place.transaction;
    ELSEIF NOT EXISTS (SELECT 1 FROM sams_place.transaction WHERE sams_place.transaction.tran_id = NEW.tran_id) THEN
      RAISE EXCEPTION 'Invalid tran_id specified.';
    END IF;

    -- 'name_id' must reference an existing name and have the most recent transaction ID
    IF NOT EXISTS (SELECT 1 FROM place_name WHERE name_id = NEW.name_id) THEN
      RAISE EXCEPTION 'Invalid name_id specified -- no matching row found.';
    ELSEIF EXISTS (SELECT 1 FROM place_name WHERE name_id = NEW.name_id AND tran_id >= NEW.tran_id) THEN
      RAISE EXCEPTION 'Invalid name_id/tran_id combination specified.';
    END IF;
  END IF;

  -- 'place_id' must reference an existing place
  IF (NEW.place_id IS NOT NULL) THEN
    IF NOT EXISTS (SELECT 1 FROM place WHERE place_id = NEW.place_id) THEN
      RAISE EXCEPTION 'Invalid place_id specified.';
    END IF;
  END IF;

  -- Return the NEW-ed row so the caller can get the values set here
  RETURN NEW;
END; 
$BODY$
LANGUAGE plpgsql;

CREATE TRIGGER bi_place_name BEFORE INSERT ON place_name FOR EACH ROW EXECUTE PROCEDURE fn_bi_place_name();

 
-- ----------------------------------------------------------------------------
-- PLACE_REP table, sequence, trigger
-- ----------------------------------------------------------------------------
CREATE TABLE place_rep
(
   rep_id               INTEGER         NOT NULL,
   tran_id              INTEGER         NOT NULL,
   parent_id            INTEGER,
   owner_id             INTEGER         NOT NULL,
   centroid_long        FLOAT(42),
   centroid_lattd       FLOAT(42),
   place_type_id        INTEGER         NOT NULL,
   parent_from_year     INTEGER,
   parent_to_year       INTEGER,
   delete_id            INTEGER,
   pref_locale          VARCHAR(30)     NOT NULL,
   pub_flag             BOOLEAN         NOT NULL DEFAULT FALSE,
   validated_flag       BOOLEAN         NOT NULL DEFAULT FALSE,
   uuid                 VARCHAR(36)     NOT NULL,
   group_id             INTEGER,

   CONSTRAINT pk_place_rep PRIMARY KEY (rep_id,tran_id),
   CONSTRAINT fk_pr_tranid_to_tran_tranid FOREIGN KEY (tran_id) REFERENCES sams_place.transaction (tran_id),
   CONSTRAINT fk_pr_plctp_to_type_typeid FOREIGN KEY (place_type_id) REFERENCES sams_place.type (type_id)
);

CREATE SEQUENCE seq_place_rep INCREMENT BY 1 START WITH 1 NO MAXVALUE;

CREATE OR REPLACE FUNCTION fn_bi_place_rep() RETURNS TRIGGER AS 
$BODY$
BEGIN
  SET SCHEMA 'sams_place';

  -- If this is a new place-rep, then set "delete_id" to NULL and use the latest transaction ID
  IF (NEW.rep_id IS NULL) THEN
    NEW.rep_id = NEXTVAL('seq_place_rep');
    NEW.delete_id = NULL;
    NEW.tran_id = MAX(tran_id) FROM sams_place.transaction;

   -- An existing place-rep requires additional validation
  ELSE
    -- If no 'tran_id" specified use the latest transaction id
    IF (NEW.tran_id IS NULL) THEN
      NEW.tran_id = MAX(tran_id) FROM sams_place.transaction;
    ELSEIF NOT EXISTS (SELECT 1 FROM sams_place.transaction WHERE sams_place.transaction.tran_id = NEW.tran_id) THEN
      RAISE EXCEPTION 'Invalid tran_id specified.';
    END IF;

    -- 'rep_id' must reference an existing place-rep and have the most recent transaction ID
    IF NOT EXISTS (SELECT 1 FROM place_rep WHERE rep_id = NEW.rep_id) THEN
      RAISE EXCEPTION 'Invalid rep_id specified -- no matching row found.';
    ELSEIF EXISTS (SELECT 1 FROM place_rep WHERE rep_id = NEW.rep_id AND tran_id >= NEW.tran_id) THEN
      RAISE EXCEPTION 'Invalid rep_id/tran_id combination specified.';
    END IF;

    -- 'delete_id' must reference an existing place-rep entry
    IF (NEW.delete_id IS NOT NULL) THEN
      IF NOT EXISTS (SELECT 1 FROM place_rep WHERE rep_id = NEW.delete_id) THEN
        RAISE EXCEPTION 'Invalid delete_id specified -- no matching rep_id.';
      END IF;
    END IF;
  END IF;

  -- 'parent_id' must reference an exsting place-rep entry
  IF (NEW.parent_id IS NOT NULL) THEN
    IF NOT EXISTS (SELECT 1 FROM place_rep WHERE rep_id = NEW.parent_id) THEN
      RAISE EXCEPTION 'Invalid parent_id specified -- no matching rep_id.';
    END IF;
  END IF;

  -- 'owner_id' must reference an existing 'place' entry
  IF (NEW.owner_id IS NOT NULL) THEN
    IF NOT EXISTS (SELECT 1 FROM place WHERE place_id = NEW.owner_id) THEN
      RAISE EXCEPTION 'Invalid owner_id specified -- no matching place.';
    END IF;
  END IF;

  -- 'group_id' must reference an existing 'place_type_group' entry
  IF (NEW.group_id IS NOT NULL) THEN
    IF NOT EXISTS (SELECT 1 FROM place_type_group WHERE group_id = NEW.group_id) THEN
      RAISE EXCEPTION 'Invalid group_id specified -- no matching group.';
    END IF;
  END IF;

  -- 'parent_from_year' must precede [inclusively] 'parent_to_year'
  IF (NEW.parent_from_year IS NOT NULL  AND  NEW.parent_to_year IS NOT NULL  AND  NEW.parent_from_year > NEW.parent_to_year) THEN
    RAISE EXCEPTION 'parent_from_year can not be after parent_to_year';
  END IF;

  -- Return the NEW-ed row so the caller can get the values set here
  RETURN NEW;
END; 
$BODY$
LANGUAGE plpgsql;

CREATE TRIGGER bi_place_rep BEFORE INSERT ON place_rep FOR EACH ROW EXECUTE PROCEDURE fn_bi_place_rep();


-- ----------------------------------------------------------------------------
-- REP_DISPLAY_NAME table, sequence, trigger
-- ----------------------------------------------------------------------------
CREATE TABLE rep_display_name
(
   rep_id               INTEGER         NOT NULL,
   locale               VARCHAR(30)     NOT NULL,
   tran_id              INTEGER         NOT NULL,
   text                 VARCHAR(255)    NOT NULL,
   delete_flag          BOOLEAN         DEFAULT FALSE,

   CONSTRAINT pk_rep_disp_name PRIMARY KEY (rep_id,locale,tran_id),
   CONSTRAINT fk_rdn_tranid_to_tran_tranid FOREIGN KEY (tran_id) REFERENCES sams_place.transaction (tran_id)
);

CREATE OR REPLACE FUNCTION fn_bi_rep_disp_name() RETURNS TRIGGER AS
$BODY$
BEGIN
  SET SCHEMA 'sams_place';

  -- If no 'tran_id" specified use the latest transaction id
  IF (NEW.tran_id IS NULL) THEN
    NEW.tran_id = MAX(tran_id) FROM sams_place.transaction;
  ELSEIF NOT EXISTS (SELECT 1 FROM sams_place.transaction WHERE sams_place.transaction.tran_id = NEW.tran_id) THEN
    RAISE EXCEPTION 'Invalid tran_id specified.';
  END IF;

  -- 'rep_id' + 'locale' combination must have the most recent transaction ID
  IF (NEW.rep_id IS NOT NULL  AND  NEW.locale IS NOT NULL) THEN
   IF EXISTS (SELECT 1 FROM rep_display_name WHERE rep_id = NEW.rep_id AND NEW.locale = locale AND tran_id >= NEW.tran_id) THEN
      RAISE EXCEPTION 'Invalid rep_id/tran_id combination specified.';
    END IF;
  END IF;

  -- Return the NEW-ed row so the caller can get the values set here
  RETURN NEW;
END; 
$BODY$
LANGUAGE plpgsql;

CREATE TRIGGER bi_rep_disp_name BEFORE INSERT ON rep_display_name FOR EACH ROW EXECUTE PROCEDURE fn_bi_rep_disp_name();


-- ----------------------------------------------------------------------------
-- REP_ATTR table, sequence, trigger
-- ----------------------------------------------------------------------------
CREATE TABLE rep_attr
(
   attr_id              INTEGER         NOT NULL,
   tran_id              INTEGER         NOT NULL,
   rep_id               INTEGER         NOT NULL,
   attr_type_id         INTEGER         NOT NULL,
   year                 INTEGER,
   attr_value           VARCHAR(255),
   delete_flag          BOOLEAN         DEFAULT FALSE,

   CONSTRAINT pk_rep_attr PRIMARY KEY (attr_id, tran_id),
   CONSTRAINT fk_ra_tranid_to_tran_tranid FOREIGN KEY (tran_id) REFERENCES sams_place.transaction (tran_id),
   CONSTRAINT fk_ra_attrtype_to_type_typeid FOREIGN KEY (attr_type_id) REFERENCES sams_place.type (type_id)
);

CREATE SEQUENCE seq_rep_attr INCREMENT BY 1 START WITH 1 NO MAXVALUE;

CREATE OR REPLACE FUNCTION fn_bi_rep_attr() RETURNS TRIGGER AS
$BODY$
BEGIN
  SET SCHEMA 'sams_place';

  -- If this is a new attribute, then set 'delete_flag' to FALSE and use the latest transaction ID
  IF (NEW.attr_id IS NULL) THEN
    NEW.attr_id = NEXTVAL('seq_rep_attr');
    NEW.delete_flag = FALSE;
    NEW.tran_id = MAX(tran_id) FROM sams_place.transaction;

  -- An existing attribute requires additional validation
  ELSE
    -- If no 'tran_id" specified use the latest transaction id
    IF (NEW.tran_id IS NULL) THEN
      NEW.tran_id = MAX(tran_id) FROM sams_place.transaction;
    ELSEIF NOT EXISTS (SELECT 1 FROM sams_place.transaction WHERE sams_place.transaction.tran_id = NEW.tran_id) THEN
      RAISE EXCEPTION 'Invalid tran_id specified.';
    END IF;

    -- 'attr_id' must reference an existing attribute and have the most recent transaction ID
    IF NOT EXISTS (SELECT 1 FROM rep_attr WHERE attr_id = NEW.attr_id) THEN
      RAISE EXCEPTION 'Invalid attr_id specified -- no matching row found.';
    ELSEIF EXISTS (SELECT 1 FROM rep_attr WHERE attr_id = NEW.attr_id AND tran_id >= NEW.tran_id) THEN
      RAISE EXCEPTION 'Invalid attr_id/tran_id combination specified.';
    END IF;
  END IF;

  -- 'rep_id' must reference an existing place-rep and have the most recent transaction ID
  IF (NEW.rep_id IS NOT NULL) THEN
    IF NOT EXISTS (SELECT 1 FROM place_rep WHERE rep_id = NEW.rep_id) THEN
      RAISE EXCEPTION 'Invalid rep_id specified -- no matching row found.';
    ELSEIF EXISTS (SELECT 1 FROM place_rep WHERE rep_id = NEW.rep_id AND tran_id > NEW.tran_id) THEN
      RAISE EXCEPTION 'Invalid rep_id/tran_id combination specified.';
    END IF;
  END IF;

  -- Return the NEW-ed row so the caller can get the values set here
  RETURN NEW;
END; 
$BODY$
LANGUAGE plpgsql;

CREATE TRIGGER bi_rep_attr BEFORE INSERT ON rep_attr FOR EACH ROW EXECUTE PROCEDURE fn_bi_rep_attr();


-- ----------------------------------------------------------------------------
-- REP_BNDRY table, sequence, trigger
-- ----------------------------------------------------------------------------
CREATE TABLE rep_bndry
(
   bndry_id             INTEGER         NOT NULL,
   tran_id              INTEGER         NOT NULL,
   rep_id               INTEGER         NOT NULL,
   longitude            FLOAT(42)       NOT NULL,
   lattitude            FLOAT(42)       NOT NULL,
   next_bndry_id        INTEGER,
   delete_flag          BOOLEAN         DEFAULT FALSE,

   CONSTRAINT pk_rep_bndry PRIMARY KEY (bndry_id,tran_id),
   CONSTRAINT fk_rb_tranid_to_tran_tranid FOREIGN KEY (tran_id) REFERENCES sams_place.transaction (tran_id)
);

CREATE SEQUENCE seq_rep_bndry INCREMENT BY 1 START WITH 1 NO MAXVALUE;

CREATE OR REPLACE FUNCTION fn_bi_rep_bndry() RETURNS TRIGGER AS 
$BODY$
BEGIN
  SET SCHEMA 'sams_place';

  -- If this is a new boundary, then set 'delete_flag' to FALSE and  use the latest transaction ID
  IF (NEW.bndry_id IS NULL) THEN
    NEW.bndry_id = NEXTVAL('seq_rep_bndry');
    NEW.delete_flag = FALSE;
    NEW.tran_id = MAX(tran_id) FROM sams_place.transaction;

  -- An existing boundary requires additional validation
  ELSE
    -- If no 'tran_id" specified use the latest transaction id
    IF (NEW.tran_id IS NULL) THEN
      NEW.tran_id = MAX(tran_id) FROM sams_place.transaction;
    ELSEIF NOT EXISTS (SELECT 1 FROM sams_place.transaction WHERE sams_place.transaction.tran_id = NEW.tran_id) THEN
      RAISE EXCEPTION 'Invalid tran_id specified.';
    END IF;

    -- 'bndry_id' must reference an existing boundary and have the most recent transaction ID
    IF NOT EXISTS (SELECT 1 FROM rep_bndry WHERE bndry_id = NEW.bndry_id) THEN
      RAISE EXCEPTION 'Invalid bndry_id specified -- no matching row found.';
    ELSEIF EXISTS (SELECT 1 FROM rep_bndry WHERE bndry_id = NEW.bndry_id AND tran_id >= NEW.tran_id) THEN
      RAISE EXCEPTION 'Invalid bndry_id/tran_id combination specified.';
    END IF;
  END IF;

  -- 'rep_id' must reference an existing place-rep and have the most recent transaction ID
  IF (NEW.rep_id IS NOT NULL) THEN
    IF NOT EXISTS (SELECT 1 FROM place_rep WHERE rep_id = NEW.rep_id) THEN
      RAISE EXCEPTION 'Invalid rep_id specified -- no matching row found.';
    ELSEIF EXISTS (SELECT 1 FROM place_rep WHERE rep_id = NEW.rep_id AND tran_id >= NEW.tran_id) THEN
      RAISE EXCEPTION 'Invalid rep_id/tran_id combination specified.';
    END IF;
  END IF;

  -- 'next_bndry_id' must reference an existing boundary
  IF (NEW.next_bndry_id IS NOT NULL) THEN
    IF NOT EXISTS (SELECT 1 FROM rep_bndry WHERE bndry_id = NEW.next_bndry_id) THEN
      RAISE EXCEPTION 'Invalid next_bndry_id specified.';
    END IF;
  END IF;

  -- Return the NEW-ed row so the caller can get the values set here
  RETURN NEW;
END; 
$BODY$
LANGUAGE plpgsql;

CREATE TRIGGER bi_rep_bndry BEFORE INSERT ON rep_bndry FOR EACH ROW EXECUTE PROCEDURE fn_bi_rep_bndry();


-- ----------------------------------------------------------------------------
-- PLACE_VERSION table, trigger
-- ----------------------------------------------------------------------------
CREATE TABLE place_version
(
   version_id           SERIAL          NOT NULL  UNIQUE,
   tag                  VARCHAR(64)     NOT NULL,
   description          VARCHAR(255)    NOT NULL,
   tran_id              INTEGER         NOT NULL,
   create_ts            TIMESTAMP       NOT NULL DEFAULT now(),
   create_id            VARCHAR(30)     NOT NULL DEFAULT session_user,

   CONSTRAINT pk_plc_version PRIMARY KEY (version_id),
   CONSTRAINT fk_pv_tranid_to_tran_tranid FOREIGN KEY (tran_id) REFERENCES sams_place.transaction (tran_id)
);

CREATE OR REPLACE FUNCTION fn_bi_plc_version() RETURNS TRIGGER AS 
$BODY$
BEGIN
  SET SCHEMA 'sams_place';

  -- If no 'tran_id" specified use the latest transaction id
  IF (NEW.tran_id IS NULL) THEN
    NEW.tran_id = MAX(tran_id) FROM sams_place.transaction;
  ELSEIF NOT EXISTS (SELECT 1 FROM sams_place.transaction WHERE sams_place.transaction.tran_id = NEW.tran_id) THEN
    RAISE EXCEPTION 'Invalid tran_id specified.';
  END IF;

  -- Return the NEW-ed row so the caller can get the values set here
  RETURN NEW;
END; 
$BODY$
LANGUAGE plpgsql;

CREATE TRIGGER bi_plc_version BEFORE INSERT ON place_version FOR EACH ROW EXECUTE PROCEDURE fn_bi_plc_version();


-- ----------------------------------------------------------------------------
-- SOURCE table
-- ----------------------------------------------------------------------------
CREATE TABLE source
(
   source_id            SERIAL          NOT NULL UNIQUE,
   title                VARCHAR(255)    NOT NULL,
   description          VARCHAR(1023),
   pub_flag             BOOLEAN         NOT NULL  DEFAULT TRUE,
   create_ts            TIMESTAMP       NOT NULL  DEFAULT now(),
   create_id            VARCHAR(30)     NOT NULL  DEFAULT session_user,

   CONSTRAINT pk_source PRIMARY KEY (source_id)
);


-- ----------------------------------------------------------------------------
-- CITATION table, sequence, trigger
-- ----------------------------------------------------------------------------
CREATE TABLE citation
(
   citation_id          INTEGER         NOT NULL,
   tran_id              INTEGER         NOT NULL,
   source_id            INTEGER         NOT NULL,
   rep_id               INTEGER         NOT NULL,
   type_id              INTEGER         NOT NULL,
   citation_date        DATE,
   description          VARCHAR(1023),
   source_ref           VARCHAR(255),
   delete_flag          BOOLEAN         NOT NULL   DEFAULT FALSE,

   CONSTRAINT pk_citation PRIMARY KEY (citation_id, tran_id),
   CONSTRAINT fk_cit_src_id_to_src_src_id FOREIGN KEY (source_id) REFERENCES sams_place.source (source_id),
   CONSTRAINT fk_cit_type_id_to_type_type_id FOREIGN KEY (type_id) REFERENCES sams_place.type(type_id)
);

CREATE SEQUENCE seq_citation INCREMENT BY 1 START WITH 1 NO MAXVALUE;

CREATE OR REPLACE FUNCTION fn_bi_citation() RETURNS TRIGGER AS
$BODY$
BEGIN
  SET SCHEMA 'sams_place';

  -- If this is a new citation, then set link ID
  IF (NEW.citation_id IS NULL) THEN
    NEW.citation_id = NEXTVAL('seq_citation');
    NEW.tran_id = MAX(tran_id) FROM sams_place.transaction;

  -- An existing source requires additional validation
  ELSE
    -- If no 'tran_id" specified use the latest transaction id
    IF (NEW.tran_id IS NULL) THEN
      NEW.tran_id = MAX(tran_id) FROM sams_place.transaction;
    ELSEIF NOT EXISTS (SELECT 1 FROM sams_place.transaction WHERE sams_place.transaction.tran_id = NEW.tran_id) THEN
      RAISE EXCEPTION 'Invalid tran_id specified.';
    END IF;

    -- 'citation_id' must reference an existing citation and have the most recent transaction ID
    IF NOT EXISTS (SELECT 1 FROM citation WHERE citation_id = NEW.citation_id) THEN
      RAISE EXCEPTION 'Invalid citation_id specified -- no matching row found.';
    ELSEIF EXISTS (SELECT 1 FROM citation WHERE citation_id = NEW.citation_id AND tran_id >= NEW.tran_id) THEN
      RAISE EXCEPTION 'Invalid citation_id/tran_id combination specified.';
    END IF;
  END IF;

  -- 'rep_id' must reference an existing place-rep entry
  IF (NEW.rep_id IS NOT NULL) THEN
    IF NOT EXISTS (SELECT 1 FROM place_rep WHERE rep_id = NEW.rep_id) THEN
      RAISE EXCEPTION 'Invalid rep_id (PLACE_REP) specified.';
    END IF;
  END IF;

  -- Return the NEW-ed row so the caller can get the values set here
  RETURN NEW;
END;
$BODY$
LANGUAGE plpgsql;

CREATE TRIGGER bi_citation BEFORE INSERT ON citation FOR EACH ROW EXECUTE PROCEDURE fn_bi_citation();


-- ----------------------------------------------------------------------------
-- PLACE_TYPE_GROUP table, sequence, trigger
-- ----------------------------------------------------------------------------
CREATE TABLE place_type_group
(
  group_id             INTEGER        NOT NULL,
  tran_id              INTEGER        NOT NULL,
  name                 VARCHAR(30)    NOT NULL,
  description          VARCHAR(255),
  pub_flag             BOOLEAN        NOT NULL DEFAULT TRUE,
  delete_flag          BOOLEAN        NOT NULL DEFAULT FALSE,

  CONSTRAINT pk_place_type_group PRIMARY KEY (group_id, tran_id),
  CONSTRAINT fk_ptg_tranid_to_tran_tranid FOREIGN KEY (tran_id) REFERENCES sams_place.transaction (tran_id)
);

CREATE SEQUENCE seq_plc_grp INCREMENT BY 1 START WITH 1 NO MAXVALUE;

CREATE OR REPLACE FUNCTION fn_bi_place_type_group() RETURNS TRIGGER AS
$BODY$
BEGIN
  SET SCHEMA 'sams_place';

  -- If this is a new place-type group, then set 'delete_flag' to FALSE and use the latest transaction ID
  IF (NEW.group_id IS NULL) THEN
    NEW.group_id = NEXTVAL('seq_plc_grp');
    NEW.delete_flag = FALSE;
    NEW.tran_id = MAX(tran_id) FROM sams_place.transaction;

  -- An existing place-type group requires additional validation
  ELSE
    -- If no 'tran_id" specified use the latest transaction id
    IF (NEW.tran_id IS NULL) THEN
      NEW.tran_id = MAX(tran_id) FROM sams_place.transaction;
    ELSEIF NOT EXISTS (SELECT 1 FROM sams_place.transaction WHERE sams_place.transaction.tran_id = NEW.tran_id) THEN
      RAISE EXCEPTION 'Invalid tran_id specified.';
    END IF;

    -- 'group_id' must reference an existing place-type-group and have the most recent transaction ID
    IF NOT EXISTS (SELECT 1 FROM place_type_group WHERE group_id = NEW.group_id) THEN
      RAISE EXCEPTION 'Invalid group_id specified -- no matching row found.';
    ELSEIF EXISTS (SELECT 1 FROM place_type_group WHERE group_id = NEW.group_id AND tran_id >= NEW.tran_id) THEN
      RAISE EXCEPTION 'Invalid group_id/tran_id combination specified.';
    END IF;
  END IF;

  -- Return the NEW-ed row so the caller can get the values set here
  RETURN NEW;
END;
$BODY$
LANGUAGE plpgsql;


CREATE TRIGGER bi_place_type_group BEFORE INSERT ON place_type_group FOR EACH ROW EXECUTE PROCEDURE fn_bi_place_type_group();


-- ----------------------------------------------------------------------------
-- PLACE_TYPE_GROUP_MEMBER table, trigger
-- ----------------------------------------------------------------------------
CREATE TABLE place_type_group_member
(
  group_id             INTEGER         NOT NULL,
  tran_id              INTEGER         NOT NULL,
  place_type_id        INTEGER         NOT NULL,
  delete_flag          BOOLEAN         NOT NULL DEFAULT FALSE,

  CONSTRAINT pk_place_type_group_member PRIMARY KEY (group_id, tran_id, place_type_id),
  CONSTRAINT fk_ptgm_plctyp_to_conceptid FOREIGN KEY (place_type_id) REFERENCES sams_place.type (type_id),
  CONSTRAINT fk_ptgm_tranid_to_tran_tranid FOREIGN KEY (tran_id) REFERENCES sams_place.transaction (tran_id)
);

CREATE OR REPLACE FUNCTION fn_bi_place_type_group_member() RETURNS TRIGGER AS
$BODY$
BEGIN
  SET SCHEMA 'sams_place';

  -- If no 'tran_id" specified use the latest transaction id
  IF (NEW.tran_id IS NULL) THEN
    NEW.tran_id = MAX(tran_id) FROM sams_place.transaction;
  ELSEIF NOT EXISTS (SELECT 1 FROM sams_place.transaction WHERE sams_place.transaction.tran_id = NEW.tran_id) THEN
    RAISE EXCEPTION 'Invalid tran_id specified.';
  END IF;

  -- 'group_id' must reference an existing place-type group
  IF (NEW.group_id IS NOT NULL) THEN
    IF NOT EXISTS (SELECT group_id FROM sams_place.place_type_group WHERE group_id = NEW.group_id) THEN
      RAISE EXCEPTION 'Invalid group_id specified -- no matching row found.';
    END IF;
  END IF;

  -- 'place_type_id" must reference an existing concept
  IF (NEW.place_type_id IS NOT NULL) THEN
    IF NOT EXISTS (SELECT type_id FROM sams_place.type WHERE type_id = NEW.place_type_id) THEN
       RAISE EXCEPTION 'Invalid place_type_id specified -- no matching row found.';
    END IF;
  END IF;

  -- Check to see if this group/place-type combination already exists; if so, ensure
  -- that this record has a later TRAN_ID ...
  IF EXISTS (SELECT 1 FROM place_type_group_member WHERE group_id = NEW.group_id AND place_type_id = NEW.place_type_id AND tran_id >= NEW.tran_id) THEN
    RAISE EXCEPTION 'Invalid group_id/place_type_id/tran_id combination specified.';
  END IF;

  -- Return the NEW-ed row so the caller can get the values set here
  RETURN NEW;
END;
$BODY$
LANGUAGE plpgsql;

CREATE TRIGGER bi_place_type_group_member BEFORE INSERT ON place_type_group_member FOR EACH ROW EXECUTE PROCEDURE fn_bi_place_type_group_member();


-- ----------------------------------------------------------------------------
-- PLACE_TYPE_SUB_GROUP table, sequence, trigger
-- ----------------------------------------------------------------------------
CREATE TABLE place_type_sub_group
(
  parent_group_id      INTEGER         NOT NULL,
  sub_group_id         INTEGER         NOT NULL,
  tran_id              INTEGER         NOT NULL,
  delete_flag          BOOLEAN         NOT NULL DEFAULT FALSE,

  CONSTRAINT pk_place_type_sub_group PRIMARY KEY (parent_group_id, sub_group_id, tran_id),
  CONSTRAINT fk_ptsg_tranid_to_tran_tranid FOREIGN KEY (tran_id) REFERENCES sams_place.transaction (tran_id)
--  CONSTRAINT fk_ptsg_grpid_to_group_group_id FOREIGN KEY (parent_group_id) REFERENCES place_type_group (group_id)
);

CREATE OR REPLACE FUNCTION fn_bi_place_type_sub_group() RETURNS TRIGGER AS
$BODY$
BEGIN
  SET SCHEMA 'sams_place';

  -- If no 'tran_id" specified use the latest transaction id
  IF (NEW.tran_id IS NULL) THEN
    NEW.tran_id = MAX(tran_id) FROM sams_place.transaction;
  ELSEIF NOT EXISTS (SELECT 1 FROM sams_place.transaction WHERE sams_place.transaction.tran_id = NEW.tran_id) THEN
    RAISE EXCEPTION 'Invalid tran_id specified.';
  END IF;

  -- 'group_id' must reference an exsting place-type group entry
  IF (NEW.parent_group_id IS NOT NULL) THEN
    IF NOT EXISTS (SELECT 1 FROM place_type_group WHERE group_id = NEW.parent_group_id) THEN
      RAISE EXCEPTION 'Invalid group_id specified -- no matching row found.';
    END IF;
  END IF;

  -- 'sub_group_id' must reference an exsting place-type group entry
  IF (NEW.sub_group_id IS NOT NULL) THEN
    IF NOT EXISTS (SELECT 1 FROM place_type_group WHERE group_id = NEW.sub_group_id) THEN
      RAISE EXCEPTION 'Invalid sub_group_id specified -- no matching row found.';
    END IF;
  END IF;

  -- Check to see if this group/place-type combination already exists; if so, ensure
  -- that this record has a later TRAN_ID ...
  IF EXISTS (SELECT 1 FROM place_type_sub_group WHERE parent_group_id = NEW.parent_group_id AND sub_group_id = NEW.sub_group_id AND tran_id >= NEW.tran_id) THEN
    RAISE EXCEPTION 'Invalid parent_group_id/sub_group_id/tran_id combination specified.';
  END IF;

  -- Logic to prevent cycles from occuring ... if the child's sub-group chain,
  -- calculated recursively, includes the parent, then don't add this.  NOTE: only
  -- look 10 levels deep in the tree, which should be plenty!!
  IF EXISTS (
    WITH RECURSIVE CTE_GROUP_HIER(parent_group_id, sub_group_id, depth) AS (
      SELECT rsg.parent_group_id, rsg.sub_group_id, 1 AS depth
        FROM sams_place.place_type_sub_group AS rsg
       WHERE rsg.parent_group_id = NEW.sub_group_id
    UNION ALL
      SELECT rsg1.parent_group_id, rsg1.sub_group_id, (rsgh.depth + 1) AS depth
        FROM CTE_GROUP_HIER AS rsgh
        JOIN sams_place.place_type_sub_group AS rsg1
          ON rsg1.parent_group_id = rsgh.sub_group_id
    )
    SELECT 1 FROM CTE_GROUP_HIER
     WHERE depth < 11
       AND sub_group_id = NEW.parent_group_id)
  THEN
    RAISE EXCEPTION 'parent_group_id/sub_group_id combination would create a cycle in the chain!!';
  END IF;

  -- Return the NEW-ed row so the caller can get the values set here
  RETURN NEW;
END;
$BODY$
LANGUAGE plpgsql;

CREATE TRIGGER bi_place_type_sub_group BEFORE INSERT ON place_type_sub_group FOR EACH ROW EXECUTE PROCEDURE fn_bi_place_type_sub_group();


-- ----------------------------------------------------------------------------
-- SCHEMA_VERSION table
-- ----------------------------------------------------------------------------
CREATE TABLE schema_version
(
  major_version        INTEGER         NOT NULL,
  minor_version        INTEGER         NOT NULL,
  version_date         TIMESTAMP       NOT NULL DEFAULT now()
);

INSERT INTO schema_version(major_version, minor_version) VALUES(1, 0);


-- ----------------------------------------------------------------------------
-- Create indexes
-- ----------------------------------------------------------------------------
CREATE INDEX idx_pn_text ON place_name (text);
CREATE INDEX idx_pn_txt_locale ON place_name (text, locale);
CREATE INDEX idx_pn_place_id ON place_name (place_id);
CREATE INDEX idx_pr_parent ON place_rep (parent_id);
CREATE INDEX idx_pr_owner ON place_rep (owner_id);
CREATE INDEX idx_rdn_rep_id ON rep_display_name (rep_id);
CREATE INDEX idx_rdn_text ON rep_display_name (text);
CREATE INDEX idx_rb_rep_id ON rep_bndry (rep_id);
CREATE INDEX idx_ra_rep_id ON rep_attr (rep_id);
CREATE INDEX idx_cit_rep_id ON citation (rep_id);
CREATE INDEX idx_plc_sub_grp ON place_type_sub_group (sub_group_id);
CREATE INDEX idx_pv_tag ON place_version (tag);
CREATE INDEX idx_term_cat_type ON type (type_cat);
CREATE INDEX idx_term_type_id ON type_term (type_id);


-- ----------------------------------------------------------------------------
-- Grant privileges to the "places" schema
-- ----------------------------------------------------------------------------
-- CREATE USER sams_place WITH PASSWORD 'sams_place';

GRANT ALL PRIVILEGES ON SCHEMA sams_place TO sams_place;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA sams_place TO sams_place;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA sams_place TO sams_place;
