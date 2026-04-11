CREATE OR REPLACE PROCEDURE insertTreatmentXDisease(id_treatment IN NUMBER, id_disease IN NUMBER)

AS 
BEGIN
    INSERT INTO treatment_x_disease
    VALUES(id_treatment, id_disease);
    COMMIT;
END insertTreatmentXDisease;