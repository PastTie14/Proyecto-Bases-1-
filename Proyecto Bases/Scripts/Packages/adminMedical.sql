CREATE OR REPLACE PACKAGE adminMedical IS

PROCEDURE insertTreatment(id_treatment IN NUMBER, "name" IN VARCHAR2, dose IN VARCHAR2);

PROCEDURE insertDisease(id_disease IN NUMBER, "name" IN VARCHAR2);

PROCEDURE insertMedicSheet(id_medicSheet IN NUMBER, abandonmentDescription IN VARCHAR2,
                                id_veterinarian IN NUMBER, id_petExtraInfo IN NUMBER);

PROCEDURE insertDiseaseXMedicSheet(id_treatment IN NUMBER, id_disease IN NUMBER);

PROCEDURE insertTreatmentXDisease(id_treatment IN NUMBER, id_disease IN NUMBER);

END adminMedical;