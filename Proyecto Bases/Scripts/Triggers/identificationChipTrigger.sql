CREATE OR REPLACE TRIGGER beforeUpdateIdentificationChip
BEFORE INSERT OR UPDATE
ON identification_chip
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Identification_Chip', 'id_chip', 'empty', :new.id_chip);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Identification_Chip', 'chip_number', 'empty', :new.chip_number);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Identification_Chip', 'registration_date', 'empty', :new.registration_date);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Identification_Chip', 'id_pet', 'empty', :new.id_pet);

    ELSE
        IF :old.chip_number <> :new.chip_number THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Identification_Chip', 'chip_number', :old.chip_number, :new.chip_number);
        END IF;

        IF :old.registration_date <> :new.registration_date THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Identification_Chip', 'registration_date', :old.registration_date, :new.registration_date);
        END IF;

        IF :old.id_pet <> :new.id_pet THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Identification_Chip', 'id_pet', :old.id_pet, :new.id_pet);
        END IF;
    END IF;
END beforeUpdateIdentificationChip;