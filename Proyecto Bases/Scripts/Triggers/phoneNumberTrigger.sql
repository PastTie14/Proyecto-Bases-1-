CREATE OR REPLACE TRIGGER beforeUpdatePhoneNumber
BEFORE INSERT OR UPDATE
ON phone_number
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        :new.createdBY := USER;
        :new.createdAt := SYSTIMESTAMP;
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Phone_Number', 'number', 'empty', :new."number");
    ELSE
        :new.modifiedBY := USER;
        :new.modifiedAt := SYSTIMESTAMP;
        IF :old."number" <> :new."number" THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Phone_Number', 'number', :old."number", :new."number");
        END IF;
    END IF;
END beforeUpdatePhoneNumber;