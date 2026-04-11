CREATE OR REPLACE PROCEDURE insertTreatment(id_treatment IN NUMBER, "name" IN VARCHAR2,
                                            dose IN VARCHAR2)

AS 
BEGIN
    INSERT INTO treatment
    VALUES(id_treatment, "name", dose);
    COMMIT;
END insertTreatment;