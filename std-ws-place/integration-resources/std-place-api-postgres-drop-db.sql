-- ----------------------------------------------------------------------------
-- Set the schema which is to be used
-- ----------------------------------------------------------------------------
SET SCHEMA 'sams_place';

-- ----------------------------------------------------------------------------
-- Drop functions, tables, sequences.  The triggers, indexes and other
-- database artifacts will follow.
-- ----------------------------------------------------------------------------
DROP FUNCTION IF EXISTS fn_bi_rep_disp_name() CASCADE;
DROP FUNCTION IF EXISTS fn_bi_place() CASCADE;
DROP FUNCTION IF EXISTS fn_bi_rep_attr() CASCADE;
DROP FUNCTION IF EXISTS fn_bi_rep_bndry() CASCADE;
DROP FUNCTION IF EXISTS fn_bi_place_name() CASCADE;
DROP FUNCTION IF EXISTS fn_bi_source_link() CASCADE;
DROP FUNCTION IF EXISTS fn_bi_citation() CASCADE;
DROP FUNCTION IF EXISTS fn_bi_source() CASCADE;
DROP FUNCTION IF EXISTS fn_bi_plc_version() CASCADE;
DROP FUNCTION IF EXISTS fn_bi_place_rep() CASCADE;
DROP FUNCTION IF EXISTS fn_bi_place_type_group() CASCADE;
DROP FUNCTION IF EXISTS fn_bi_place_type_group_member() CASCADE;
DROP FUNCTION IF EXISTS fn_bi_place_type_sub_group() CASCADE;

TRUNCATE TABLE schema_version CASCADE;
TRUNCATE TABLE place_type_sub_group CASCADE;
TRUNCATE TABLE place_type_group_member CASCADE;
TRUNCATE TABLE place_type_group CASCADE;
TRUNCATE TABLE source CASCADE;
TRUNCATE TABLE citation CASCADE;
TRUNCATE TABLE place_version CASCADE;
TRUNCATE TABLE rep_bndry CASCADE;
TRUNCATE TABLE rep_attr CASCADE;
TRUNCATE TABLE rep_display_name CASCADE;
TRUNCATE TABLE place_rep CASCADE;
TRUNCATE TABLE place_name CASCADE;
TRUNCATE TABLE place CASCADE;
TRUNCATE TABLE type_term CASCADE;
TRUNCATE TABLE type CASCADE;
TRUNCATE TABLE transaction CASCADE;

DROP TABLE IF EXISTS schema_version CASCADE;
DROP TABLE IF EXISTS place_type_sub_group CASCADE;
DROP TABLE IF EXISTS place_type_group_member CASCADE;
DROP TABLE IF EXISTS place_type_group CASCADE;
DROP TABLE IF EXISTS citation CASCADE;
DROP TABLE IF EXISTS source CASCADE;
DROP TABLE IF EXISTS source CASCADE;
DROP TABLE IF EXISTS citation CASCADE;
DROP TABLE IF EXISTS place_version CASCADE;
DROP TABLE IF EXISTS rep_bndry CASCADE;
DROP TABLE IF EXISTS rep_attr CASCADE;
DROP TABLE IF EXISTS rep_display_name CASCADE;
DROP TABLE IF EXISTS place_rep CASCADE;
DROP TABLE IF EXISTS place_name CASCADE;
DROP TABLE IF EXISTS place CASCADE;
DROP TABLE IF EXISTS type_term CASCADE;
DROP TABLE IF EXISTS type CASCADE;
DROP TABLE IF EXISTS transaction CASCADE;

DROP SEQUENCE IF EXISTS place_version_version_id_seq;
DROP SEQUENCE IF EXISTS seq_place;
DROP SEQUENCE IF EXISTS seq_place_name;
DROP SEQUENCE IF EXISTS seq_place_rep;
DROP SEQUENCE IF EXISTS seq_rep_attr;
DROP SEQUENCE IF EXISTS seq_rep_bndry;
DROP SEQUENCE IF EXISTS seq_source;
DROP SEQUENCE IF EXISTS seq_citation;
DROP SEQUENCE IF EXISTS seq_plc_grp;
DROP SEQUENCE IF EXISTS seq_citation;
DROP SEQUENCE IF EXISTS source_source_id_seq;
DROP SEQUENCE IF EXISTS transaction_tran_id_seq;
DROP SEQUENCE IF EXISTS type_term_term_id_seq;
DROP SEQUENCE IF EXISTS type_type_id_seq;
