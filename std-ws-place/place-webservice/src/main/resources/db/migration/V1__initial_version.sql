-- DDL USING SYNTAX FOR POSTGRES - USE DEFAULT TABLESPACE FOR EVERYTHING ...

-- ----------------------------------------------------------------------------
-- Set the schema which is to be used
-- ----------------------------------------------------------------------------
-- SET SCHEMA 'sams_place';


-- ----------------------------------------------------------------------------
-- Enable postgis extensions
-- ----------------------------------------------------------------------------
-- CREATE EXTENSION postgis;


-- ----------------------------------------------------------------------------
-- TRANSACTION table, and implied sequence
-- ----------------------------------------------------------------------------
CREATE TABLE transaction (
    tran_id        SERIAL        NOT NULL  UNIQUE,
    create_ts      TIMESTAMP     NOT NULL  DEFAULT now(),
    create_id      VARCHAR(30)   NOT NULL  DEFAULT session_user,

    CONSTRAINT pk_transaction PRIMARY KEY (tran_id)
);

INSERT INTO transaction DEFAULT VALUES;


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
    CONSTRAINT ck_type_cat CHECK (type_cat IN ('PLACE', 'NAME', 'ATTRIBUTE', 'CITATION', 'EXT_XREF', 'RESOLUTION')),
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
-- GROUP_DEF table
-- ----------------------------------------------------------------------------
CREATE TABLE group_def
(
    group_id             SERIAL         NOT NULL UNIQUE,
    group_type           VARCHAR(12)    NOT NULL,
    pub_flag             BOOLEAN        NOT NULL DEFAULT TRUE,

    CONSTRAINT pk_group PRIMARY KEY (group_id),
    CONSTRAINT ck_group_type CHECK (group_type IN ('PLACE_REP', 'PLACE_TYPE'))
);


-- ----------------------------------------------------------------------------
-- PLACE_TYPE_SUB_GROUP table, trigger
-- ----------------------------------------------------------------------------
CREATE TABLE group_hierarchy
(
    parent_group_id      INTEGER         NOT NULL,
    child_group_id       INTEGER         NOT NULL,
    delete_flag          BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT pk_grp_hierarchy PRIMARY KEY (parent_group_id, child_group_id),
    CONSTRAINT fk_gh_parentid_to_group FOREIGN KEY (parent_group_id) REFERENCES group_def (group_id),
    CONSTRAINT fk_gh_childid_to_group FOREIGN KEY (child_group_id) REFERENCES group_def (group_id)
);

CREATE OR REPLACE FUNCTION fn_bi_group_hierarchy() RETURNS TRIGGER AS
$BODY$
DECLARE
  typeOne VARCHAR(12);
  typeTwo VARCHAR(12);

BEGIN
-- SET SCHEMA 'sams_place';

  -- Ensure that the parent and child group IDs reference the same type
  IF NOT EXISTS (SELECT 1 FROM group_def WHERE group_id = NEW.parent_group_id) THEN
    RAISE EXCEPTION 'Invalid parent group-id specified -- no matching row found.';
  ELSEIF NOT EXISTS (SELECT 1 FROM group_def WHERE group_ID = NEW.child_group_id) THEN
    RAISE EXCEPTION 'Invalid child group-id specified -- no matching row found.';
  ELSE
    typeOne := group_type FROM group_def WHERE group_id = NEW.parent_group_id;
    typeTwo := group_type FROM group_def WHERE group_id = NEW.child_group_id;
    if (typeOne <> typeTwo) THEN
      RAISE EXCEPTION 'Parent and child group-id values are of different types.';
    END IF;
  END IF;

  -- Logic to prevent cycles from occuring ... if the child's sub-group chain,
  -- calculated recursively, includes the parent, then don't add this.  NOTE: only
  -- look 10 levels deep in the tree, which should be plenty!!
  IF EXISTS (
    WITH RECURSIVE CTE_GROUP_HIER(parent_group_id, child_group_id, depth) AS (
      SELECT rsg.parent_group_id, rsg.child_group_id, 1 AS depth
        FROM group_hierarchy AS rsg
       WHERE rsg.parent_group_id = NEW.child_group_id
    UNION ALL
      SELECT rsg1.parent_group_id, rsg1.child_group_id, (rsgh.depth + 1) AS depth
        FROM CTE_GROUP_HIER AS rsgh
        JOIN group_hierarchy AS rsg1
          ON rsg1.parent_group_id = rsgh.child_group_id
    )
    SELECT 1 FROM CTE_GROUP_HIER
     WHERE depth < 11
       AND child_group_id = NEW.parent_group_id)
  THEN
    RAISE EXCEPTION 'parent_group_id/child_group_id combination would create a cycle in the chain!!';
  END IF;

  -- Return the NEW-ed row so the caller can get the values set here
  RETURN NEW;
