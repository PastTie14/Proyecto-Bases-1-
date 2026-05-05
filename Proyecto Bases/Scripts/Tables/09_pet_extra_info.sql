-- ============================================================
-- FILE 09 - PET EXTRA INFO & RELATED
-- Tables: current_status, energy_level, training_ease,
--         pet_extra_info, bounty
-- Depends on: pet (06), currency (01)
-- ============================================================

-- ============================================
-- CURRENT STATUS
-- ============================================
CREATE TABLE current_status
(
    id_current_status NUMBER(8),
    status_type       VARCHAR2(30)
)
TABLESPACE TS_DATA;

ALTER TABLE current_status
    ADD CONSTRAINT pk_current_status PRIMARY KEY (id_current_status)
    USING INDEX TABLESPACE TS_INDEX;

-- ============================================
-- ENERGY LEVEL
-- ============================================
CREATE TABLE energy_level
(
    id_energy_level   NUMBER(8),
    "name"            VARCHAR2(50)
)
TABLESPACE TS_DATA;

ALTER TABLE energy_level
    ADD CONSTRAINT pk_energy_level PRIMARY KEY (id_energy_level)
    USING INDEX TABLESPACE TS_INDEX;

-- ============================================
-- TRAINING EASE
-- ============================================
CREATE TABLE training_ease
(
    id_training_ease  NUMBER(8),
    "name"            VARCHAR2(50)
)
TABLESPACE TS_DATA;

ALTER TABLE training_ease
    ADD CONSTRAINT pk_training_ease PRIMARY KEY (id_training_ease)
    USING INDEX TABLESPACE TS_INDEX;

-- ============================================
-- PET EXTRA INFO
-- ============================================
CREATE TABLE pet_extra_info
(
    id_pet_extra_info   NUMBER(8),
    before_picture      VARCHAR2(200),
    after_picture       VARCHAR2(200),
    id_pet              NUMBER(8),
    id_current_status   NUMBER(8),
    id_energy_level     NUMBER(8),
    id_training_ease    NUMBER(8)
)
TABLESPACE TS_DATA;

ALTER TABLE pet_extra_info
    ADD CONSTRAINT pk_pet_extra_info PRIMARY KEY (id_pet_extra_info)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE pet_extra_info
    ADD CONSTRAINT fk_pei_pet
    FOREIGN KEY (id_pet) REFERENCES pet (id_pet)
    ON DELETE CASCADE;
    
ALTER TABLE pet_extra_info
    ADD CONSTRAINT fk_pei_currentStatus
    FOREIGN KEY (id_current_status) REFERENCES current_status (id_current_status);
    
ALTER TABLE pet_extra_info
    ADD CONSTRAINT fk_pei_energyLevel
    FOREIGN KEY (id_energy_level) REFERENCES energy_level (id_energy_level);
    
ALTER TABLE pet_extra_info
    ADD CONSTRAINT fk_pei_trainingEase
    FOREIGN KEY (id_training_ease) REFERENCES training_ease (id_training_ease);

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
    FOREIGN KEY (id_pet_extra_info) REFERENCES pet_extra_info (id_pet_extra_info)
    ON DELETE CASCADE;

ALTER TABLE bounty
    ADD CONSTRAINT fk_bounty_currency
    FOREIGN KEY (id_currency) REFERENCES currency (id_currency)
    ON DELETE CASCADE;
