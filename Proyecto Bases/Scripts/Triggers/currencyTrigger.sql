CREATE OR REPLACE TRIGGER beforeUpdateCurrency
BEFORE INSERT OR UPDATE
ON currency
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Currency', 'id_currency', 'empty', :new.id_currency);
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Currency', 'name', 'empty', :new."name");
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Currency', 'acronym', 'empty', :new.acronym);
    ELSE
    IF :old."name" <> :new."name" THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Currency', 'name', :old."name", :new."name");
    END IF;
    IF :old.acronym <> :new.acronym THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Currency', 'acronym', :old.acronym, :new.acronym);
    END IF;
    END IF;
END beforeUpdateCurrency;