END;
$BODY$
LANGUAGE plpgsql;

CREATE TRIGGER bi_group_hierarchy BEFORE INSERT ON group_hierarchy FOR EACH ROW EXECUTE PROCEDURE fn_bi_group_hierarchy();


-- ----------------------------------------------------------------------------
-- GROUP_MEMBER table, trigger
-- ----------------------------------------------------------------------------
CREATE TABLE group_member
(
    group_id             INTEGER         NOT NULL,
    entity_id            INTEGER         NOT NULL,
    delete_flag          BOOLEAN         NOT NULL DEFAULT FALSE,

    CONSTRAINT pk_grp_member PRIMARY KEY (group_id, entity_id),
    CONSTRAINT fk_gm_groupid_to_group FOREIGN KEY (group_id) REFERENCES group_def (group_id)
);


-- ----------------------------------------------------------------------------
-- GROUP_TERM table
-- ----------------------------------------------------------------------------
CREATE TABLE group_term
(
    term_id              SERIAL         NOT NULL UNIQUE,
    group_id             INTEGER        NOT NULL,
    locale               VARCHAR(30)    NOT NULL,
    name                 VARCHAR(30)    NOT NULL,
    description          VARCHAR(255),

    CONSTRAINT pk_grp_term PRIMARY KEY (term_id),
    CONSTRAINT nq_gt_group_and_locale UNIQUE (group_id, locale),
    CONSTRAINT fk_gt_grpid_to_group FOREIGN KEY (group_id) REFERENCES group_def (group_id)
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
   CONSTRAINT fk_pl_tranid_to_tran FOREIGN KEY (tran_id) REFERENCES transaction (tran_id)
);

CREATE SEQUENCE seq_place INCREMENT BY 1 START WITH 1 NO MAXVALUE;

CREATE OR REPLACE FUNCTION fn_bi_place() RETURNS TRIGGER AS
$BODY$
BEGIN
  -- SET SCHEMA 'sams_place';

  -- If this is a new place, then set "delete_id" to NULL and use the latest transaction ID
  IF (NEW.place_id IS NULL) THEN
    NEW.place_id = NEXTVAL('seq_place');
    NEW.delete_id = NULL;
    NEW.tran_id = MAX(tran_id) FROM transaction;

  -- An existing place requires additional validation
  ELSE
    -- If no 'tran_id" specified use the latest transaction id
    IF (NEW.tran_id IS NULL) THEN
      NEW.tran_id = MAX(tran_id) FROM transaction;
    ELSEIF NOT EXISTS (SELECT 1 FROM transaction WHERE transaction.tran_id = NEW.tran_id) THEN
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
   CONSTRAINT fk_pn_tranid_to_tran FOREIGN KEY (tran_id) REFERENCES transaction (tran_id),
   CONSTRAINT fk_pn_typeid_to_type FOREIGN KEY (type_id) REFERENCES type (type_id)
);

CREATE SEQUENCE seq_place_name INCREMENT BY 1 START WITH 1 NO MAXVALUE;

