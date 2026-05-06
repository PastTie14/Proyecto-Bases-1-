CREATE OR REPLACE TRIGGER beforeUpdateProvince
BEFORE INSERT OR UPDATE
ON province
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Province', 'id_province', 'empty', :new.id_province);
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Province', 'name', 'empty', :new."name");
    ELSE
        IF :old."name" <> :new."name" THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                            previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Province', 'name', :old."name", :new."name");
        END IF;
    END IF;
END beforeUpdateProvince;