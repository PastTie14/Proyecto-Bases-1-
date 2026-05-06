CREATE OR REPLACE TRIGGER beforeUpdateSize
BEFORE INSERT OR UPDATE
ON "size"
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Size', 'id_size', 'empty', :new.id_size);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Size', 'name', 'empty', :new."name");

    ELSE
        IF :old."name" <> :new."name" THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                            previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Size', 'name', :old."name", :new."name");
        END IF;
    END IF;
END beforeUpdateSize;