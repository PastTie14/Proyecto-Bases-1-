CREATE OR REPLACE PROCEDURE insertCurrentStatus(id_currentStatus IN NUMBER, status_type IN VARCHAR2)

AS 
BEGIN
    INSERT INTO current_status
    VALUES(id_currentStatus, status_type);
    COMMIT;
END insertCurrentStatus;