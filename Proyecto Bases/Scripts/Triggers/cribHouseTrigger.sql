CREATE OR REPLACE TRIGGER beforeUpdateCribHouse
BEFORE INSERT OR UPDATE
ON crib_house
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        :new.createdBY := USER;
        :new.createdAt := SYSTIMESTAMP;
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Crib_House', 'requires_donations', 'empty', :new.requires_donations);

    ELSE
        :new.modifiedBY := USER;
        :new.modifiedAt := SYSTIMESTAMP;
        IF :old.requires_donations <> :new.requires_donations THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Crib_House', 'requires_donations', :old.requires_donations, :new.requires_donations);
        END IF;
    END IF;
END beforeUpdateCribHouse;