ALTER TABLE attribute ADD COLUMN frequency bigint NULL;
ALTER TABLE attribute ADD COLUMN weight int2 NULL;
ALTER TABLE attribute ADD CONSTRAINT weight_range_check CHECK (weight >= 0 and weight <= 100);
CREATE INDEX idx_attr_male ON attribute USING btree (is_male);
CREATE INDEX idx_attr_female ON attribute USING btree (is_female);
CREATE INDEX idx_attr_freq ON attribute USING btree (frequency);
CREATE INDEX idx_attr_weight ON attribute USING BTREE (weight);