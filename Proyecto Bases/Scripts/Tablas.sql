
-- ============================================
-- USER
-- ============================================
CREATE TABLE "user"
(
    id_user    NUMBER(8),
    email      VARCHAR2(50),
    "password" VARCHAR2(20),
    createdBy  VARCHAR2(20),
    createdAt  DATE,
    modifiedBy VARCHAR2(20),
    modifiedAt DATE
)
TABLESPACE TS_USER;
ALTER TABLE "user"
    ADD CONSTRAINT pk_user PRIMARY KEY (id_user)
    USING INDEX
    TABLESPACE TS_INDEX;
    
-- ============================================
-- CURRENCY
-- ============================================

CREATE TABLE currency
(
    id_currency NUMBER(4),
    "name"      VARCHAR2(20),
    acronym     VARCHAR2(5)
)
TABLESPACE TS_FINANCIAL;
ALTER TABLE currency
    ADD CONSTRAINT pk_currency PRIMARY KEY (id_currency)
    USING INDEX
    TABLESPACE TS_INDEX;

-- ============================================
-- PHONE NUMBER
-- ============================================

CREATE TABLE phone_number
(
    id_phone NUMBER(8),
    "number"   VARCHAR2(20),
    id_user  NUMBER(8),
    id_pet NUMBER(8),
    id_veterinarian NUMBER(8)
)
TABLESPACE TS_USER;

ALTER TABLE phone_number
    ADD CONSTRAINT pk_phone_number PRIMARY KEY (id_phone)
    USING INDEX
    TABLESPACE TS_INDEX;

ALTER TABLE phone_number
    ADD CONSTRAINT fk_phone_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user);

ALTER TABLE phone_number
    ADD CONSTRAINT fk_phone_pet
    FOREIGN KEY (id_pet) REFERENCES pet (id_pet);

ALTER TABLE phone_number
    ADD CONSTRAINT fk_phone_veterinarian
    FOREIGN KEY (id_veterinarian) REFERENCES veterinarian (id_veterinarian);

-- ============================================
-- BLACK LIST
-- ============================================

CREATE TABLE black_list
(
    id_report NUMBER(8),
    id_user   NUMBER(8)
)
TABLESPACE TS_USER;

ALTER TABLE black_list
    ADD CONSTRAINT pk_black_list PRIMARY KEY (id_report)
    USING INDEX
    TABLESPACE TS_INDEX;

ALTER TABLE black_list
    ADD CONSTRAINT fk_report_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user);

-- ============================================
-- USER X REPORT BLACK LIST
-- ============================================

CREATE TABLE user_x_black_list
(
    reason        VARCHAR2(200),
    id_user       NUMBER(8),
    id_report     NUMBER(8)
)
TABLESPACE TS_USER;

ALTER TABLE user_x_black_list
    ADD CONSTRAINT pk_user_x_report PRIMARY KEY (id_user, id_report)
    USING INDEX
    TABLESPACE TS_INDEX;

ALTER TABLE user_x_black_list
    ADD CONSTRAINT fk_user_x_report_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user);

ALTER TABLE user_x_black_list
    ADD CONSTRAINT fk_user_x_report
    FOREIGN KEY (id_report) REFERENCES black_list (id_report);

-- ============================================
-- DONATION
-- ============================================

CREATE TABLE donation
(
    id_donation    NUMBER(8),
    amount         NUMBER(12, 2),
    id_association NUMBER(8),
    id_currency    NUMBER(4)
)
TABLESPACE TS_FINANCIAL;

ALTER TABLE donation
    ADD CONSTRAINT pk_donation PRIMARY KEY (id_donation)
    USING INDEX
    TABLESPACE TS_INDEX;

ALTER TABLE donation
    ADD CONSTRAINT fk_donation_association
    FOREIGN KEY (id_association) REFERENCES "user" (id_user);

ALTER TABLE donation
    ADD CONSTRAINT fk_donation_currency
    FOREIGN KEY (id_currency) REFERENCES currency (id_currency);

