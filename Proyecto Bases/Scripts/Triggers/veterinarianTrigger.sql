CREATE OR REPLACE TRIGGER beforeUpdateVeterinarian
BEFORE INSERT OR UPDATE
ON veterinarian
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Veterinarian', 'id_veterinarian', 'empty', :new.id_veterinarian);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Veterinarian', 'first_name', 'empty', :new.first_name);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Veterinarian', 'second_name', 'empty', :new.second_name);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Veterinarian', 'first_surname', 'empty', :new.first_surname);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Veterinarian', 'second_surname', 'empty', :new.second_surname);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Veterinarian', 'clinic_name', 'empty', :new.clinic_name);

    ELSE
        IF :old.first_name <> :new.first_name THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Veterinarian', 'first_name', :old.first_name, :new.first_name);
        END IF;

        IF :old.second_name <> :new.second_name THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Veterinarian', 'second_name', :old.second_name, :new.second_name);
        END IF;

        IF :old.first_surname <> :new.first_surname THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Veterinarian', 'first_surname', :old.first_surname, :new.first_surname);
        END IF;

        IF :old.second_surname <> :new.second_surname THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Veterinarian', 'second_surname', :old.second_surname, :new.second_surname);
        END IF;

        IF :old.clinic_name <> :new.clinic_name THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Veterinarian', 'clinic_name', :old.clinic_name, :new.clinic_name);
        END IF;
    END IF;
END beforeUpdateVeterinarian;