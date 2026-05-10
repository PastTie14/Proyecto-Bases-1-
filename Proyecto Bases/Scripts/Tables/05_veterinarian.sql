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
    clinic_name     VARCHAR2(100),
    createdBy  VARCHAR2(20),
    createdAt  DATE,
    modifiedBy VARCHAR2(20),
    modifiedAt DATE
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
    
COMMENT ON TABLE veterinarian
IS 'Stores veterinarian information';

COMMENT ON COLUMN veterinarian.id_veterinarian
IS 'Primary key, identifier for the veterinarian';

COMMENT ON COLUMN veterinarian.first_name
IS 'First name of the veterinarian';

COMMENT ON COLUMN veterinarian.second_name
IS 'Second name of the veterinarian';

COMMENT ON COLUMN veterinarian.first_surname
IS 'Paternal surname of the veterinarian';

COMMENT ON COLUMN veterinarian.second_surname
IS 'Maternal surname of the veterinarian';

COMMENT ON COLUMN veterinarian.clinic_name
IS 'Name of the clinic';

COMMENT ON COLUMN veterinarian.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN veterinarian.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN veterinarian.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN veterinarian.ModifiedAt
IS 'The date the table was modified';