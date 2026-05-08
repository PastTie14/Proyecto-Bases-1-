CREATE OR REPLACE TRIGGER beforeUpdatePetExtraInfo
BEFORE INSERT OR UPDATE
ON pet_extra_info
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        :new.createdBY := USER;
        :new.createdAt := SYSTIMESTAMP;
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Pet_Extra_Info', 'id_current_status', 'empty', :new.id_current_status);

    ELSE
        :new.modifiedBY := USER;
        :new.modifiedAt := SYSTIMESTAMP;
        IF :old.id_current_status <> :new.id_current_status THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Pet_Extra_Info', 'id_current_status', :old.id_current_status, :new.id_current_status);
        END IF;
    END IF;
END beforeUpdatePetExtraInfo;