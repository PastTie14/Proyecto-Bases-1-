CREATE OR REPLACE PROCEDURE insertCurrentStatus(id_currentStatus IN NUMBER, status_type IN VARCHAR2, 
                                            id_petExtraInfo IN NUMBER)

AS 
BEGIN
    INSERT INTO current_status
    VALUES(id_currentStatus, status_type, id_petExtraInfo);
    COMMIT;
END insertCurrentStatus;