ALTER TABLE attribute DROP COLUMN locale;

ALTER TABLE name DROP COLUMN normalized_text;

ALTER TABLE name ADD COLUMN locale character varying(10) NOT NULL DEFAULT('EN');