-- ============================================
-- DONATION X USER
-- ============================================

CREATE TABLE donation_x_user
(
    id_user     NUMBER(8),
    id_donation NUMBER(8)
)
TABLESPACE TS_FINANCIAL;

ALTER TABLE donation_x_user
    ADD CONSTRAINT pk_donation_x_user PRIMARY KEY (id_user, id_donation)
    USING INDEX
    TABLESPACE TS_INDEX;

ALTER TABLE donation_x_user
    ADD CONSTRAINT fk_dxu_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user);

ALTER TABLE donation_x_user
    ADD CONSTRAINT fk_dxu_donation
    FOREIGN KEY (id_donation) REFERENCES donation (id_donation);

-- ============================================
-- ASSOCIATION
-- ============================================

CREATE TABLE association
(
    id_user NUMBER(8),
    "name"  VARCHAR2(100)
)
TABLESPACE TS_USER;

ALTER TABLE association
    ADD CONSTRAINT pk_association PRIMARY KEY (id_user)
    USING INDEX
    TABLESPACE TS_INDEX;

ALTER TABLE association
    ADD CONSTRAINT fk_association_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user);

-- ============================================
-- ADOPTER
-- ============================================

CREATE TABLE adopter
(
    id_user         NUMBER(8),
    first_name      VARCHAR2(50),
    second_name     VARCHAR2(50),
    first_surname   VARCHAR2(50),
    second_surname  VARCHAR2(50)
)
TABLESPACE TS_USER;

ALTER TABLE adopter
    ADD CONSTRAINT pk_adopter PRIMARY KEY (id_user)
    USING INDEX
    TABLESPACE TS_INDEX;

ALTER TABLE adopter
    ADD CONSTRAINT fk_adopter_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user);

-- ============================================
-- RESCUER
-- ============================================

CREATE TABLE rescuer
(
    id_user         NUMBER(8),
    first_name      VARCHAR2(50),
    second_name     VARCHAR2(50),
    first_surname   VARCHAR2(50),
    second_surname  VARCHAR2(50)
)
TABLESPACE TS_USER;

ALTER TABLE rescuer
    ADD CONSTRAINT pk_rescuer PRIMARY KEY (id_user)
    USING INDEX
    TABLESPACE TS_INDEX;

ALTER TABLE rescuer
    ADD CONSTRAINT fk_rescuer_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user);

-- ============================================
-- CRIB HOUSE
-- ============================================

CREATE TABLE crib_house
(
    id_user            NUMBER(8),
    "name"             VARCHAR2(100),
    requires_donations NUMBER(1) DEFAULT 0,
    accepted_size      NUMBER(4)
)
TABLESPACE TS_USER;

ALTER TABLE crib_house
    ADD CONSTRAINT pk_crib_house PRIMARY KEY (id_user)
    USING INDEX
    TABLESPACE TS_INDEX;

ALTER TABLE crib_house
    ADD CONSTRAINT fk_crib_house_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user);

ALTER TABLE crib_house
    ADD CONSTRAINT chk_requires_donations
    CHECK (requires_donations IN (0, 1));
   
-- ============================================
-- LOG
-- ============================================

CREATE TABLE "log"
(
    id_log number(8),
    changeDate date,
    chageBy varchar (20),
    tableName varchar (20),
    fieldName varchar(20),
    previousName varchar(20),
    currentValue varchar(20),
    id_user number(8)
)
TABLESPACE TS_USER;

ALTER TABLE "log"
    ADD CONSTRAINT pk_log PRIMARY KEY (id_log)
    USING INDEX
    TABLESPACE TS_INDEX;

ALTER TABLE "log"
    ADD CONSTRAINT fk_log_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user);
    
