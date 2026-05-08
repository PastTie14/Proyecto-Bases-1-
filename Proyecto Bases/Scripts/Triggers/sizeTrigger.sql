CREATE OR REPLACE TRIGGER beforeUpdateSize
BEFORE INSERT OR UPDATE
ON "size"
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        :new.createdBY := USER;
        :new.createdAt := SYSTIMESTAMP;
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Size', 'name', 'empty', :new."name");

    ELSE
        :new.modifiedBY := USER;
        :new.modifiedAt := SYSTIMESTAMP;
        IF :old."name" <> :new."name" THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                            previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Size', 'name', :old."name", :new."name");
        END IF;
    END IF;
END beforeUpdateSize;