CREATE OR REPLACE PACKAGE BODY adminAdoptionMatch AS

PROCEDURE insertAdoptionForm(id_adoption IN NUMBER, notes VARCHAR2,
                                        adoption_date IN DATE, "reference" IN VARCHAR2,
                                        id_adopter IN NUMBER, id_pet IN NUMBER)

IS 
BEGIN
    INSERT INTO adoption_form
    VALUES(id_adoption, notes, adoption_date, "reference", id_adopter, id_pet);
    COMMIT;
END insertAdoptionForm;

--=======================================================================================

PROCEDURE insertPhoto(id_photo IN NUMBER, "date" IN DATE, 
                                    photo_dir IN VARCHAR2, id_adopter IN NUMBER)

IS 
BEGIN
    INSERT INTO photo
    VALUES(id_photo, "date", photo_dir, id_adopter);
    COMMIT;
END insertPhoto;

--=======================================================================================

PROCEDURE insertRating(id_rating IN NUMBER, score IN NUMBER, 
                                        id_user IN NUMBER, id_adopter IN NUMBER)

IS 
BEGIN
    INSERT INTO rating
    VALUES(id_rating, score, id_user, id_adopter);
    COMMIT;
END insertRating;

--=======================================================================================

PROCEDURE insertMatch(id_match IN NUMBER, match_date IN DATE,
                                        similarity_percentage IN NUMBER)

IS 
BEGIN
    INSERT INTO match
    VALUES(id_match, match_date, similarity_percentage);
    COMMIT;
END insertMatch;

--=======================================================================================

PROCEDURE insertParameters(id_parameter IN NUMBER, "value" IN VARCHAR2,
                                            id_match IN NUMBER, id_value_type IN NUMBER)

IS 
BEGIN
    INSERT INTO parameters
    VALUES(id_parameter, "value", id_match, id_value_type);
    COMMIT;
END insertParameters;

END adminAdoptionMatch;