-- ============================================
-- PHOTO
-- ============================================
CREATE TABLE photo --Se supone que son imagenes que sube el adoptante para que la persona que dio en adopcion o la casa refugio puedan ver como se encuentra
(
    id_photo NUMBER(8),
    "date"   DATE,
    photo_dir varchar(200),
    id_user  NUMBER(8)
)
TABLESPACE TS_USER;

ALTER TABLE photo
    ADD CONSTRAINT pk_photo PRIMARY KEY (id_photo)
    USING INDEX
    TABLESPACE TS_INDEX;

ALTER TABLE photo 
    ADD CONSTRAINT fk_photo_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user);

-- ============================================
-- RATING
-- ============================================
CREATE TABLE rating
(
    id_rating  NUMBER(8),
    score      NUMBER(3, 1),
    id_user    NUMBER(8),
    id_adopter NUMBER(8)
)
TABLESPACE TS_USER;

ALTER TABLE rating
    ADD CONSTRAINT pk_rating PRIMARY KEY (id_rating)
    USING INDEX
    TABLESPACE TS_INDEX;

ALTER TABLE rating
    ADD CONSTRAINT fk_rating_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user);

ALTER TABLE rating
    ADD CONSTRAINT fk_rating_adopter
    FOREIGN KEY (id_adopter) REFERENCES adopter (id_user);

-- ============================================
-- PET TYPE
-- ============================================
CREATE TABLE pet_type
(
    id_pet_type NUMBER(4),
    "name"      VARCHAR2(50)
)
TABLESPACE TS_USER;

ALTER TABLE pet_type
    ADD CONSTRAINT pk_pet_type PRIMARY KEY (id_pet_type)
    USING INDEX
    TABLESPACE TS_INDEX;

-- ============================================
-- RACE
-- ============================================
CREATE TABLE race
(
    id_race     NUMBER(4),
    "name"      VARCHAR2(50),
    id_pet_type NUMBER(4)
)
TABLESPACE TS_USER;

ALTER TABLE race
    ADD CONSTRAINT pk_race PRIMARY KEY (id_race)
    USING INDEX
    TABLESPACE TS_INDEX;

ALTER TABLE race
    ADD CONSTRAINT fk_race_pet_type
    FOREIGN KEY (id_pet_type) REFERENCES pet_type (id_pet_type);

-- ============================================
-- PROVINCE
-- ============================================
CREATE TABLE province
(
    id_province NUMBER(4),
    "name"      VARCHAR2(50)
)
TABLESPACE TS_USER;

ALTER TABLE province
    ADD CONSTRAINT pk_province PRIMARY KEY (id_province)
    USING INDEX
    TABLESPACE TS_INDEX;

-- ============================================
-- CANTON
-- ============================================
CREATE TABLE canton
(
    id_canton   NUMBER(4),
    "name"      VARCHAR2(50),
    id_province NUMBER(4)
)
TABLESPACE TS_USER;

ALTER TABLE canton
    ADD CONSTRAINT pk_canton PRIMARY KEY (id_canton)
    USING INDEX
    TABLESPACE TS_INDEX;

ALTER TABLE canton
    ADD CONSTRAINT fk_canton_province
    FOREIGN KEY (id_province) REFERENCES province (id_province);

-- ============================================
-- DISTRICT
-- ============================================
CREATE TABLE district
(
    id_district NUMBER(4),
    "name"      VARCHAR2(50),
    id_canton   NUMBER(4)
)
TABLESPACE TS_USER;

ALTER TABLE district
    ADD CONSTRAINT pk_district PRIMARY KEY (id_district)
    USING INDEX
    TABLESPACE TS_INDEX;

ALTER TABLE district
    ADD CONSTRAINT fk_district_canton
    FOREIGN KEY (id_canton) REFERENCES canton (id_canton);

-- ============================================
-- STATUS
-- ============================================
CREATE TABLE status
(
    id_status   NUMBER(4),
    status_type VARCHAR2(30)
)
TABLESPACE TS_USER;

ALTER TABLE status
    ADD CONSTRAINT pk_status PRIMARY KEY (id_status)
    USING INDEX
    TABLESPACE TS_INDEX;

