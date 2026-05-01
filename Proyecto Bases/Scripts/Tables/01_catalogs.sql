-- ============================================================
-- FILE 01 - CATALOGS
-- Tables: currency, province, canton, district,
--         pet_type, race, status, color, value_type
-- ============================================================

-- ============================================
-- CURRENCY
-- ============================================
CREATE TABLE currency
(
    id_currency NUMBER(4),
    "name"      VARCHAR2(20),
    acronym     VARCHAR2(5)
)
TABLESPACE TS_DATA;

ALTER TABLE currency 
    MODIFY id_currency CONSTRAINT currency_idCurrency_nn NOT NULL;

ALTER TABLE currency 
    MODIFY "name" CONSTRAINT currency_name_nn NOT NULL;

ALTER TABLE currency 
    MODIFY acronym CONSTRAINT currency_acronym_nn NOT NULL;

ALTER TABLE currency
    ADD CONSTRAINT pk_currency PRIMARY KEY (id_currency)
    USING INDEX TABLESPACE TS_INDEX;

-- ============================================
-- PROVINCE
-- ============================================
CREATE TABLE province
(
    id_province NUMBER(4),
    "name"      VARCHAR2(50)
)
TABLESPACE TS_DATA;

ALTER TABLE province 
    MODIFY id_province CONSTRAINT province_idProvince_nn NOT NULL;
    
ALTER TABLE province 
    MODIFY "name" CONSTRAINT province_name_nn NOT NULL;

ALTER TABLE province
    ADD CONSTRAINT pk_province PRIMARY KEY (id_province)
    USING INDEX TABLESPACE TS_INDEX;

-- ============================================
-- CANTON
-- ============================================
CREATE TABLE canton
(
    id_canton   NUMBER(4),
    "name"      VARCHAR2(50),
    id_province NUMBER(4)
)
TABLESPACE TS_DATA;

ALTER TABLE canton 
    MODIFY id_canton CONSTRAINT canton_idCanton_nn NOT NULL;
    
ALTER TABLE canton 
    MODIFY "name" CONSTRAINT canton_name_nn NOT NULL;
    
ALTER TABLE canton 
    MODIFY id_province CONSTRAINT canton_idProvince_nn NOT NULL;

ALTER TABLE canton
    ADD CONSTRAINT pk_canton PRIMARY KEY (id_canton)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE canton
    ADD CONSTRAINT fk_canton_province
    FOREIGN KEY (id_province) REFERENCES province (id_province)
    ON DELETE CASCADE;

-- ============================================
-- DISTRICT
-- ============================================
CREATE TABLE district
(
    id_district NUMBER(4),
    "name"      VARCHAR2(50),
    id_canton   NUMBER(4)
)
TABLESPACE TS_DATA;

ALTER TABLE district
    MODIFY id_district CONSTRAINT district_idDistrict_nn NOT NULL;

ALTER TABLE district
    MODIFY "name" CONSTRAINT district_name_nn NOT NULL;

ALTER TABLE district
    MODIFY id_canton CONSTRAINT district_idCanton_nn NOT NULL;

ALTER TABLE district
    ADD CONSTRAINT pk_district PRIMARY KEY (id_district)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE district
    ADD CONSTRAINT fk_district_canton
    FOREIGN KEY (id_canton) REFERENCES canton (id_canton)
    ON DELETE CASCADE;

-- ============================================
-- PET TYPE
-- ============================================
CREATE TABLE pet_type
(
    id_pet_type NUMBER(4),
    "name"      VARCHAR2(50)
)
TABLESPACE TS_DATA;

ALTER TABLE pet_type 
    MODIFY id_pet_type CONSTRAINT petType_idPetType_nn NOT NULL;

ALTER TABLE pet_type 
    MODIFY "name" CONSTRAINT petType_name_nn NOT NULL;

ALTER TABLE pet_type
    ADD CONSTRAINT pk_pet_type PRIMARY KEY (id_pet_type)
    USING INDEX TABLESPACE TS_INDEX;

-- ============================================
-- RACE
-- ============================================
CREATE TABLE race
(
    id_race     NUMBER(4),
    "name"      VARCHAR2(50),
    id_pet_type NUMBER(4)
)
TABLESPACE TS_DATA;

ALTER TABLE race 
    MODIFY id_race CONSTRAINT race_idRace_nn NOT NULL;
    
ALTER TABLE race 
    MODIFY "name" CONSTRAINT race_name_nn NOT NULL;
    
ALTER TABLE race 
    MODIFY id_pet_type CONSTRAINT race_idPetType_nn NOT NULL;

ALTER TABLE race
    ADD CONSTRAINT pk_race PRIMARY KEY (id_race)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE race
    ADD CONSTRAINT fk_race_pet_type
    FOREIGN KEY (id_pet_type) REFERENCES pet_type (id_pet_type)
    ON DELETE CASCADE;

-- ============================================
-- STATUS
-- ============================================
CREATE TABLE status
(
    id_status   NUMBER(4),
    status_type VARCHAR2(30)
)
TABLESPACE TS_DATA;

ALTER TABLE status 
    MODIFY id_status CONSTRAINT status_idStatus_nn NOT NULL;
    
ALTER TABLE status 
    MODIFY status_type CONSTRAINT status_statusType_nn NOT NULL;

ALTER TABLE status
    ADD CONSTRAINT pk_status PRIMARY KEY (id_status)
    USING INDEX TABLESPACE TS_INDEX;

-- ============================================
-- COLOR
-- ============================================
CREATE TABLE color
(
    id_color NUMBER(4),
    "name"   VARCHAR2(30)
)
TABLESPACE TS_DATA;

ALTER TABLE color 
    MODIFY id_color CONSTRAINT color_idColor_nn NOT NULL;

ALTER TABLE color 
    MODIFY "name" CONSTRAINT color_name_nn NOT NULL;

ALTER TABLE color
    ADD CONSTRAINT pk_color PRIMARY KEY (id_color)
    USING INDEX TABLESPACE TS_INDEX;

-- ============================================
-- VALUE TYPE
-- ============================================
CREATE TABLE value_type
(
    id_value_type NUMBER(4),
    "type"        VARCHAR2(30)
)
TABLESPACE TS_DATA;

ALTER TABLE value_type 
    MODIFY id_value_type CONSTRAINT valueType_idValueType_nn NOT NULL;

ALTER TABLE value_type 
    MODIFY "type" CONSTRAINT valueType_type_nn NOT NULL;

ALTER TABLE value_type
    ADD CONSTRAINT pk_value_type PRIMARY KEY (id_value_type)
    USING INDEX TABLESPACE TS_INDEX;