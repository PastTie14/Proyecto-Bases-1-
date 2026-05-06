CREATE OR REPLACE TRIGGER beforeUpdateTxDisease
BEFORE INSERT OR UPDATE
ON treatment_x_disease
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Treatment_X_Disease', 'id_treatment', 'empty', :new.id_treatment);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Treatment_X_Disease', 'id_disease', 'empty', :new.id_disease);

    ELSE
        IF :old.id_treatment <> :new.id_treatment THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Treatment_X_Disease', 'id_treatment', :old.id_treatment, :new.id_treatment);
        END IF;

        IF :old.id_disease <> :new.id_disease THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Treatment_X_Disease', 'id_disease', :old.id_disease, :new.id_disease);
        END IF;
    END IF;
END beforeUpdateTxDisease;