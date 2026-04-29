CREATE OR REPLACE PACKAGE BODY adminMedical AS

PROCEDURE insertTreatment(pIdTreatment IN NUMBER, pName IN VARCHAR2,
                                            pDose IN VARCHAR2)
IS 
BEGIN
    INSERT INTO treatment
    VALUES(pIdTreatment, pName, pDose);
    COMMIT;
END insertTreatment;

--=======================================================================================

PROCEDURE insertDisease(pIdDisease IN NUMBER, pName IN VARCHAR2)
IS 
BEGIN
    INSERT INTO disease
    VALUES(pIdDisease, pName);
    COMMIT;
END insertDisease;

--=======================================================================================

PROCEDURE insertMedicSheet(pIdMedicSheet IN NUMBER, pAbandonmentDescription IN VARCHAR2,
                                            pIdVeterinarian IN NUMBER, pIdPetExtraInfo IN NUMBER)
IS 
BEGIN
    INSERT INTO medic_sheet
    VALUES(pIdMedicSheet, pAbandonmentDescription, pIdVeterinarian, pIdPetExtraInfo);
    COMMIT;
END insertMedicSheet;

--=======================================================================================

PROCEDURE insertDiseaseXMedicSheet(pIdTreatment IN NUMBER, pIdDisease IN NUMBER)
IS 
BEGIN
    INSERT INTO disease_x_medic_sheet
    VALUES(pIdTreatment, pIdDisease);
    COMMIT;
END insertDiseaseXMedicSheet;

--=======================================================================================

PROCEDURE insertTreatmentXDisease(pIdTreatment IN NUMBER, pIdDisease IN NUMBER)
IS 
BEGIN
    INSERT INTO treatment_x_disease
    VALUES(pIdTreatment, pIdDisease);
    COMMIT;
END insertTreatmentXDisease;

END adminMedical;