-- ============================================
-- PET
-- ============================================
CREATE TABLE pet
(
    id_pet      NUMBER(8),
    picture     VARCHAR2(200),--URL, o ruta a la imagen de la mascota
    first_name  VARCHAR2(50),
    birth_date  DATE,
    date_lost   DATE,
    date_found  DATE,
    email       VARCHAR2(50),
    createdBy   VARCHAR2(20),
    createdAt   DATE,
    modifiedBy  VARCHAR2(20),
    modifiedAt  DATE,
    id_status   NUMBER(4),
    id_pet_type NUMBER(4),
    id_rescuer  NUMBER(8)
)
TABLESPACE TS_USER;

ALTER TABLE pet
    ADD CONSTRAINT pk_pet PRIMARY KEY (id_pet)
    USING INDEX
    TABLESPACE TS_INDEX;

ALTER TABLE pet
    ADD CONSTRAINT fk_pet_status
    FOREIGN KEY (id_status) REFERENCES status (id_status);

ALTER TABLE pet
    ADD CONSTRAINT fk_pet_pet_type
    FOREIGN KEY (id_pet_type) REFERENCES pet_type (id_pet_type);

ALTER TABLE pet
    ADD CONSTRAINT fk_pet_rescuer
    FOREIGN KEY (id_rescuer) REFERENCES rescuer (id_user);

-- ============================================
-- IDENTIFICATION CHIP
-- ============================================
CREATE TABLE identification_chip
(
    id_chip           NUMBER(8),
    chip_number       VARCHAR2(30),
    registration_date DATE,
    id_pet            NUMBER(8)
)
TABLESPACE TS_USER;

ALTER TABLE identification_chip
    ADD CONSTRAINT pk_identification_chip PRIMARY KEY (id_chip)
    USING INDEX
    TABLESPACE TS_INDEX;

ALTER TABLE identification_chip
    ADD CONSTRAINT fk_chip_pet
    FOREIGN KEY (id_pet) REFERENCES pet (id_pet);

-- ============================================
-- COLOR
-- ============================================
CREATE TABLE color
(
    id_color NUMBER(4),
    "name"   VARCHAR2(30)
)
TABLESPACE TS_USER;

ALTER TABLE color
    ADD CONSTRAINT pk_color PRIMARY KEY (id_color)
    USING INDEX
    TABLESPACE TS_INDEX;

-- ============================================
-- PET X COLOR
-- ============================================
CREATE TABLE pet_x_color
(
    id_pet   NUMBER(8),
    id_color NUMBER(4)
)
TABLESPACE TS_USER;

ALTER TABLE pet_x_color
    ADD CONSTRAINT pk_pet_x_color PRIMARY KEY (id_pet, id_color)
    USING INDEX
    TABLESPACE TS_INDEX;

ALTER TABLE pet_x_color
    ADD CONSTRAINT fk_pxc_pet
    FOREIGN KEY (id_pet) REFERENCES pet (id_pet);

ALTER TABLE pet_x_color
    ADD CONSTRAINT fk_pxc_color
    FOREIGN KEY (id_color) REFERENCES color (id_color);

-- ============================================
-- PET X DISTRICT
-- ============================================
CREATE TABLE pet_x_district
(
    id_pet      NUMBER(8),
    id_district NUMBER(4)
)
TABLESPACE TS_USER;

ALTER TABLE pet_x_district
    ADD CONSTRAINT pk_pet_x_district PRIMARY KEY (id_pet, id_district)
    USING INDEX
    TABLESPACE TS_INDEX;

ALTER TABLE pet_x_district
    ADD CONSTRAINT fk_pxd_pet
    FOREIGN KEY (id_pet) REFERENCES pet (id_pet);

ALTER TABLE pet_x_district
    ADD CONSTRAINT fk_pxd_district
    FOREIGN KEY (id_district) REFERENCES district (id_district);