CREATE OR REPLACE FUNCTION fn_bi_place_name() RETURNS TRIGGER AS
$BODY$
BEGIN
  -- SET SCHEMA 'sams_place';

  -- If this is a new name, then set 'delete_flag' to FALSE and use the latest transaction ID
  IF (NEW.name_id IS NULL) THEN
    NEW.name_id = NEXTVAL('seq_place_name');
    NEW.delete_flag = FALSE;
    NEW.tran_id = MAX(tran_id) FROM transaction;

  -- An existing name requires additional validation
  ELSE
    -- If no 'tran_id" specified use the latest transaction id
    IF (NEW.tran_id IS NULL) THEN
      NEW.tran_id = MAX(tran_id) FROM transaction;
    ELSEIF NOT EXISTS (SELECT 1 FROM transaction WHERE transaction.tran_id = NEW.tran_id) THEN
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
   CONSTRAINT fk_pr_tranid_to_tran FOREIGN KEY (tran_id) REFERENCES transaction (tran_id),
   CONSTRAINT fk_pr_plctp_to_type FOREIGN KEY (place_type_id) REFERENCES type (type_id),
   CONSTRAINT fk_pr_grpid_to_group FOREIGN KEY (group_id) REFERENCES group_def (group_id)
);

CREATE SEQUENCE seq_place_rep INCREMENT BY 1 START WITH 1 NO MAXVALUE;

CREATE OR REPLACE FUNCTION fn_bi_place_rep() RETURNS TRIGGER AS 
$BODY$
BEGIN
  -- SET SCHEMA 'sams_place';

  -- If this is a new place-rep, then set "delete_id" to NULL and use the latest transaction ID
  IF (NEW.rep_id IS NULL) THEN
    NEW.rep_id = NEXTVAL('seq_place_rep');
    NEW.delete_id = NULL;
    NEW.tran_id = MAX(tran_id) FROM transaction;

   -- An existing place-rep requires additional validation
  ELSE
    -- If no 'tran_id" specified use the latest transaction id
    IF (NEW.tran_id IS NULL) THEN
      NEW.tran_id = MAX(tran_id) FROM transaction;
    ELSEIF NOT EXISTS (SELECT 1 FROM transaction WHERE transaction.tran_id = NEW.tran_id) THEN
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
   CONSTRAINT fk_rdn_tranid_to_tran FOREIGN KEY (tran_id) REFERENCES transaction (tran_id)
);

CREATE OR REPLACE FUNCTION fn_bi_rep_disp_name() RETURNS TRIGGER AS
$BODY$
BEGIN
  -- SET SCHEMA 'sams_place';

  -- If no 'tran_id" specified use the latest transaction id
  IF (NEW.tran_id IS NULL) THEN
    NEW.tran_id = MAX(tran_id) FROM transaction;
  ELSEIF NOT EXISTS (SELECT 1 FROM transaction WHERE transaction.tran_id = NEW.tran_id) THEN
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
   locale               VARCHAR(30),
   attr_value           VARCHAR(2048),
   delete_flag          BOOLEAN         DEFAULT FALSE,

   CONSTRAINT pk_rep_attr PRIMARY KEY (attr_id, tran_id),
   CONSTRAINT fk_ra_tranid_to_tran FOREIGN KEY (tran_id) REFERENCES transaction (tran_id),
   CONSTRAINT fk_ra_attrtype_to_type FOREIGN KEY (attr_type_id) REFERENCES type (type_id)
);

CREATE SEQUENCE seq_rep_attr INCREMENT BY 1 START WITH 1 NO MAXVALUE;

