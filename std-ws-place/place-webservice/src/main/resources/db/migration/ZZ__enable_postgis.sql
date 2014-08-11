-- DDL USING SYNTAX FOR POSTGRES - USE DEFAULT TABLESPACE FOR EVERYTHING ...


-- ----------------------------------------------------------------------------
-- Enable postgis extensions
-- ----------------------------------------------------------------------------
CREATE EXTENSION postgis;


- ----------------------------------------------------------------------------
-- BOUNDARY table, sequence, function and trigger
-- ----------------------------------------------------------------------------
CREATE TABLE boundary (
    boundary_id    INT4           NOT NULL,
    tran_id        INT4           NOT NULL,
    description    VARCHAR(255),
    from_date      VARCHAR(32),
    to_date        VARCHAR(32),
    delete_flag    BOOLEAN        DEFAULT FALSE,

    CONSTRAINT pk_boundary PRIMARY KEY (boundary_id, tran_id)
);

CREATE SEQUENCE seq_boundary INCREMENT BY 1 START WITH 1 NO MAXVALUE;

CREATE OR REPLACE FUNCTION fn_bi_boundary() RETURNS TRIGGER AS
$BODY$
BEGIN
  -- SET SCHEMA 'sams_place';

  -- If this is a new boundary, then set 'delete_flag' to FALSE and use the latest transaction ID
  IF (NEW.boundary_id IS NULL) THEN
    NEW.boundary_id = NEXTVAL('seq_boundary');
    NEW.delete_flag = FALSE;
    NEW.tran_id = MAX(tran_id) FROM transaction;

  -- An existing boundary requires additional validation
  ELSE
    -- If no 'tran_id' specified use the latest transaction id
    IF (NEW.tran_id IS NULL) THEN
      NEW.tran_id = MAX(tran_id) FROM transaction;
    ELSEIF NOT EXISTS (SELECT 1 FROM transaction WHERE transaction.tran_id = NEW.tran_id) THEN
      RAISE EXCEPTION 'Invalid tran_id specified.';
    END IF;

    -- 'boundary_id' must reference an existing boundary and have the most recent transaction ID
    IF NOT EXISTS (SELECT 1 FROM boundary WHERE boundary_id = NEW.boundary_id) THEN
      RAISE EXCEPTION 'Invalid boundary_id specified -- no matching row found.';
    ELSEIF EXISTS (SELECT 1 FROM boundary WHERE boundary_id = NEW.boundary_id AND tran_id >= NEW.tran_id) THEN
      RAISE EXCEPTION 'Invalid boundary_id/tran_id combination specified.';
    END IF;
  END IF;

  -- Return the NEW-ed row so the caller can get the values set here
  RETURN NEW;
END; 
$BODY$
LANGUAGE plpgsql;

CREATE TRIGGER bi_boundary BEFORE INSERT ON boundary FOR EACH ROW EXECUTE PROCEDURE fn_bi_boundary();


-- ----------------------------------------------------------------------------
-- BOUNDARY_DETAIL table, function and trigger
-- ----------------------------------------------------------------------------
CREATE TABLE boundary_detail (
    boundary_id    INT4           NOT NULL,
    tran_id        INT4           NOT NULL,
    type_id        INT4           NOT NULL,
    point_count    INT4,
    boundary_data  GEOGRAPHY      NOT NULL,
    delete_flag    BOOLEAN        DEFAULT FALSE,

    CONSTRAINT pk_bnd_detail PRIMARY KEY (boundary_id,tran_id,type_id),
    CONSTRAINT fk_bnddet_typeid_to_type FOREIGN KEY (type_id) REFERENCES type (type_id)
);

