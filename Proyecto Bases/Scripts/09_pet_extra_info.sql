-- ============================================================
-- FILE 09 - PET EXTRA INFO & RELATED
-- Tables: pet_extra_info, bounty, current_status,
--         energy_level, training_ease
-- Depends on: pet (06), currency (01)
-- NOTE: No NOT NULL constraints on this group of tables
-- ============================================================

-- ============================================
-- PET EXTRA INFO
-- ============================================
CREATE TABLE pet_extra_info
(
    id_pet_extra_info   NUMBER(8),
    "size"              VARCHAR2(20),
    before_picture      VARCHAR2(200),
    after_picture       VARCHAR2(200),
    id_pet              NUMBER(8)
)
TABLESPACE TS_DATA;

ALTER TABLE pet_extra_info
    ADD CONSTRAINT pk_pet_extra_info PRIMARY KEY (id_pet_extra_info)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE pet_extra_info
    ADD CONSTRAINT fk_pei_pet
    FOREIGN KEY (id_pet) REFERENCES pet (id_pet);

-- ============================================
-- BOUNTY
-- ============================================
CREATE TABLE bounty
(
    id_bounty         NUMBER(8),
    amount            NUMBER(12,2),
    id_pet_extra_info NUMBER(8),
    id_currency       NUMBER(4)
)
TABLESPACE TS_DATA;

ALTER TABLE bounty
    ADD CONSTRAINT pk_bounty PRIMARY KEY (id_bounty)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE bounty
    ADD CONSTRAINT fk_bounty_pet_extra_info
    FOREIGN KEY (id_pet_extra_info) REFERENCES pet_extra_info (id_pet_extra_info);

ALTER TABLE bounty
    ADD CONSTRAINT fk_bounty_currency
    FOREIGN KEY (id_currency) REFERENCES currency (id_currency);

-- ============================================
-- CURRENT STATUS
-- ============================================
CREATE TABLE current_status
(
    id_current_status NUMBER(8),
    status_type       VARCHAR2(30),
    id_pet_extra_info NUMBER(8)
)
TABLESPACE TS_DATA;

ALTER TABLE current_status
    ADD CONSTRAINT pk_current_status PRIMARY KEY (id_current_status)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE current_status
    ADD CONSTRAINT fk_cs_pet_extra_info
    FOREIGN KEY (id_pet_extra_info) REFERENCES pet_extra_info (id_pet_extra_info);

-- ============================================
-- ENERGY LEVEL
-- ============================================
CREATE TABLE energy_level
(
    id_energy_level   NUMBER(8),
    "name"            VARCHAR2(50),
    id_pet_extra_info NUMBER(8)
)
TABLESPACE TS_DATA;

ALTER TABLE energy_level
    ADD CONSTRAINT pk_energy_level PRIMARY KEY (id_energy_level)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE energy_level
    ADD CONSTRAINT fk_el_pet_extra_info
    FOREIGN KEY (id_pet_extra_info) REFERENCES pet_extra_info (id_pet_extra_info);

-- ============================================
-- TRAINING EASE
-- ============================================
CREATE TABLE training_ease
(
    id_training_ease  NUMBER(8),
    "name"            VARCHAR2(50),
    id_pet_extra_info NUMBER(8)
)
TABLESPACE TS_DATA;

ALTER TABLE training_ease
    ADD CONSTRAINT pk_training_ease PRIMARY KEY (id_training_ease)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE training_ease
    ADD CONSTRAINT fk_te_pet_extra_info
    FOREIGN KEY (id_pet_extra_info) REFERENCES pet_extra_info (id_pet_extra_info);
