TRUNCATE TABLE name CASCADE;
ALTER TABLE name DROP CONSTRAINT name_normalized_text_key;
ALTER TABLE name ADD CONSTRAINT name_text_key UNIQUE (text);