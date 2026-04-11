CREATE OR REPLACE PROCEDURE insertRating(id_rating IN NUMBER, score IN NUMBER, 
                                        id_user IN NUMBER, id_adopter IN NUMBER)

AS 
BEGIN
    INSERT INTO rating
    VALUES(id_rating, score, id_user, id_adopter);
    COMMIT;
END insertRating;