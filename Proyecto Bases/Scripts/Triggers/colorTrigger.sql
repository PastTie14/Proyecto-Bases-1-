CREATE OR REPLACE TRIGGER beforeUpdateColor
BEFORE INSERT OR UPDATE
ON color
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Color', 'id_color', 'empty', :new.id_color);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Color', 'name', 'empty', :new."name");

    ELSE
        IF :old."name" <> :new."name" THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                            previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Color', 'name', :old."name", :new."name");
        END IF;
    END IF;
END beforeUpdateColor;