CREATE OR REPLACE TRIGGER beforeUpdateMedicSheet
BEFORE INSERT OR UPDATE
ON medic_sheet
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Medic_Sheet', 'id_medic_sheet', 'empty', :new.id_medic_sheet);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Medic_Sheet', 'abandonment_description', 'empty', :new.abandonment_description);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Medic_Sheet', 'id_veterinarian', 'empty', :new.id_veterinarian);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Medic_Sheet', 'id_pet_extra_info', 'empty', :new.id_pet_extra_info);

    ELSE
        IF :old.abandonment_description <> :new.abandonment_description THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Medic_Sheet', 'abandonment_description', :old.abandonment_description, :new.abandonment_description);
        END IF;

        IF :old.id_veterinarian <> :new.id_veterinarian THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Medic_Sheet', 'id_veterinarian', :old.id_veterinarian, :new.id_veterinarian);
        END IF;

        IF :old.id_pet_extra_info <> :new.id_pet_extra_info THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Medic_Sheet', 'id_pet_extra_info', :old.id_pet_extra_info, :new.id_pet_extra_info);
        END IF;
    END IF;
END beforeUpdateMedicSheet;