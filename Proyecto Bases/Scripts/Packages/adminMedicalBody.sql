CREATE OR REPLACE PACKAGE BODY adminMedical AS

PROCEDURE insertTreatment(pIdTreatment IN NUMBER, pName IN VARCHAR2,
                                            pDose IN VARCHAR2)
IS 
BEGIN
    INSERT INTO treatment (id_treatment, "name", dose)
    VALUES(pIdTreatment, pName, pDose);
    COMMIT;
END insertTreatment;

--=======================================================================================

PROCEDURE insertDisease(pIdDisease IN NUMBER, pName IN VARCHAR2)
IS 
BEGIN
    INSERT INTO disease (id_disease, "name")
    VALUES(pIdDisease, pName);
    COMMIT;
END insertDisease;

--=======================================================================================

PROCEDURE insertMedicSheet(pIdMedicSheet IN NUMBER, pAbandonmentDescription IN VARCHAR2,
                            pIdVeterinarian IN NUMBER, pIdPetExtraInfo IN NUMBER)
IS 
BEGIN
    INSERT INTO medic_sheet (id_medic_sheet, abandonment_description, id_veterinarian, id_pet_extra_info)
    VALUES(pIdMedicSheet, pAbandonmentDescription, pIdVeterinarian, pIdPetExtraInfo);
    COMMIT;
END insertMedicSheet;

--=======================================================================================

PROCEDURE insertDiseaseXMedicSheet(pIdDisease IN NUMBER, pIdMedicSheet IN NUMBER)
IS 
BEGIN
    INSERT INTO disease_x_medic_sheet (id_disease, id_medic_sheet)
    VALUES(pIdDisease, pIdMedicSheet);
    COMMIT;
END insertDiseaseXMedicSheet;

--=======================================================================================

PROCEDURE insertTreatmentXDisease(pIdTreatment IN NUMBER, pIdDisease IN NUMBER)
IS 
BEGIN
    INSERT INTO treatment_x_disease (id_treatment, id_disease)
    VALUES(pIdTreatment, pIdDisease);
    COMMIT;
END insertTreatmentXDisease;


-- ======================================== UPDATE ========================================
PROCEDURE updateTreatment(pIdTreatment IN NUMBER, pName IN VARCHAR2, pDose IN VARCHAR2)
IS
BEGIN
    UPDATE treatment
    SET "name" = pName,
        dose = pDose
    
    WHERE id_treatment = pIdTreatment;
    COMMIT;
END;

PROCEDURE updateDisease(pIdDisease IN NUMBER, pName IN VARCHAR2)
IS
BEGIN
    UPDATE disease
    SET "name" = pName
    
    WHERE id_disease = pIdDisease;
    COMMIT;
END;

PROCEDURE updateMedicSheet(pIdMedicSheet IN NUMBER, pAbandonmentDescription IN VARCHAR2,
                                pIdVeterinarian IN NUMBER, pIdPetExtraInfo IN NUMBER)
IS
BEGIN
    UPDATE medic_sheet
    SET abandonment_description = pAbandonmentDescription
    
    WHERE id_medic_sheet = pIdMedicSheet
    AND id_veterinarian = pIdVeterinarian
    AND id_pet_extra_info = pIdPetExtraInfo;
    COMMIT;
END;

-- ======================================== GET ========================================

FUNCTION getTreatment RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT * FROM treatment;
        RETURN v_cursor;
END;

FUNCTION getDisease RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT * FROM disease;
        RETURN v_cursor;
END;

FUNCTION getMedicSheet RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT * FROM medic_sheet;
        RETURN v_cursor;
END;

FUNCTION getDiseaseXMedicSheet RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT * FROM disease_x_medic_sheet;
        RETURN v_cursor;
END;

FUNCTION getTreatmentXDisease RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT * FROM treatment_x_disease;
        RETURN v_cursor;
END;

FUNCTION getDiseasesFromMedicSheet(pIdMedicSheet IN NUMBER) RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR 
        SELECT d."name"
        FROM disease d
    
        INNER JOIN disease_x_medic_sheet dxms
        ON d.id_disease = dxms.id_disease
    
        WHERE dxms.id_medic_sheet = pIdMedicSheet;
    RETURN v_cursor;
END;

FUNCTION getTreatmentsForDisease(pIdDisease IN NUMBER) RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR 
        SELECT t."name"
        FROM treatment t
    
        INNER JOIN treatment_x_disease txd
        ON t.id_treatment = txd.id_treatment
    
        WHERE txd.id_disease = pIdDisease;
    RETURN v_cursor;
END;

END adminMedical;