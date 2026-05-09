CREATE OR REPLACE TRIGGER beforeUpdatePet
BEFORE INSERT OR UPDATE
ON pet
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        :new.createdBY := USER;
        :new.createdAt := SYSTIMESTAMP;
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Pet', 'id_status', 'empty', :new.id_status);
    ELSE
        :new.modifiedBY := USER;
        :new.modifiedAt := SYSTIMESTAMP;
    
        IF :old.id_status <> :new.id_status THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Pet', 'id_status', :old.id_status, :new.id_status);
        END IF;
    END IF;
END beforeUpdatePet;