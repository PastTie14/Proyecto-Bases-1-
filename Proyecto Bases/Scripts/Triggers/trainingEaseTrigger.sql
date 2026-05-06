CREATE OR REPLACE TRIGGER beforeUpdateTrainingEase
BEFORE INSERT OR UPDATE
ON training_ease
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Training_Ease', 'id_training_ease', 'empty', :new.id_training_ease);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Training_Ease', 'name', 'empty', :new."name");

    ELSE
        IF :old."name" <> :new."name" THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Training_Ease', 'name', :old."name", :new."name");
        END IF;
    END IF;
END beforeUpdateTrainingEase;