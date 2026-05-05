CREATE OR REPLACE PACKAGE adminMedical IS

-- INSERT
PROCEDURE insertTreatment(pIdTreatment IN NUMBER, pName IN VARCHAR2, pDose IN VARCHAR2);

PROCEDURE insertDisease(pIdDisease IN NUMBER, pName IN VARCHAR2);

PROCEDURE insertMedicSheet(pIdMedicSheet IN NUMBER, pAbandonmentDescription IN VARCHAR2,
                                pIdVeterinarian IN NUMBER, pIdPetExtraInfo IN NUMBER);

PROCEDURE insertDiseaseXMedicSheet(pIdDisease IN NUMBER, pIdMedicSheet IN NUMBER);

PROCEDURE insertTreatmentXDisease(pIdTreatment IN NUMBER, pIdDisease IN NUMBER);

FUNCTION insertar_veterinario (
    p_id_veterinarian   IN NUMBER,
    p_first_name        IN VARCHAR2,
    p_second_name       IN VARCHAR2,
    p_first_surname     IN VARCHAR2,
    p_second_surname    IN VARCHAR2,
    p_clinic_name       IN VARCHAR2
) RETURN NUMBER;


-- UPDATE
PROCEDURE updateTreatment(pIdTreatment IN NUMBER, pName IN VARCHAR2, pDose IN VARCHAR2);

PROCEDURE updateDisease(pIdDisease IN NUMBER, pName IN VARCHAR2);

PROCEDURE updateMedicSheet(pIdMedicSheet IN NUMBER, pAbandonmentDescription IN VARCHAR2,
                                pIdVeterinarian IN NUMBER, pIdPetExtraInfo IN NUMBER);

-- GET
FUNCTION getTreatment RETURN SYS_REFCURSOR;
FUNCTION getTreatmentById(pIdTreatment IN NUMBER) RETURN SYS_REFCURSOR;

FUNCTION getDisease RETURN SYS_REFCURSOR;
FUNCTION getDiseaseById(pIdDisease IN NUMBER) RETURN SYS_REFCURSOR;

FUNCTION getMedicSheet RETURN SYS_REFCURSOR;
FUNCTION getMedicSheetById(pIdMedicSheet IN NUMBER) RETURN SYS_REFCURSOR;
FUNCTION getDiseasesAndTreatments(p_idPet IN NUMBER) RETURN SYS_REFCURSOR;

FUNCTION getDiseaseXMedicSheet RETURN SYS_REFCURSOR;
FUNCTION getTreatmentXDisease RETURN SYS_REFCURSOR;
FUNCTION getDiseasesFromMedicSheet(pIdMedicSheet IN NUMBER) RETURN SYS_REFCURSOR;
FUNCTION getTreatmentsForDisease(pIdDisease IN NUMBER) RETURN SYS_REFCURSOR;

END adminMedical;