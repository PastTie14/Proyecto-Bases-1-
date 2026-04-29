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


-- ======================================== UPDATE ========================================
                            
PROCEDURE updatePhoto(pIdPhoto IN NUMBER, pDate IN DATE, 
                        pPhotoDir IN VARCHAR2, pIdAdopter IN NUMBER) 
IS
BEGIN
    UPDATE photo
    SET "date" = pDate, 
        photo_dir = pPhotoDir
    
    WHERE id_photo = pIdPhoto
    AND id_user = pIdAdopter;
    COMMIT;
END;

PROCEDURE updateRating(pIdRating IN NUMBER, pScore IN NUMBER, 
                        pIdUser IN NUMBER, pIdAdopter IN NUMBER)
IS
BEGIN
    UPDATE rating
    SET score = pScore
    
    WHERE id_rating = pIdRating
    AND id_user = pIdUser
    AND id_adopter = pIdAdopter;
    COMMIT;
END;                        

PROCEDURE updateMatch(pIdMatch IN NUMBER, pMatchDate IN DATE,
                        pSimilarityPercentage IN NUMBER)
IS
BEGIN
    UPDATE match
    SET match_date = pMatchDate,
        similarity_percentage = pSimilarityPercentage
    
    WHERE id_match = pIdMatch;
    COMMIT;
END;

PROCEDURE updateParameters(pIdParameter IN NUMBER, pValue IN VARCHAR2,
                            pIdMatch IN NUMBER, pIdValueType IN NUMBER)
IS
BEGIN
    UPDATE parameters
    SET "value" = pValue,
        id_match = pIdMatch,
        id_value_type = pIdValueType
    
    WHERE id_parameter = pIdParameter;
    COMMIT;
END;


-- ======================================== GET ========================================

FUNCTION getAdoptionForm RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT * FROM adoption_form;
        RETURN v_cursor;
END;

FUNCTION getPhoto RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT * FROM photo;
        RETURN v_cursor;
END;

FUNCTION getRating RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT * FROM rating;
        RETURN v_cursor;
END;

FUNCTION getMatch RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT * FROM match;
        RETURN v_cursor;
END;

FUNCTION getParameters RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT * FROM parameters;
        RETURN v_cursor;
END;

END adminAdoptionMatch;