CREATE OR REPLACE FUNCTION fn_bi_rep_attr() RETURNS TRIGGER AS
$BODY$
BEGIN
  -- SET SCHEMA 'sams_place';

  -- If this is a new attribute, then set 'delete_flag' to FALSE and use the latest transaction ID
  IF (NEW.attr_id IS NULL) THEN
    NEW.attr_id = NEXTVAL('seq_rep_attr');
    NEW.delete_flag = FALSE;
    NEW.tran_id = MAX(tran_id) FROM transaction;

    -- enforce uniqueness of rep_id/attr_type_id/year/locale, the latter two may be null
    IF (NEW.locale IS NULL AND NEW.year IS NULL) THEN
      IF EXISTS (SELECT 1 FROM rep_attr WHERE rep_id = NEW.rep_id AND attr_id = NEW.attr_id AND year IS NULL AND locale IS NULL) THEN
        RAISE EXCEPTION 'Duplicate rep_id/attr_id/year=null/locale=null not allowed.';
      END IF;
    ELSEIF (NEW.year IS NULL) THEN
      IF EXISTS (SELECT 1 FROM rep_attr WHERE rep_id = NEW.rep_id AND attr_id = NEW.attr_id AND year IS NULL AND locale = NEW.locale) THEN
        RAISE EXCEPTION 'Duplicate rep_id/attr_id/year=null/locale not allowed.';
      END IF;
    ELSEIF (NEW.locale IS NULL) THEN
      IF EXISTS (SELECT 1 FROM rep_attr WHERE rep_id = NEW.rep_id AND attr_id = NEW.attr_id AND year = NEW.year AND locale IS NULL) THEN
        RAISE EXCEPTION 'Duplicate rep_id/attr_id/year/locale=null not allowed.';
      END IF;
    ELSE
      IF EXISTS (SELECT 1 FROM rep_attr WHERE rep_id = NEW.rep_id AND attr_id = NEW.attr_id AND year = NEW.year AND locale = NEW.locale) THEN
        RAISE EXCEPTION 'Duplicate rep_id/attr_id/year/locale not allowed.';
      END IF;
    END IF;

  -- An existing attribute requires additional validation
  ELSE
    -- If no 'tran_id" specified use the latest transaction id
    IF (NEW.tran_id IS NULL) THEN
      NEW.tran_id = MAX(tran_id) FROM transaction;
    ELSEIF NOT EXISTS (SELECT 1 FROM transaction WHERE transaction.tran_id = NEW.tran_id) THEN
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
   CONSTRAINT fk_pv_tranid_to_tran FOREIGN KEY (tran_id) REFERENCES transaction (tran_id)
);

CREATE OR REPLACE FUNCTION fn_bi_plc_version() RETURNS TRIGGER AS 
$BODY$
BEGIN
  -- SET SCHEMA 'sams_place';

  -- If no 'tran_id" specified use the latest transaction id
  IF (NEW.tran_id IS NULL) THEN
    NEW.tran_id = MAX(tran_id) FROM transaction;
  ELSEIF NOT EXISTS (SELECT 1 FROM transaction WHERE transaction.tran_id = NEW.tran_id) THEN
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
   CONSTRAINT fk_cit_srcid_to_src FOREIGN KEY (source_id) REFERENCES source (source_id),
   CONSTRAINT fk_cit_typeid_to_type FOREIGN KEY (type_id) REFERENCES type(type_id)
);

CREATE SEQUENCE seq_citation INCREMENT BY 1 START WITH 1 NO MAXVALUE;

