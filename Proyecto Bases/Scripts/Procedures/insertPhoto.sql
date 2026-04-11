CREATE OR REPLACE PROCEDURE insertPhoto(id_photo IN NUMBER, "date" IN DATE, 
                                    photo_dir IN VARCHAR2, id_adopter IN NUMBER)

AS 
BEGIN
    INSERT INTO photo
    VALUES(id_photo, "date", photo_dir, id_adopter);
    COMMIT;
END insertPhoto;