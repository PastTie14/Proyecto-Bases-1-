CREATE OR REPLACE PACKAGE BODY adminAdoptionMatch AS

--  ===============================================================
--  INSERT
--  ===============================================================

PROCEDURE insertAdoptionForm(
    pNotes        IN VARCHAR2,
    pAdoptionDate IN DATE,
    pReference    IN VARCHAR2,
    pIdAdopter    IN NUMBER,
    pIdPet        IN NUMBER
) IS
BEGIN
    INSERT INTO adoption_form (
        id_adoption, notes, adoption_date, "reference", id_adopter, id_pet
    ) VALUES (
        s_adoption.NEXTVAL,   
        pNotes,
        pAdoptionDate,
        pReference,
        pIdAdopter,
        pIdPet
    );
    COMMIT;
END insertAdoptionForm;

--  ===============================================================

PROCEDURE insertPhoto(
    pIdPhoto   IN NUMBER,
    pDate      IN DATE,
    pPhotoDir  IN VARCHAR2,
    pIdAdopter IN NUMBER
) IS
BEGIN
    INSERT INTO photo (id_photo, "date", photo_dir, id_user)
    VALUES (pIdPhoto, pDate, pPhotoDir, pIdAdopter);
    COMMIT;
END insertPhoto;

--  ===============================================================

PROCEDURE insertRating(
    pIdRating  IN NUMBER,
    pScore     IN NUMBER,
    pIdUser    IN NUMBER,
    pIdAdopter IN NUMBER
) IS
BEGIN
    INSERT INTO rating (id_rating, score, id_user, id_adopter)
    VALUES (s_rating.NEXTVAL, pScore, pIdUser, pIdAdopter);
    COMMIT;
END insertRating;

--  ===============================================================

PROCEDURE insertMatch(
    pIdMatch              IN NUMBER,
    pMatchDate            IN DATE
) IS
BEGIN
    INSERT INTO match (id_match, match_date)
    VALUES (s_match.NEXTVAL, pMatchDate);
    COMMIT;
END insertMatch;

--  ===============================================================

PROCEDURE insertParameters(
    pIdParameter IN NUMBER,
    pValue       IN VARCHAR2,
    pIdMatch     IN NUMBER,
    pIdValueType IN NUMBER
) IS
BEGIN
    INSERT INTO parameters (id_parameter, "value", id_match, id_value_type)
    VALUES (s_parameter.NEXTVAL, pValue, pIdMatch, pIdValueType);
    COMMIT;
END insertParameters;


--  ===============================================================
--  UPDATE
--  ===============================================================
PROCEDURE updateAdoptionForm(
    pIdAdoption   IN NUMBER,
    pNotes        IN VARCHAR2,
    pAdoptionDate IN DATE,
    pReference    IN VARCHAR2
) IS
BEGIN
    UPDATE adoption_form
    SET    notes         = pNotes,
           adoption_date = pAdoptionDate,
           "reference"   = pReference
    WHERE  id_adoption   = pIdAdoption;
    COMMIT;
END updateAdoptionForm;

--  ===============================================================

PROCEDURE updatePhoto(
    pIdPhoto   IN NUMBER,
    pDate      IN DATE,
    pPhotoDir  IN VARCHAR2,
    pIdAdopter IN NUMBER
) IS
BEGIN
    UPDATE photo
    SET    "date"    = pDate,
           photo_dir = pPhotoDir
    WHERE  id_photo  = pIdPhoto
    AND    id_user   = pIdAdopter;
    COMMIT;
END updatePhoto;

--  ===============================================================

PROCEDURE updateRating(
    pIdRating  IN NUMBER,
    pScore     IN NUMBER,
    pIdUser    IN NUMBER,
    pIdAdopter IN NUMBER
) IS
BEGIN
    UPDATE rating
    SET    score      = pScore
    WHERE  id_rating  = pIdRating
    AND    id_user    = pIdUser
    AND    id_adopter = pIdAdopter;
    COMMIT;
END updateRating;

--  ===============================================================

PROCEDURE updateMatch(
    pIdMatch              IN NUMBER,
    pMatchDate            IN DATE
) IS
BEGIN
    UPDATE match
    SET    match_date             = pMatchDate
    WHERE  id_match               = pIdMatch;
    COMMIT;
END updateMatch;

--  ===============================================================

PROCEDURE updateParameters(
    pIdParameter IN NUMBER,
    pValue       IN VARCHAR2,
    pIdMatch     IN NUMBER,
    pIdValueType IN NUMBER
) IS
BEGIN
    UPDATE parameters
    SET    "value"       = pValue,
           id_match      = pIdMatch,
           id_value_type = pIdValueType
    WHERE  id_parameter  = pIdParameter;
    COMMIT;
END updateParameters;


--  ===============================================================
--  GET
--  ===============================================================

FUNCTION getAdoptionForm RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
        SELECT af.id_adoption,
               af.notes,
               af.adoption_date,
               af."reference",
               af.id_adopter,
               af.id_pet
        FROM   adoption_form af
        ORDER  BY af.adoption_date DESC;
    RETURN v_cursor;
END getAdoptionForm;

-- ?????????????????????????????????????????????????????????????

FUNCTION getAdoptionFormById(pIdAdoption IN NUMBER) RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
        SELECT af.id_adoption,
               af.notes,
               af.adoption_date,
               af."reference",
               af.id_adopter,
               af.id_pet
        FROM   adoption_form af
        WHERE  af.id_adoption = pIdAdoption;
    RETURN v_cursor;
END getAdoptionFormById;

--  ===============================================================

FUNCTION getAdoptionsByPet(pIdPet IN NUMBER) RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
        SELECT af.id_adoption,
               af.notes,
               af.adoption_date,
               af."reference",
               af.id_adopter,
               af.id_pet
        FROM   adoption_form af
        WHERE  af.id_pet = pIdPet
        ORDER  BY af.adoption_date DESC;
    RETURN v_cursor;
END getAdoptionsByPet;

--  ===============================================================

FUNCTION getAdoptionsByAdopter(pIdAdopter IN NUMBER) RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
        SELECT af.id_adoption,
               af.notes,
               af.adoption_date,
               af."reference",
               af.id_adopter,
               af.id_pet
        FROM   adoption_form af
        WHERE  af.id_adopter = pIdAdopter
        ORDER  BY af.adoption_date DESC;
    RETURN v_cursor;
END getAdoptionsByAdopter;

--  ===============================================================

FUNCTION getPhoto RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM photo;
    RETURN v_cursor;
END getPhoto;

FUNCTION getRating RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM rating;
    RETURN v_cursor;
END getRating;

FUNCTION getMatch RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM match;
    RETURN v_cursor;
END getMatch;

FUNCTION getParameters RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM parameters;
    RETURN v_cursor;
END getParameters;


--  ===============================================================
--  DELETE
-- ===============================================================

PROCEDURE deleteAdoptionForm(pIdAdoption IN NUMBER) IS
BEGIN
    DELETE FROM adoption_form
    WHERE  id_adoption = pIdAdoption;
    COMMIT;
END deleteAdoptionForm;

--  ===============================================================

PROCEDURE deletePhoto(pIdPhoto IN NUMBER) IS
BEGIN
    DELETE FROM photo WHERE id_photo = pIdPhoto;
    COMMIT;
END deletePhoto;

PROCEDURE deleteRating(pIdRating IN NUMBER) IS
BEGIN
    DELETE FROM rating WHERE id_rating = pIdRating;
    COMMIT;
END deleteRating;


END adminAdoptionMatch;
