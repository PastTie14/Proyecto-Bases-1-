-- ============================================================
-- FILE 07 - PHONE NUMBER (UNIFIED)
-- Table: phone_number
-- Depends on: user (02), pet (06), veterinarian (05)
-- Note: Only one FK per row will be populated depending
--       on the entity the number belongs to.
-- ============================================================

-- ============================================
-- PHONE NUMBER
-- ============================================
CREATE TABLE phone_number
(
    id_phone        NUMBER(8),
    "number"        VARCHAR2(20),
    id_user         NUMBER(8),
    id_pet          NUMBER(8),
    id_veterinarian NUMBER(8)
)
TABLESPACE TS_DATA;

ALTER TABLE phone_number
    MODIFY id_phone CONSTRAINT phoneNumber_idPhone_nn NOT NULL;
    
ALTER TABLE phone_number
    MODIFY "number" CONSTRAINT phoneNumber_number__ NOT NULL;

ALTER TABLE phone_number
    ADD CONSTRAINT pk_phone_number PRIMARY KEY (id_phone)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE phone_number
    ADD CONSTRAINT fk_phone_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user);

ALTER TABLE phone_number
    ADD CONSTRAINT fk_phone_pet
    FOREIGN KEY (id_pet) REFERENCES pet (id_pet);

ALTER TABLE phone_number
    ADD CONSTRAINT fk_phone_veterinarian
    FOREIGN KEY (id_veterinarian) REFERENCES veterinarian (id_veterinarian);

ALTER TABLE phone_number
    ADD CONSTRAINT chk_phone_single_owner
    CHECK (
        (CASE WHEN id_user         IS NOT NULL THEN 1 ELSE 0 END +
         CASE WHEN id_pet          IS NOT NULL THEN 1 ELSE 0 END +
         CASE WHEN id_veterinarian IS NOT NULL THEN 1 ELSE 0 END) = 1
    );
