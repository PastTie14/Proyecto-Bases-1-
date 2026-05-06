CREATE OR REPLACE TRIGGER beforeUpdateParameters
BEFORE INSERT OR UPDATE
ON parameters
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Parameters', 'id_parameter', 'empty', :new.id_parameter);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Parameters', 'value', 'empty', :new."value");

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Parameters', 'id_match', 'empty', :new.id_match);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Parameters', 'id_value_type', 'empty', :new.id_value_type);

    ELSE
        IF :old."value" <> :new."value" THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Parameters', 'value', :old."value", :new."value");
        END IF;

        IF :old.id_match <> :new.id_match THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Parameters', 'id_match', :old.id_match, :new.id_match);
        END IF;

        IF :old.id_value_type <> :new.id_value_type THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Parameters', 'id_value_type', :old.id_value_type, :new.id_value_type);
        END IF;
    END IF;
END beforeUpdateParameters;