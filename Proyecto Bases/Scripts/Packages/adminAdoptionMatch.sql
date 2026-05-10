CREATE OR REPLACE PACKAGE adminAdoptionMatch IS
 
-- ======================================== INSERT ========================================
 
PROCEDURE insertAdoptionForm(
    pNotes        IN VARCHAR2,
    pAdoptionDate IN DATE,
    pReference    IN VARCHAR2,
    pIdAdopter    IN NUMBER,
    pIdPet        IN NUMBER
);
 
PROCEDURE insertPhoto(
    pIdPhoto   IN NUMBER,
    pDate      IN DATE,
    pPhotoDir  IN VARCHAR2,
    pIdAdopter IN NUMBER
);
 
PROCEDURE insertRating(
    pIdRating  IN NUMBER,
    pScore     IN NUMBER,
    pIdUser    IN NUMBER,
    pIdAdopter IN NUMBER
);
 
PROCEDURE insertMatch(
    pIdMatch              IN NUMBER,
    pMatchDate            IN DATE
);
 
PROCEDURE insertParameters(
    pIdParameter  IN NUMBER,
    pValue        IN VARCHAR2,
    pIdMatch      IN NUMBER,
    pIdValueType  IN NUMBER
);
 
-- ======================================== UPDATE ========================================

 
PROCEDURE updateAdoptionForm(
    pIdAdoption   IN NUMBER,
    pNotes        IN VARCHAR2,
    pAdoptionDate IN DATE,
    pReference    IN VARCHAR2
);
 
PROCEDURE updatePhoto(
    pIdPhoto   IN NUMBER,
    pDate      IN DATE,
    pPhotoDir  IN VARCHAR2,
    pIdAdopter IN NUMBER
);
 
PROCEDURE updateRating(
    pIdRating  IN NUMBER,
    pScore     IN NUMBER,
    pIdUser    IN NUMBER,
    pIdAdopter IN NUMBER
);
 
PROCEDURE updateMatch(
    pIdMatch              IN NUMBER,
    pMatchDate            IN DATE
);
 
PROCEDURE updateParameters(
    pIdParameter IN NUMBER,
    pValue       IN VARCHAR2,
    pIdMatch     IN NUMBER,
    pIdValueType IN NUMBER
);
 
-- ======================================== GET ========================================
 
FUNCTION getAdoptionForm         RETURN SYS_REFCURSOR;
 
FUNCTION getAdoptionFormById(pIdAdoption IN NUMBER)  RETURN SYS_REFCURSOR;
 
FUNCTION getAdoptionsByPet(pIdPet IN NUMBER)         RETURN SYS_REFCURSOR;
 
FUNCTION getAdoptionsByAdopter(pIdAdopter IN NUMBER) RETURN SYS_REFCURSOR;
 
FUNCTION getPhoto      RETURN SYS_REFCURSOR;
FUNCTION getRating     RETURN SYS_REFCURSOR;
FUNCTION getMatch      RETURN SYS_REFCURSOR;
FUNCTION getParameters RETURN SYS_REFCURSOR;
 
-- ======================================== DELETE ========================================

 
PROCEDURE deleteAdoptionForm(pIdAdoption IN NUMBER);
PROCEDURE deletePhoto (pIdPhoto  IN NUMBER);
PROCEDURE deleteRating(pIdRating IN NUMBER);
 
END adminAdoptionMatch;