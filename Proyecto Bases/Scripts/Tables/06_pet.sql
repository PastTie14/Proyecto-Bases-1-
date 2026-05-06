-- ============================================================
-- FILE 06 - PET & PET RELATIONSHIPS
-- Tables: pet, identification_chip, pet_x_color,
--         pet_x_district, pet_type_x_crib_house
-- Depends on: catalogs (01), user subtypes (02), veterinarian (05)
-- ============================================================

-- ============================================
-- PET
-- ============================================
CREATE TABLE pet
(
    id_pet      NUMBER(8),
    picture     VARCHAR2(200),
    first_name  VARCHAR2(50),
    birth_date  DATE,
    date_lost   DATE,
    date_found  DATE,
    email       VARCHAR2(50),
    createdBy   VARCHAR2(20),
    createdAt   DATE,
    modifiedBy  VARCHAR2(20),
    modifiedAt  DATE,
    id_size     NUMBER(8),
    id_status   NUMBER(4),
    id_pet_type NUMBER(4),
    id_rescuer  NUMBER(8),
    id_crib_house NUMBER(8),
    id_district NUMBER(8)
)
TABLESPACE TS_DATA;

ALTER TABLE pet
    MODIFY id_pet CONSTRAINT pet_idPet_nn NOT NULL;

ALTER TABLE pet
    MODIFY first_name CONSTRAINT pet_firstName_nn NOT NULL;

ALTER TABLE pet
    MODIFY id_size CONSTRAINT pet_idSize_nn NOT NULL;

ALTER TABLE pet
    MODIFY createdBy CONSTRAINT pet_createdBy_nn NOT NULL;

ALTER TABLE pet
    MODIFY createdAt CONSTRAINT pet_createdAt_nn NOT NULL;

ALTER TABLE pet
    MODIFY id_status CONSTRAINT pet_idStatus_nn NOT NULL;

ALTER TABLE pet
    MODIFY id_pet_type CONSTRAINT pet_idPetType_nn NOT NULL;

ALTER TABLE pet
    MODIFY id_rescuer CONSTRAINT pet_idRescuer_nn NOT NULL;

ALTER TABLE pet
    ADD CONSTRAINT pk_pet PRIMARY KEY (id_pet)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE pet
    ADD CONSTRAINT fk_pet_status
    FOREIGN KEY (id_status) REFERENCES status (id_status);

ALTER TABLE pet
    ADD CONSTRAINT fk_pet_size
    FOREIGN KEY (id_size) REFERENCES "size" (id_size);

ALTER TABLE pet
    ADD CONSTRAINT fk_pet_pet_type
    FOREIGN KEY (id_pet_type) REFERENCES pet_type (id_pet_type);

ALTER TABLE pet
    ADD CONSTRAINT fk_pet_rescuer
    FOREIGN KEY (id_rescuer) REFERENCES rescuer (id_user);
    
ALTER TABLE pet
    ADD CONSTRAINT fk_pet_crib_house
    FOREIGN KEY (id_crib_house) REFERENCES crib_house (id_user);
    
ALTER TABLE pet
    ADD CONSTRAINT fk_pet_district
    FOREIGN KEY (id_district) REFERENCES district (id_district);

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
TABLESPACE TS_DATA;

ALTER TABLE identification_chip
    MODIFY id_chip CONSTRAINT identificationChip_idChip_nn NOT NULL;

ALTER TABLE identification_chip
    MODIFY chip_number CONSTRAINT identifChip_chipNumber_nn NOT NULL;

ALTER TABLE identification_chip
    MODIFY registration_date CONSTRAINT identChip_registrationDate_nn NOT NULL;

ALTER TABLE identification_chip
    MODIFY id_pet CONSTRAINT identificationChip_idPet_nn NOT NULL;

ALTER TABLE identification_chip
    ADD CONSTRAINT pk_identification_chip PRIMARY KEY (id_chip)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE identification_chip
    ADD CONSTRAINT uq_chip_number UNIQUE (chip_number);

ALTER TABLE identification_chip
    ADD CONSTRAINT fk_chip_pet
    FOREIGN KEY (id_pet) REFERENCES pet (id_pet)
    ON DELETE CASCADE;

-- ============================================
-- PET X COLOR
-- ============================================
CREATE TABLE pet_x_color
(
    id_pet   NUMBER(8),
    id_color NUMBER(4)
)
TABLESPACE TS_DATA;

ALTER TABLE pet_x_color
    MODIFY id_pet CONSTRAINT petXColor_idPet_nn NOT NULL;

ALTER TABLE pet_x_color
    MODIFY id_color CONSTRAINT petXColor_idColor_nn NOT NULL;

ALTER TABLE pet_x_color
    ADD CONSTRAINT pk_pet_x_color PRIMARY KEY (id_pet, id_color)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE pet_x_color
    ADD CONSTRAINT fk_pxc_pet
    FOREIGN KEY (id_pet) REFERENCES pet (id_pet)
    ON DELETE CASCADE;

ALTER TABLE pet_x_color
    ADD CONSTRAINT fk_pxc_color
    FOREIGN KEY (id_color) REFERENCES color (id_color)
    ON DELETE CASCADE;

-- ============================================
-- PET TYPE X CRIB HOUSE
-- ============================================
CREATE TABLE pet_type_x_crib_house
(
    id_pet_type   NUMBER(4),
    id_crib_house NUMBER(8)
)
TABLESPACE TS_DATA;

ALTER TABLE pet_type_x_crib_house
    MODIFY id_pet_type CONSTRAINT petTypeXCribHouse_idPetType_nn NOT NULL;

ALTER TABLE pet_type_x_crib_house
    MODIFY id_crib_house CONSTRAINT petTypeXCrib_idCribHouse_nn NOT NULL;

ALTER TABLE pet_type_x_crib_house
    ADD CONSTRAINT pk_pet_type_x_crib_house PRIMARY KEY (id_pet_type, id_crib_house)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE pet_type_x_crib_house
    ADD CONSTRAINT fk_ptxch_pet_type
    FOREIGN KEY (id_pet_type) REFERENCES pet_type (id_pet_type)
    ON DELETE CASCADE;

ALTER TABLE pet_type_x_crib_house
    ADD CONSTRAINT fk_ptxch_crib_house
    FOREIGN KEY (id_crib_house) REFERENCES crib_house (id_user)
    ON DELETE CASCADE;