-- ============================================
-- PET TYPE X CRIB HOUSE
-- ============================================
CREATE TABLE pet_type_x_crib_house
(
    id_pet_type   NUMBER(4),
    id_crib_house NUMBER(8)
)
TABLESPACE TS_USER;

ALTER TABLE pet_type_x_crib_house
    ADD CONSTRAINT pk_pet_type_x_crib_house PRIMARY KEY (id_pet_type, id_crib_house)
    USING INDEX
    TABLESPACE TS_INDEX;

ALTER TABLE pet_type_x_crib_house
    ADD CONSTRAINT fk_ptxch_pet_type
    FOREIGN KEY (id_pet_type) REFERENCES pet_type (id_pet_type);

ALTER TABLE pet_type_x_crib_house
    ADD CONSTRAINT fk_ptxch_crib_house
    FOREIGN KEY (id_crib_house) REFERENCES crib_house (id_user);

-- ============================================
-- ADOPTION FORM
-- ============================================
CREATE TABLE adoption_form
(
    id_adoption   NUMBER(8),
    notes         VARCHAR2(500),
    adoption_date DATE,
    "reference"     VARCHAR2(200),
    id_adopter    NUMBER(8),
    id_pet        NUMBER(8)
)
TABLESPACE TS_USER;

ALTER TABLE adoption_form
    ADD CONSTRAINT pk_adoption_form PRIMARY KEY (id_adoption)
    USING INDEX
    TABLESPACE TS_INDEX;

ALTER TABLE adoption_form
    ADD CONSTRAINT fk_af_adopter
    FOREIGN KEY (id_adopter) REFERENCES adopter (id_user);

ALTER TABLE adoption_form
    ADD CONSTRAINT fk_af_pet
    FOREIGN KEY (id_pet) REFERENCES pet (id_pet);

-- ============================================
-- VETERINARIAN
-- ============================================
CREATE TABLE veterinarian
(
    id_veterinarian NUMBER(8),
    first_name      VARCHAR2(50),
    second_name     VARCHAR2(50),
    first_surname   VARCHAR2(50),
    second_surname  VARCHAR2(50),
    clinic_name     VARCHAR2(100)
)
TABLESPACE TS_USER;

ALTER TABLE veterinarian
    ADD CONSTRAINT pk_veterinarian PRIMARY KEY (id_veterinarian)
    USING INDEX
    TABLESPACE TS_INDEX;


-- ============================================
-- VALUE TYPE
-- ============================================
CREATE TABLE value_type
(
    id_value_type NUMBER(4),
    "type"        VARCHAR2(30)
)
TABLESPACE TS_USER;

ALTER TABLE value_type
    ADD CONSTRAINT pk_value_type PRIMARY KEY (id_value_type)
    USING INDEX
    TABLESPACE TS_INDEX;

-- ============================================
-- MATCH
-- ============================================
CREATE TABLE match
(
    id_match   NUMBER(8),
    match_date DATE
)
TABLESPACE TS_USER;

ALTER TABLE match
    ADD CONSTRAINT pk_match PRIMARY KEY (id_match)
    USING INDEX
    TABLESPACE TS_INDEX;

-- ============================================
-- PARAMETERS
-- ============================================
CREATE TABLE parameters
(
    id_parameter  NUMBER(8),
    "value"         VARCHAR2(100),
    id_match      NUMBER(8),
    id_value_type NUMBER(4)
)
TABLESPACE TS_USER;

ALTER TABLE parameters
    ADD CONSTRAINT pk_parameters PRIMARY KEY (id_parameter)
    USING INDEX
    TABLESPACE TS_INDEX;

ALTER TABLE parameters
    ADD CONSTRAINT fk_param_match
    FOREIGN KEY (id_match) REFERENCES match (id_match);

ALTER TABLE parameters
    ADD CONSTRAINT fk_param_value_type
    FOREIGN KEY (id_value_type) REFERENCES value_type (id_value_type);



