CREATE OR REPLACE TRIGGER beforeUpdateDisease
BEFORE INSERT OR UPDATE
ON disease
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Disease', 'id_disease', 'empty', :new.id_disease);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Disease', 'name', 'empty', :new."name");

    ELSE
        IF :old."name" <> :new."name" THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Disease', 'name', :old."name", :new."name");
        END IF;
    END IF;
END beforeUpdateDisease;