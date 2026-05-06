CREATE OR REPLACE TRIGGER beforeUpdateDxMedicSheet
BEFORE INSERT OR UPDATE
ON disease_x_medic_sheet
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Disease_X_Medic_Sheet', 'id_disease', 'empty', :new.id_disease);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Disease_X_Medic_Sheet', 'id_medic_sheet', 'empty', :new.id_medic_sheet);

    ELSE
        IF :old.id_disease <> :new.id_disease THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Disease_X_Medic_Sheet', 'id_disease', :old.id_disease, :new.id_disease);
        END IF;

        IF :old.id_medic_sheet <> :new.id_medic_sheet THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Disease_X_Medic_Sheet', 'id_medic_sheet', :old.id_medic_sheet, :new.id_medic_sheet);
        END IF;
    END IF;
END beforeUpdateDxMedicSheet;