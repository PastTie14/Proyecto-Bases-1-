CREATE OR REPLACE PROCEDURE insertProvince(id_province IN NUMBER, "name" VARCHAR2)

AS 
BEGIN
    INSERT INTO province
    VALUES(id_province,"name");
    COMMIT;
END insertProvince;