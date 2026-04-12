CREATE OR REPLACE PROCEDURE insertTrainingEase(id_trainingEase IN NUMBER, "name" IN VARCHAR2)

AS 
BEGIN
    INSERT INTO training_ease
    VALUES(id_trainingEase, "name", id_petExtraInfo);
    COMMIT;
END insertTrainingEase;