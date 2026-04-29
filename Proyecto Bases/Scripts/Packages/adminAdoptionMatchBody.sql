CREATE OR REPLACE PACKAGE BODY adminAdoptionMatch AS

PROCEDURE insertAdoptionForm(pIdAdoption IN NUMBER, pNotes VARCHAR2,
                                        pAdoptionDate IN DATE, pReference IN VARCHAR2,
                                        pIdAdopter IN NUMBER, pIdPet IN NUMBER)
IS 
BEGIN
    INSERT INTO adoption_form
    VALUES(pIdAdoption, pNotes, pAdoptionDate, pReference, pIdAdopter, pIdPet);
    COMMIT;
END insertAdoptionForm;

--=======================================================================================

PROCEDURE insertPhoto(pIdPhoto IN NUMBER, pDate IN DATE, 
                                    pPhotoDir IN VARCHAR2, pIdAdopter IN NUMBER)
IS 
BEGIN
    INSERT INTO photo
    VALUES(pIdPhoto, pDate, pPhotoDir, pIdAdopter);
    COMMIT;
END insertPhoto;

--=======================================================================================

PROCEDURE insertRating(pIdRating IN NUMBER, pScore IN NUMBER, 
                                        pIdUser IN NUMBER, pIdAdopter IN NUMBER)
IS 
BEGIN
    INSERT INTO rating
    VALUES(pIdRating, pScore, pIdUser, pIdAdopter);
    COMMIT;
END insertRating;

--=======================================================================================

PROCEDURE insertMatch(pIdMatch IN NUMBER, pMatchDate IN DATE,
                                        pSimilarityPercentage IN NUMBER)
IS 
BEGIN
    INSERT INTO match
    VALUES(pIdMatch, pMatchDate, pSimilarityPercentage);
    COMMIT;
END insertMatch;

--=======================================================================================

PROCEDURE insertParameters(pIdParameter IN NUMBER, pValue IN VARCHAR2,
                                            pIdMatch IN NUMBER, pIdValueType IN NUMBER)
IS 
BEGIN
    INSERT INTO parameters
    VALUES(pIdParameter, pValue, pIdMatch, pIdValueType);
    COMMIT;
END insertParameters;

END adminAdoptionMatch;