CREATE OR REPLACE FUNCTION fn_bi_citation() RETURNS TRIGGER AS
$BODY$
BEGIN
  -- SET SCHEMA 'sams_place';

  -- If this is a new citation, then set link ID
  IF (NEW.citation_id IS NULL) THEN
    NEW.citation_id = NEXTVAL('seq_citation');
    NEW.tran_id = MAX(tran_id) FROM transaction;

  -- An existing source requires additional validation
  ELSE
    -- If no 'tran_id" specified use the latest transaction id
    IF (NEW.tran_id IS NULL) THEN
      NEW.tran_id = MAX(tran_id) FROM transaction;
    ELSEIF NOT EXISTS (SELECT 1 FROM transaction WHERE transaction.tran_id = NEW.tran_id) THEN
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
-- EXTERNAL_XREF table, implied sequence, function and trigger
-- ----------------------------------------------------------------------------
CREATE TABLE external_xref (
    xref_id        SERIAL         NOT NULL UNIQUE,
    rep_id         INT4           NOT NULL,
    type_id        INT4           NOT NULL,
    external_key   VARCHAR(512)   NOT NULL,
    pub_flag       BOOLEAN        DEFAULT FALSE,

    CONSTRAINT pk_ext_xref PRIMARY KEY (xref_id),
    CONSTRAINT fk_ex_typeid_to_type FOREIGN KEY (type_id) REFERENCES type (type_id)
);

CREATE OR REPLACE FUNCTION fn_bi_ext_xref() RETURNS TRIGGER AS
$BODY$
BEGIN
  -- SET SCHEMA 'sams_place';

  -- 'rep_id' must reference an existing place-rep
  IF (NEW.rep_id IS NOT NULL) THEN
    IF NOT EXISTS (SELECT 1 FROM place_rep WHERE rep_id = NEW.rep_id) THEN
      RAISE EXCEPTION 'Invalid rep_id specified -- no matching row found.';
    END IF;
  END IF;
  
  -- Return the NEW-ed row so the caller can get the values set here
  RETURN NEW;
END; 
$BODY$
LANGUAGE plpgsql;

CREATE TRIGGER bi_ext_xref BEFORE INSERT ON external_xref FOR EACH ROW EXECUTE PROCEDURE fn_bi_ext_xref();


-- ----------------------------------------------------------------------------
-- SCHEMA_VERSION table
-- ----------------------------------------------------------------------------
-- CREATE TABLE schema_version
-- (
--   major_version        INTEGER         NOT NULL,
--   minor_version        INTEGER         NOT NULL,
--   version_date         TIMESTAMP       NOT NULL DEFAULT now()
-- );

-- INSERT INTO schema_version(major_version, minor_version) VALUES(1, 0);


-- ----------------------------------------------------------------------------
-- Create indexes
-- ----------------------------------------------------------------------------
CREATE INDEX idx_term_cat_type ON type (type_cat);
CREATE INDEX idx_term_type_id ON type_term (type_id);
CREATE INDEX idx_pn_text ON place_name (text);
CREATE INDEX idx_pn_txt_locale ON place_name (text, locale);
CREATE INDEX idx_pn_place_id ON place_name (place_id);
CREATE INDEX idx_pr_parent ON place_rep (parent_id);
CREATE INDEX idx_pr_owner ON place_rep (owner_id);
CREATE INDEX idx_rdn_rep_id ON rep_display_name (rep_id);
CREATE INDEX idx_rdn_text ON rep_display_name (text);
CREATE INDEX idx_ra_rep_id ON rep_attr (rep_id);
CREATE INDEX idx_cit_rep_id ON citation (rep_id);
CREATE INDEX idx_cit_source_id ON citation (source_id);
CREATE INDEX idx_ext_xref_rep_id ON  external_xref (rep_id);
CREATE INDEX idx_grp_hier_par ON group_hierarchy (parent_group_id);
CREATE INDEX idx_grp_hier_sub ON group_hierarchy (child_group_id);
CREATE INDEX idx_grp_mbr ON group_member (group_id);
CREATE INDEX idx_grp_mbr_type ON group_member (entity_id);
CREATE INDEX idx_grp_term ON group_term (group_id);
CREATE INDEX idx_pv_tag ON place_version (tag);


-- ----------------------------------------------------------------------------
-- Grant privileges to the "places" schema
-- ----------------------------------------------------------------------------

-- GRANT ALL PRIVILEGES ON SCHEMA sams_place TO sams_place;
-- GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA sams_place TO sams_place;
-- GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA sams_place TO sams_place;
