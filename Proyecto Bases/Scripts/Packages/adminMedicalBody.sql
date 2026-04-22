CREATE OR REPLACE PACKAGE BODY adminMedical AS

PROCEDURE insertTreatment(id_treatment IN NUMBER, "name" IN VARCHAR2,
                                            dose IN VARCHAR2)

IS 
BEGIN
    INSERT INTO treatment
    VALUES(id_treatment, "name", dose);
    COMMIT;
END insertTreatment;

--=======================================================================================

PROCEDURE insertDisease(id_disease IN NUMBER, "name" IN VARCHAR2)

IS 
BEGIN
    INSERT INTO disease
    VALUES(id_disease, "name");
    COMMIT;
END insertDisease;

--=======================================================================================

PROCEDURE insertMedicSheet(id_medicSheet IN NUMBER, abandonmentDescription IN VARCHAR2,
                                            id_veterinarian IN NUMBER, id_petExtraInfo IN NUMBER)

IS 
BEGIN
    INSERT INTO medic_sheet
    VALUES(id_medicSheet, abandonmentDescription, id_veterinarian, id_petExtraInfo);
    COMMIT;
END insertMedicSheet;

--=======================================================================================

PROCEDURE insertDiseaseXMedicSheet(id_treatment IN NUMBER, id_disease IN NUMBER)

IS 
BEGIN
    INSERT INTO disease_x_medic_sheet
    VALUES(id_treatment, id_disease);
    COMMIT;
END insertDiseaseXMedicSheet;

--=======================================================================================

PROCEDURE insertTreatmentXDisease(id_treatment IN NUMBER, id_disease IN NUMBER)

IS 
BEGIN
    INSERT INTO treatment_x_disease
    VALUES(id_treatment, id_disease);
    COMMIT;
END insertTreatmentXDisease;

END adminMedical;