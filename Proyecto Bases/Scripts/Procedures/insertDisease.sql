CREATE OR REPLACE PROCEDURE insertDisease(id_disease IN NUMBER, "name" IN VARCHAR2)

AS 
BEGIN
    INSERT INTO disease
    VALUES(id_disease, "name");
    COMMIT;
END insertDisease;