CREATE OR REPLACE FUNCTION fn_bi_bnd_detail() RETURNS TRIGGER AS
$BODY$
BEGIN
  -- SET SCHEMA 'sams_place';

  -- If no 'tran_id' specified use the latest transaction id, otherwise validate the boundary_id/type_id combination
  IF (NEW.tran_id IS NULL) THEN
    NEW.tran_id = MAX(tran_id) FROM transaction;
  ELSEIF NOT EXISTS (SELECT 1 FROM transaction WHERE transaction.tran_id = NEW.tran_id) THEN
    RAISE EXCEPTION 'Invalid tran_id specified.';
  ELSEIF EXISTS (SELECT 1 FROM boundary_detail WHERE boundary_id = NEW.boundary_id AND type_id = NEW.type_id AND tran_id >= NEW.tran_id) THEN
    RAISE EXCEPTION 'Invalid boundary_id/type_id/tran_id combination specified.';
  END IF;

  -- 'boundary_id' must reference an existing boundary
  IF NOT EXISTS (SELECT 1 FROM boundary WHERE boundary_id = NEW.boundary_id) THEN
    RAISE EXCEPTION 'Invalid boundary_id specified -- no matching row found.';
  END IF;
  
  -- Return the NEW-ed row so the caller can get the values set here
  RETURN NEW;
END; 
$BODY$
LANGUAGE plpgsql;

CREATE TRIGGER bi_bnd_detail BEFORE INSERT ON boundary_detail FOR EACH ROW EXECUTE PROCEDURE fn_bi_bnd_detail();


-- ----------------------------------------------------------------------------
-- REP_BOUNDARY table, function and trigger
-- ----------------------------------------------------------------------------
CREATE TABLE rep_boundary (
    boundary_id    INT4           NOT NULL,
    rep_id         INT4           NOT NULL,
    tran_id        INT4           NOT NULL,
    delete_flag    BOOLEAN        DEFAULT FALSE,

    CONSTRAINT pk_rep_boundary PRIMARY KEY (boundary_id,rep_id,tran_id),
    CONSTRAINT fk_rb_tranid_to_tran FOREIGN KEY (tran_id) REFERENCES transaction(tran_id)
);

CREATE OR REPLACE FUNCTION fn_bi_rep_boundary() RETURNS TRIGGER AS
$BODY$
BEGIN
  -- SET SCHEMA 'sams_place';

  -- If no 'tran_id' specified use the latest transaction id, otherwise validate the tran_id/boundary_id/rep_id combination
    IF (NEW.tran_id IS NULL) THEN
      NEW.tran_id = MAX(tran_id) FROM transaction;
    ELSEIF NOT EXISTS (SELECT 1 FROM transaction WHERE transaction.tran_id = NEW.tran_id) THEN
      RAISE EXCEPTION 'Invalid tran_id specified.';
    ELSEIF EXISTS (SELECT 1 FROM rep_boundary WHERE boundary_id = NEW.boundary_id AND rep_id = NEW.rep_id AND tran_id >= NEW.tran_id) THEN
      RAISE EXCEPTION 'Invalid boundary_id/rep_id/tran_id combination specified.';
    END IF;

  -- 'boundary_id' must reference an existing boundary
  IF (NEW.boundary_id IS NOT NULL) THEN
    IF NOT EXISTS (SELECT 1 FROM boundary WHERE boundary_id = NEW.boundary_id) THEN
      RAISE EXCEPTION 'Invalid boundary_id specified -- no matching row found.';
    END IF;
  END IF;

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

CREATE TRIGGER bi_rep_boundary BEFORE INSERT ON rep_boundary FOR EACH ROW EXECUTE PROCEDURE fn_bi_rep_boundary();


-- ----------------------------------------------------------------------------
-- Create indexes
-- ----------------------------------------------------------------------------
CREATE INDEX idx_bnd_bndary_id ON boundary (boundary_id);
CREATE INDEX idx_bnd_dtl_bndary_id ON boundary_detail (boundary_id);
CREATE INDEX idx_rep_bnd_bndary_id ON rep_boundary (boundary_id);
CREATE INDEX idx_rep_bnd_rep_id ON rep_boundary (rep_id);


-- ----------------------------------------------------------------------------
-- Grant privileges to the "places" schema
-- ----------------------------------------------------------------------------

-- GRANT ALL PRIVILEGES ON SCHEMA sams_place TO sams_place;
-- GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA sams_place TO sams_place;
-- GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA sams_place TO sams_place;
