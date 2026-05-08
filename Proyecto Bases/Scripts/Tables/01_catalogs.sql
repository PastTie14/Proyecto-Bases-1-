-- ============================================
-- CURRENCY
-- ============================================
CREATE TABLE currency
(
    id_currency NUMBER(4),
    "name"      VARCHAR2(20),
    acronym     VARCHAR2(5),
    createdBy  VARCHAR2(20),
    createdAt  DATE,
    modifiedBy VARCHAR2(20),
    modifiedAt DATE
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
    
COMMENT ON TABLE currency 
IS 'Stores currency types for donations';

COMMENT ON COLUMN currency.id_currency 
IS 'Primary key, identifier for the currency';

COMMENT ON COLUMN currency."name" 
IS 'Full name of the currency';

COMMENT ON COLUMN currency.acronym 
IS 'Standard 3 letter acronym for the currency (USD, EUR, CRC)';

COMMENT ON COLUMN currency.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN currency.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN currency.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN currency.ModifiedAt
IS 'The date the table was modified';

-- ============================================
-- PROVINCE
-- ============================================
CREATE TABLE province
(
    id_province NUMBER(4),
    "name"      VARCHAR2(50),
    createdBy  VARCHAR2(20),
    createdAt  DATE,
    modifiedBy VARCHAR2(20),
    modifiedAt DATE
)
TABLESPACE TS_DATA;

ALTER TABLE province 
    MODIFY id_province CONSTRAINT province_idProvince_nn NOT NULL;
    
ALTER TABLE province 
    MODIFY "name" CONSTRAINT province_name_nn NOT NULL;

ALTER TABLE province
    ADD CONSTRAINT pk_province PRIMARY KEY (id_province)
    USING INDEX TABLESPACE TS_INDEX;

COMMENT ON TABLE province 
IS 'Stores the province';

COMMENT ON COLUMN province.id_province 
IS 'Primary key, identifier for the province';

COMMENT ON COLUMN province."name" 
IS 'Official name of the province';

COMMENT ON COLUMN province.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN province.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN province.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN province.ModifiedAt
IS 'The date the table was modified';

-- ============================================
-- CANTON
-- ============================================
CREATE TABLE canton
(
    id_canton   NUMBER(4),
    "name"      VARCHAR2(50),
    id_province NUMBER(4),
    createdBy  VARCHAR2(20),
    createdAt  DATE,
    modifiedBy VARCHAR2(20),
    modifiedAt DATE
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

COMMENT ON TABLE canton 
IS 'Stores the canton. Each province contains multiple cantons';

COMMENT ON COLUMN canton.id_canton 
IS 'Primary key, identifier for the canton';

COMMENT ON COLUMN canton."name" 
IS 'Official name of the canton';

COMMENT ON COLUMN canton.id_province 
IS 'Foreign key, references the province that contains this canton';

COMMENT ON COLUMN canton.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN canton.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN canton.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN canton.ModifiedAt
IS 'The date the table was modified';

-- ============================================
-- DISTRICT
-- ============================================
CREATE TABLE district
(
    id_district NUMBER(4),
    "name"      VARCHAR2(50),
    id_canton   NUMBER(4),
    createdBy  VARCHAR2(20),
    createdAt  DATE,
    modifiedBy VARCHAR2(20),
    modifiedAt DATE
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

COMMENT ON TABLE district 
IS 'Stores districts. Each canton has multiple districts';

COMMENT ON COLUMN district.id_district 
IS 'Primary key, identifier for the district';

COMMENT ON COLUMN district."name" 
IS 'Official name of the district';

COMMENT ON COLUMN district.id_canton 
IS 'Foreign key, references the canton that contains this district';

COMMENT ON COLUMN district.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN district.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN district.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN district.ModifiedAt
IS 'The date the table was modified';

-- ============================================
-- PET TYPE
-- ============================================
CREATE TABLE pet_type
(
    id_pet_type NUMBER(4),
    "name"      VARCHAR2(50),
    createdBy  VARCHAR2(20),
    createdAt  DATE,
    modifiedBy VARCHAR2(20),
    modifiedAt DATE
)
TABLESPACE TS_DATA;

ALTER TABLE pet_type 
    MODIFY id_pet_type CONSTRAINT petType_idPetType_nn NOT NULL;

ALTER TABLE pet_type 
    MODIFY "name" CONSTRAINT petType_name_nn NOT NULL;

ALTER TABLE pet_type
    ADD CONSTRAINT pk_pet_type PRIMARY KEY (id_pet_type)
    USING INDEX TABLESPACE TS_INDEX;

COMMENT ON TABLE pet_type 
IS 'Stores species of pets';

COMMENT ON COLUMN pet_type.id_pet_type 
IS 'Primary key, identifier for the pet type';

COMMENT ON COLUMN pet_type."name" 
IS 'Name of the pet category';

COMMENT ON COLUMN pet_type.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN pet_type.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN pet_type.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN pet_type.ModifiedAt
IS 'The date the table was modified';

-- ============================================
-- RACE
-- ============================================
CREATE TABLE race
(
    id_race     NUMBER(4),
    "name"      VARCHAR2(50),
    id_pet_type NUMBER(4),
    createdBy  VARCHAR2(20),
    createdAt  DATE,
    modifiedBy VARCHAR2(20),
    modifiedAt DATE
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

COMMENT ON TABLE race 
IS 'Specific breed within a pet type';

COMMENT ON COLUMN race.id_race 
IS 'Primary key, identifier for the breed';

COMMENT ON COLUMN race."name" 
IS 'Name of the breed';

COMMENT ON COLUMN race.id_pet_type 
IS 'Foreign key, references the pet type this breed belongs to';

COMMENT ON COLUMN race.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN race.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN race.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN race.ModifiedAt
IS 'The date the table was modified';

-- ============================================
-- STATUS
-- ============================================
CREATE TABLE status
(
    id_status   NUMBER(4),
    status_type VARCHAR2(30),
    createdBy  VARCHAR2(20),
    createdAt  DATE,
    modifiedBy VARCHAR2(20),
    modifiedAt DATE
)
TABLESPACE TS_DATA;

ALTER TABLE status 
    MODIFY id_status CONSTRAINT status_idStatus_nn NOT NULL;
    
ALTER TABLE status 
    MODIFY status_type CONSTRAINT status_statusType_nn NOT NULL;

ALTER TABLE status
    ADD CONSTRAINT pk_status PRIMARY KEY (id_status)
    USING INDEX TABLESPACE TS_INDEX;

COMMENT ON TABLE status 
IS 'Stores statuses for pets';

COMMENT ON COLUMN status.id_status 
IS 'Primary key, identifier for the status';

COMMENT ON COLUMN status.status_type 
IS 'Name of the status (Adopted, Not adopted, Lost, Found)';

COMMENT ON COLUMN status.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN status.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN status.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN status.ModifiedAt
IS 'The date the table was modified';

-- ============================================
-- COLOR
-- ============================================
CREATE TABLE color
(
    id_color NUMBER(4),
    "name"   VARCHAR2(30),
    createdBy  VARCHAR2(20),
    createdAt  DATE,
    modifiedBy VARCHAR2(20),
    modifiedAt DATE
)
TABLESPACE TS_DATA;

ALTER TABLE color 
    MODIFY id_color CONSTRAINT color_idColor_nn NOT NULL;

ALTER TABLE color 
    MODIFY "name" CONSTRAINT color_name_nn NOT NULL;

ALTER TABLE color
    ADD CONSTRAINT pk_color PRIMARY KEY (id_color)
    USING INDEX TABLESPACE TS_INDEX;

COMMENT ON TABLE color 
IS 'Stores colors for pets';

COMMENT ON COLUMN color.id_color 
IS 'Primary key, identifier for the color';

COMMENT ON COLUMN color."name" 
IS 'Name of the color';

COMMENT ON COLUMN color.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN color.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN color.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN color.ModifiedAt
IS 'The date the table was modified';

-- ============================================
-- VALUE TYPE
-- ============================================
CREATE TABLE value_type
(
    id_value_type NUMBER(4),
    "type"        VARCHAR2(30),
    createdBy  VARCHAR2(20),
    createdAt  DATE,
    modifiedBy VARCHAR2(20),
    modifiedAt DATE
)
TABLESPACE TS_DATA;

ALTER TABLE value_type 
    MODIFY id_value_type CONSTRAINT valueType_idValueType_nn NOT NULL;

ALTER TABLE value_type 
    MODIFY "type" CONSTRAINT valueType_type_nn NOT NULL;

ALTER TABLE value_type
    ADD CONSTRAINT pk_value_type PRIMARY KEY (id_value_type)
    USING INDEX TABLESPACE TS_INDEX;
    
COMMENT ON TABLE value_type 
IS 'Stores types of values for parameters';

COMMENT ON COLUMN value_type.id_value_type 
IS 'Primary key, identifier for the value type';

COMMENT ON COLUMN value_type."type" 
IS 'Name of the value type';

COMMENT ON COLUMN value_type.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN value_type.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN value_type.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN value_type.ModifiedAt
IS 'The date the table was modified';

-- ============================================
-- SIZE
-- ============================================
CREATE TABLE "size"
(
    id_size     NUMBER(1),
    "name"      VARCHAR2(20),
    createdBy  VARCHAR2(20),
    createdAt  DATE,
    modifiedBy VARCHAR2(20),
    modifiedAt DATE
)
TABLESPACE TS_DATA;

ALTER TABLE "size"
    MODIFY id_size CONSTRAINT size_idSize_nn NOT NULL;
    
ALTER TABLE "size"
    ADD CONSTRAINT pk_size PRIMARY KEY (id_size)
    USING INDEX TABLESPACE TS_INDEX;
    
COMMENT ON TABLE "size" 
IS 'Stores pet sizes';

COMMENT ON COLUMN "size".id_size 
IS 'Primary key, identifier for pet size';

COMMENT ON COLUMN "size"."name" 
IS 'Name of the size';

COMMENT ON COLUMN "size".CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN "size".CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN "size".ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN "size".ModifiedAt
IS 'The date the table was modified';

-- ============================================
-- SIZE_X_CRIB_HOUSE
-- ============================================
CREATE TABLE size_x_crib_house
(
    id_size         NUMBER(1),
    id_crib_house   NUMBER(8),
    createdBy       VARCHAR2(20),
    createdAt       DATE,
    modifiedBy      VARCHAR2(20),
    modifiedAt      DATE
)
TABLESPACE TS_DATA;

ALTER TABLE size_x_crib_house
    ADD CONSTRAINT pk_size_x_crib_house PRIMARY KEY (id_size, id_crib_house)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE size_x_crib_house
    ADD CONSTRAINT fk_sxch_size
    FOREIGN KEY (id_size) REFERENCES "size" (id_size)
    ON DELETE CASCADE;

ALTER TABLE size_x_crib_house
    ADD CONSTRAINT fk_sxch_crib_house
    FOREIGN KEY (id_crib_house) REFERENCES crib_house (id_user)
    ON DELETE CASCADE;

COMMENT ON TABLE size_x_crib_house 
IS 'Intermediate table, N to N relationship between size and crib_house';

COMMENT ON COLUMN size_x_crib_house.id_size 
IS 'Composite primary key. Foreign key references the size id';

COMMENT ON COLUMN size_x_crib_house.id_crib_house 
IS 'Composite primary key. Foreign key references the crib house id';

COMMENT ON COLUMN size_x_crib_house.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN size_x_crib_house.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN size_x_crib_house.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN size_x_crib_house.ModifiedAt
IS 'The date the table was modified';