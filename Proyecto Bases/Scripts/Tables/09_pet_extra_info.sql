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
    status_type       VARCHAR2(30),
    createdBy  VARCHAR2(20),
    createdAt  DATE,
    modifiedBy VARCHAR2(20),
    modifiedAt DATE
)
TABLESPACE TS_DATA;

ALTER TABLE current_status
    ADD CONSTRAINT pk_current_status PRIMARY KEY (id_current_status)
    USING INDEX TABLESPACE TS_INDEX;

COMMENT ON TABLE current_status
IS 'Stores information about health statuses for pets';

COMMENT ON COLUMN current_status.id_current_status
IS 'Primary key, identifier for the current health status';

COMMENT ON COLUMN current_status.status_type
IS 'The name of the status';

COMMENT ON COLUMN current_status.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN current_status.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN current_status.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN current_status.ModifiedAt
IS 'The date the table was modified';

-- ============================================
-- ENERGY LEVEL
-- ============================================
CREATE TABLE energy_level
(
    id_energy_level   NUMBER(8),
    "name"            VARCHAR2(50),
    createdBy  VARCHAR2(20),
    createdAt  DATE,
    modifiedBy VARCHAR2(20),
    modifiedAt DATE
)
TABLESPACE TS_DATA;

ALTER TABLE energy_level
    ADD CONSTRAINT pk_energy_level PRIMARY KEY (id_energy_level)
    USING INDEX TABLESPACE TS_INDEX;

COMMENT ON TABLE energy_level
IS 'Stores information the energy levels of pets';

COMMENT ON COLUMN energy_level.id_energy_level
IS 'Primary key, identifier for the energy level';

COMMENT ON COLUMN energy_level."name"
IS 'The name of the energy level';

COMMENT ON COLUMN energy_level.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN energy_level.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN energy_level.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN energy_level.ModifiedAt
IS 'The date the table was modified';

-- ============================================
-- TRAINING EASE
-- ============================================
CREATE TABLE training_ease
(
    id_training_ease  NUMBER(8),
    "name"            VARCHAR2(50),
    createdBy  VARCHAR2(20),
    createdAt  DATE,
    modifiedBy VARCHAR2(20),
    modifiedAt DATE
)
TABLESPACE TS_DATA;

ALTER TABLE training_ease
    ADD CONSTRAINT pk_training_ease PRIMARY KEY (id_training_ease)
    USING INDEX TABLESPACE TS_INDEX;

COMMENT ON TABLE training_ease
IS 'Stores information about how easy it is to train a pet';

COMMENT ON COLUMN training_ease.id_training_ease
IS 'Primary key, identifier for the training ease';

COMMENT ON COLUMN training_ease."name"
IS 'The name of the training ease';

COMMENT ON COLUMN training_ease.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN training_ease.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN training_ease.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN training_ease.ModifiedAt
IS 'The date the table was modified';

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
    id_training_ease    NUMBER(8),
    createdBy  VARCHAR2(20),
    createdAt  DATE,
    modifiedBy VARCHAR2(20),
    modifiedAt DATE
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

COMMENT ON TABLE pet_extra_info
IS 'Stores all the extra information of the pet';

COMMENT ON COLUMN pet_extra_info.id_pet_extra_info
IS 'Primary key, identifier for the pet extra info';

COMMENT ON COLUMN pet_extra_info.before_picture
IS 'Path to the picture before the pet was rescued';

COMMENT ON COLUMN pet_extra_info.after_picture
IS 'Path to the picture after the pet was rescued';

COMMENT ON COLUMN pet_extra_info.id_pet
IS 'Foreign key, references the associated pet';

COMMENT ON COLUMN pet_extra_info.id_current_status
IS 'Foreign key, references the health status of the pet';

COMMENT ON COLUMN pet_extra_info.id_energy_level
IS 'Foreign key, references the energy level of the pet';

COMMENT ON COLUMN pet_extra_info.id_training_ease
IS 'Foreign key, references the training ease of the pet';

COMMENT ON COLUMN pet_extra_info.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN pet_extra_info.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN pet_extra_info.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN pet_extra_info.ModifiedAt
IS 'The date the table was modified';

-- ============================================
-- BOUNTY
-- ============================================
CREATE TABLE bounty
(
    id_bounty         NUMBER(8),
    amount            NUMBER(12,2),
    id_pet_extra_info NUMBER(8),
    id_currency       NUMBER(4),
    createdBy  VARCHAR2(20),
    createdAt  DATE,
    modifiedBy VARCHAR2(20),
    modifiedAt DATE
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
    
COMMENT ON TABLE bounty
IS 'Stores information about a bounty put on a lost pet';

COMMENT ON COLUMN bounty.id_bounty
IS 'Primary key, identifier for the bounty';

COMMENT ON COLUMN bounty.amount
IS 'The amount of the bounty';

COMMENT ON COLUMN bounty.id_pet_extra_info
IS 'Foreign key, references the associated pet_extra_info';

COMMENT ON COLUMN bounty.id_currency
IS 'Foreign key, references the currency of the bounty';

COMMENT ON COLUMN bounty.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN bounty.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN bounty.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN bounty.ModifiedAt
IS 'The date the table was modified';
