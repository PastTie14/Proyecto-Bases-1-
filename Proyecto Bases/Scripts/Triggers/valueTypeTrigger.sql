CREATE OR REPLACE TRIGGER beforeUpdateValueType
BEFORE INSERT OR UPDATE
ON value_type
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Value_Type', 'id_value_type', 'empty', :new.id_value_type);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Value_Type', 'type', 'empty', :new."type");

    ELSE
        IF :old."type" <> :new."type" THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                            previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Value_Type', 'type', :old."type", :new."type");
        END IF;
    END IF;
END beforeUpdateValueType;