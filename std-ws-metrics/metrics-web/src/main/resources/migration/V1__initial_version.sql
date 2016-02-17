-- ----------------------------------------------------------------------------
-- DDL USING SYNTAX FOR POSTGRES - USE DEFAULT TABLESPACE FOR EVERYTHING ...
-- ----------------------------------------------------------------------------


-- ----------------------------------------------------------------------------
-- TYPE table, and implied sequence
-- ----------------------------------------------------------------------------
CREATE TABLE type (
    type_id         SERIAL       NOT NULL  UNIQUE,
    name            VARCHAR(64)  NOT NULL  UNIQUE,
    description     VARCHAR(64)  NOT NULL,

    CONSTRAINT pk_type PRIMARY KEY (type_id)
);


-- ----------------------------------------------------------------------------
-- NAME table, and implied sequence
-- ----------------------------------------------------------------------------
CREATE TABLE name (
    name_id         SERIAL       NOT NULL  UNIQUE,
    text            VARCHAR(64)  NOT NULL,
    normalized_text VARCHAR(64)  NOT NULL  UNIQUE,

    CONSTRAINT pk_name PRIMARY KEY (name_id)
);


-- ----------------------------------------------------------------------------
-- ATTRIBUTE table, and implied sequence
-- ----------------------------------------------------------------------------
CREATE TABLE attribute (
    attr_id         SERIAL       NOT NULL UNIQUE,
    name_id         INTEGER      NOT NULL,
    type_id         INTEGER,
    locale_lang     CHAR(3),
    locale_script   CHAR(4),
    rep_group_id    INTEGER,
    is_male         BOOLEAN      NOT NULL DEFAULT FALSE,
    is_female       BOOLEAN      NOT NULL DEFAULT FALSE,
    from_yr         INTEGER,
    to_yr           INTEGER,

    CONSTRAINT pk_attribute PRIMARY KEY (attr_id),
    CONSTRAINT fk_name_id_to_name FOREIGN KEY (name_id) REFERENCES name(name_id),
    CONSTRAINT fk_type_id_to_type FOREIGN KEY (type_id) REFERENCES type(type_id)
);


-- ----------------------------------------------------------------------------
-- Define Indexes
-- ----------------------------------------------------------------------------
CREATE INDEX idx_name_norm_text ON name (normalized_text);

CREATE INDEX idx_attr_type_id ON attribute (type_id);
CREATE INDEX idx_attr_name_id ON attribute (name_id);
CREATE INDEX idx_attr_lang ON attribute (locale_lang);
CREATE INDEX idx_attr_script ON attribute (locale_script);
CREATE INDEX idx_attr_rep_group_id ON attribute (rep_group_id);
CREATE INDEX idx_attr_from_yr ON attribute (from_yr);
CREATE INDEX idx_attr_to_yr ON attribute (to_yr);
