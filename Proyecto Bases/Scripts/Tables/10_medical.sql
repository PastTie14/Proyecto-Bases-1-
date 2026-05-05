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
