CREATE OR REPLACE TRIGGER beforeUpdateCurrentStatus
BEFORE INSERT OR UPDATE
ON current_status
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        :new.createdBY := USER;
        :new.createdAt := SYSTIMESTAMP;
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Current_Status', 'status_type', 'empty', :new.status_type);

    ELSE
        :new.modifiedBY := USER;
        :new.modifiedAt := SYSTIMESTAMP;
        IF :old.status_type <> :new.status_type THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Current_Status', 'status_type', :old.status_type, :new.status_type);
        END IF;
    END IF;
END beforeUpdateCurrentStatus;