-- ============================================================
-- FILE 10 - MEDICAL
-- Tables: medic_sheet, disease, disease_x_medic_sheet,
--         treatment, treatment_x_disease
-- Depends on: veterinarian (05), pet_extra_info (09)
-- ============================================================

-- ============================================
-- DISEASE
-- ============================================
CREATE TABLE disease
(
    id_disease NUMBER(8),
    "name"     VARCHAR2(100)
)
TABLESPACE TS_DATA;

ALTER TABLE disease
    ADD CONSTRAINT pk_disease PRIMARY KEY (id_disease)
    USING INDEX TABLESPACE TS_INDEX;

COMMENT ON TABLE disease
IS 'Stores information about diseases that affect pets';

COMMENT ON COLUMN disease.id_disease
IS 'Primary key, identifier for the disease';

COMMENT ON COLUMN disease."name"
IS 'The name of the disease';

COMMENT ON COLUMN disease.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN disease.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN disease.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN disease.ModifiedAt
IS 'The date the table was modified';

-- ============================================
-- TREATMENT
-- ============================================
CREATE TABLE treatment
(
    id_treatment NUMBER(8),
    "name"       VARCHAR2(100),
    dose         VARCHAR2(100)
)
TABLESPACE TS_DATA;

ALTER TABLE treatment
    ADD CONSTRAINT pk_treatment PRIMARY KEY (id_treatment)
    USING INDEX TABLESPACE TS_INDEX;

COMMENT ON TABLE treatment
IS 'Stores information about treatments for diseases';

COMMENT ON COLUMN treatment.id_treatment
IS 'Primary key, identifier for the treatment';

COMMENT ON COLUMN treatment."name"
IS 'The name of the treatment';

COMMENT ON COLUMN treatment.dose
IS 'The dose the pet must take';

COMMENT ON COLUMN treatment.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN treatment.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN treatment.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN treatment.ModifiedAt
IS 'The date the table was modified';

-- ============================================
-- MEDIC SHEET
-- ============================================
CREATE TABLE medic_sheet
(
    id_medic_sheet          NUMBER(8),
    abandonment_description VARCHAR2(500),
    id_veterinarian         NUMBER(8),
    id_pet_extra_info       NUMBER(8)
)
TABLESPACE TS_DATA;

ALTER TABLE medic_sheet
    ADD CONSTRAINT pk_medic_sheet PRIMARY KEY (id_medic_sheet)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE medic_sheet
    ADD CONSTRAINT fk_ms_veterinarian
    FOREIGN KEY (id_veterinarian) REFERENCES veterinarian (id_veterinarian);

ALTER TABLE medic_sheet
    ADD CONSTRAINT fk_ms_pet_extra_info
    FOREIGN KEY (id_pet_extra_info) REFERENCES pet_extra_info (id_pet_extra_info)
    ON DELETE CASCADE;

COMMENT ON TABLE medic_sheet
IS 'Stores information about the health related matters of a pet';

COMMENT ON COLUMN medic_sheet.id_medic_sheet
IS 'Primary key, identifier for the training ease';

COMMENT ON COLUMN medic_sheet.abandonment_description
IS 'Describes the state in which the pet was found';

COMMENT ON COLUMN medic_sheet.id_veterinarian
IS 'oreign key, references the veterinarian who treats the pet';

COMMENT ON COLUMN medic_sheet.id_pet_extra_info
IS 'Foreign key, references the pet extra info';

COMMENT ON COLUMN medic_sheet.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN medic_sheet.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN medic_sheet.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN medic_sheet.ModifiedAt
IS 'The date the table was modified';

-- ============================================
-- DISEASE X MEDIC SHEET
-- ============================================
CREATE TABLE disease_x_medic_sheet
(
    id_disease     NUMBER(8),
    id_medic_sheet NUMBER(8)
)
TABLESPACE TS_DATA;

ALTER TABLE disease_x_medic_sheet
    ADD CONSTRAINT pk_disease_x_medic_sheet PRIMARY KEY (id_disease, id_medic_sheet)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE disease_x_medic_sheet
    ADD CONSTRAINT fk_dxms_disease
    FOREIGN KEY (id_disease) REFERENCES disease (id_disease)
    ON DELETE CASCADE;

ALTER TABLE disease_x_medic_sheet
    ADD CONSTRAINT fk_dxms_medic_sheet
    FOREIGN KEY (id_medic_sheet) REFERENCES medic_sheet (id_medic_sheet)
    ON DELETE CASCADE;

COMMENT ON TABLE disease_x_medic_sheet
IS 'Intermediate table, stores all the diseases that a pet has';

COMMENT ON COLUMN disease_x_medic_sheet.id_disease
IS 'Composite primary key. Foreign key references the disease id';

COMMENT ON COLUMN disease_x_medic_sheet.id_medic_sheet
IS 'Composite primary key. Foreign key references the medic sheet id';

COMMENT ON COLUMN disease_x_medic_sheet.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN disease_x_medic_sheet.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN disease_x_medic_sheet.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN disease_x_medic_sheet.ModifiedAt
IS 'The date the table was modified';

-- ============================================
-- TREATMENT X DISEASE
-- ============================================
CREATE TABLE treatment_x_disease
(
    id_treatment NUMBER(8),
    id_disease   NUMBER(8)
)
TABLESPACE TS_DATA;

ALTER TABLE treatment_x_disease
    ADD CONSTRAINT pk_treatment_x_disease PRIMARY KEY (id_treatment, id_disease)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE treatment_x_disease
    ADD CONSTRAINT fk_txd_treatment
    FOREIGN KEY (id_treatment) REFERENCES treatment (id_treatment)
    ON DELETE CASCADE;

ALTER TABLE treatment_x_disease
    ADD CONSTRAINT fk_txd_disease
    FOREIGN KEY (id_disease) REFERENCES disease (id_disease)
    ON DELETE CASCADE;

COMMENT ON TABLE treatment_x_disease
IS 'Intermediate table, stores all treatments of a disease';

COMMENT ON COLUMN treatment_x_disease.id_treatment
IS 'Composite primary key. Foreign key references the treatment id';

COMMENT ON COLUMN treatment_x_disease.id_disease
IS 'Composite primary key. Foreign key references the disease id';

COMMENT ON COLUMN treatment_x_disease.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN treatment_x_disease.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN treatment_x_disease.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN treatment_x_disease.ModifiedAt
IS 'The date the table was modified';
