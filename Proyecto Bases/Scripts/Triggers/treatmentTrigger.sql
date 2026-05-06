CREATE OR REPLACE TRIGGER beforeUpdateTreatment
BEFORE INSERT OR UPDATE
ON treatment
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Treatment', 'id_treatment', 'empty', :new.id_treatment);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Treatment', 'name', 'empty', :new."name");

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Treatment', 'dose', 'empty', :new.dose);

    ELSE
        IF :old."name" <> :new."name" THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Treatment', 'name', :old."name", :new."name");
        END IF;

        IF :old.dose <> :new.dose THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Treatment', 'dose', :old.dose, :new.dose);
        END IF;
    END IF;
END beforeUpdateTreatment;