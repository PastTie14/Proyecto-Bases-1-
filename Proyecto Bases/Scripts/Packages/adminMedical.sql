CREATE OR REPLACE PACKAGE adminMedical IS

-- INSERT
PROCEDURE insertTreatment(pIdTreatment IN NUMBER, pName IN VARCHAR2, pDose IN VARCHAR2);

PROCEDURE insertDisease(pIdDisease IN NUMBER, pName IN VARCHAR2);

PROCEDURE insertMedicSheet(pIdMedicSheet IN NUMBER, pAbandonmentDescription IN VARCHAR2,
                                pIdVeterinarian IN NUMBER, pIdPetExtraInfo IN NUMBER);

PROCEDURE insertDiseaseXMedicSheet(pIdDisease IN NUMBER, pIdMedicSheet IN NUMBER);

PROCEDURE insertTreatmentXDisease(pIdTreatment IN NUMBER, pIdDisease IN NUMBER);

-- UPDATE
PROCEDURE updateTreatment(pIdTreatment IN NUMBER, pName IN VARCHAR2, pDose IN VARCHAR2);

PROCEDURE updateDisease(pIdDisease IN NUMBER, pName IN VARCHAR2);

PROCEDURE updateMedicSheet(pIdMedicSheet IN NUMBER, pAbandonmentDescription IN VARCHAR2,
                                pIdVeterinarian IN NUMBER, pIdPetExtraInfo IN NUMBER);

--PROCEDURE updateDiseaseXMedicSheet(pIdTreatment IN NUMBER, pIdDisease IN NUMBER);

--PROCEDURE updateTreatmentXDisease(pIdTreatment IN NUMBER, pIdDisease IN NUMBER);

-- GET
FUNCTION getTreatment RETURN SYS_REFCURSOR;
FUNCTION getDisease RETURN SYS_REFCURSOR;
FUNCTION getMedicSheet RETURN SYS_REFCURSOR;
FUNCTION getDiseaseXMedicSheet RETURN SYS_REFCURSOR;
FUNCTION getTreatmentXDisease RETURN SYS_REFCURSOR;
END adminMedical;