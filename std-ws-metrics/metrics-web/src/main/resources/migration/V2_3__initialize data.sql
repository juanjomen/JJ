TRUNCATE name CASCADE;
ALTER SEQUENCE name_name_id_seq START WITH 1 RESTART WITH 1;
TRUNCATE attribute CASCADE;
ALTER SEQUENCE attribute_attr_id_seq START WITH 1 RESTART WITH 1;
TRUNCATE type CASCADE;
ALTER SEQUENCE type_type_id_seq START WITH 1 RESTART WITH 1;
INSERT INTO type (name, description) VALUES ('given', 'Name given at birth, not a family name');
INSERT INTO type (name, description) VALUES ('surname', 'Family name');