CREATE OR REPLACE TRIGGER beforeUpdateStatus
BEFORE INSERT OR UPDATE
ON status
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Status', 'id_status', 'empty', :new.id_status);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Status', 'status_type', 'empty', :new.status_type);

    ELSE
        IF :old.status_type <> :new.status_type THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                            previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Status', 'status_type', :old.status_type, :new.status_type);
        END IF;
    END IF;
END beforeUpdateStatus;