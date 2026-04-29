CREATE OR REPLACE PACKAGE adminMedical IS

PROCEDURE insertTreatment(pIdTreatment IN NUMBER, pName IN VARCHAR2, pDose IN VARCHAR2);

PROCEDURE insertDisease(pIdDisease IN NUMBER, pName IN VARCHAR2);

PROCEDURE insertMedicSheet(pIdMedicSheet IN NUMBER, pAbandonmentDescription IN VARCHAR2,
                                pIdVeterinarian IN NUMBER, pIdPetExtraInfo IN NUMBER);

PROCEDURE insertDiseaseXMedicSheet(pIdTreatment IN NUMBER, pIdDisease IN NUMBER);

PROCEDURE insertTreatmentXDisease(pIdTreatment IN NUMBER, pIdDisease IN NUMBER);

END adminMedical;