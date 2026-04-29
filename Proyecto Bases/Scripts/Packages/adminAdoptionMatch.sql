CREATE OR REPLACE PACKAGE adminAdoptionMatch IS

-- INSERT
PROCEDURE insertAdoptionForm(pIdAdoption IN NUMBER, pNotes VARCHAR2,
                            pAdoptionDate IN DATE, pReference IN VARCHAR2,
                            pIdAdopter IN NUMBER, pIdPet IN NUMBER);

PROCEDURE insertPhoto(pIdPhoto IN NUMBER, pDate IN DATE, 
                        pPhotoDir IN VARCHAR2, pIdAdopter IN NUMBER);

PROCEDURE insertRating(pIdRating IN NUMBER, pScore IN NUMBER, 
                        pIdUser IN NUMBER, pIdAdopter IN NUMBER);

PROCEDURE insertMatch(pIdMatch IN NUMBER, pMatchDate IN DATE,
                        pSimilarityPercentage IN NUMBER);

PROCEDURE insertParameters(pIdParameter IN NUMBER, pValue IN VARCHAR2,
                            pIdMatch IN NUMBER, pIdValueType IN NUMBER);

-- UPDATE
                            
PROCEDURE updatePhoto(pIdPhoto IN NUMBER, pDate IN DATE, 
                        pPhotoDir IN VARCHAR2, pIdAdopter IN NUMBER);
                        
PROCEDURE updateRating(pIdRating IN NUMBER, pScore IN NUMBER, 
                        pIdUser IN NUMBER, pIdAdopter IN NUMBER);
                        
PROCEDURE updateMatch(pIdMatch IN NUMBER, pMatchDate IN DATE,
                        pSimilarityPercentage IN NUMBER);
                        
PROCEDURE updateParameters(pIdParameter IN NUMBER, pValue IN VARCHAR2,
                            pIdMatch IN NUMBER, pIdValueType IN NUMBER);
                            
-- GET
FUNCTION getAdoptionForm RETURN SYS_REFCURSOR;
FUNCTION getPhoto RETURN SYS_REFCURSOR;
FUNCTION getRating RETURN SYS_REFCURSOR;
FUNCTION getMatch RETURN SYS_REFCURSOR;
FUNCTION getParameters RETURN SYS_REFCURSOR;

END adminAdoptionMatch;