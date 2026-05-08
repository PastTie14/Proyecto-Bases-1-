CREATE OR REPLACE TRIGGER beforeUpdateCanton
BEFORE INSERT OR UPDATE
ON canton
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        :new.createdBY := USER;
        :new.createdAt := SYSTIMESTAMP;
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Canton', 'name', 'empty', :new."name");
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Canton', 'id_province', 'empty', :new.id_province);
    ELSE
        :new.modifiedBY := USER;
        :new.modifiedAt := SYSTIMESTAMP;
        IF :old."name" <> :new."name" THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                            previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Canton', 'name', :old."name", :new."name");
        END IF;
        IF :old.id_province <> :new.id_province THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                            previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Canton', 'id_province', :old.id_province, :new.id_province);
        END IF;
    END IF;
END beforeUpdateCanton;