-- ============================================================
-- FILE 05 - VETERINARIAN
-- Tables: veterinarian
-- No dependencies on other custom tables
-- ============================================================

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
TABLESPACE TS_DATA;

ALTER TABLE veterinarian
    MODIFY id_veterinarian CONSTRAINT veterinarian_idVeterinarian_nn NOT NULL;

ALTER TABLE veterinarian
    MODIFY first_name CONSTRAINT veterinarian_firstName_nn NOT NULL;

ALTER TABLE veterinarian
    MODIFY first_surname CONSTRAINT veterinarian_firstSurname_nn NOT NULL;

ALTER TABLE veterinarian
    MODIFY clinic_name CONSTRAINT veterinarian_clinicName_nn NOT NULL;

ALTER TABLE veterinarian
    ADD CONSTRAINT pk_veterinarian PRIMARY KEY (id_veterinarian)
    USING INDEX TABLESPACE TS_INDEX;