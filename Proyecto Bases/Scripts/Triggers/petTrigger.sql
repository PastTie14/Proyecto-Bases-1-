CREATE OR REPLACE TRIGGER beforeUpdatePet
BEFORE INSERT OR UPDATE
ON pet
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Pet', 'id_pet', 'empty', :new.id_pet);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Pet', 'picture', 'empty', :new.picture);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Pet', 'first_name', 'empty', :new.first_name);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Pet', 'birth_date', 'empty', :new.birth_date);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Pet', 'date_lost', 'empty', :new.date_lost);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Pet', 'date_found', 'empty', :new.date_found);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Pet', 'email', 'empty', :new.email);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Pet', 'id_status', 'empty', :new.id_status);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Pet', 'id_pet_type', 'empty', :new.id_pet_type);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Pet', 'id_rescuer', 'empty', :new.id_rescuer);

    ELSE
        IF :old.picture <> :new.picture THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Pet', 'picture', :old.picture, :new.picture);
        END IF;

        IF :old.first_name <> :new.first_name THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Pet', 'first_name', :old.first_name, :new.first_name);
        END IF;

        IF :old.birth_date <> :new.birth_date THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Pet', 'birth_date', :old.birth_date, :new.birth_date);
        END IF;

        IF :old.date_lost <> :new.date_lost THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Pet', 'date_lost', :old.date_lost, :new.date_lost);
        END IF;

        IF :old.date_found <> :new.date_found THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Pet', 'date_found', :old.date_found, :new.date_found);
        END IF;

        IF :old.email <> :new.email THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Pet', 'email', :old.email, :new.email);
        END IF;

        IF :old.id_status <> :new.id_status THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Pet', 'id_status', :old.id_status, :new.id_status);
        END IF;

        IF :old.id_pet_type <> :new.id_pet_type THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Pet', 'id_pet_type', :old.id_pet_type, :new.id_pet_type);
        END IF;

        IF :old.id_rescuer <> :new.id_rescuer THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Pet', 'id_rescuer', :old.id_rescuer, :new.id_rescuer);
        END IF;
    END IF;
END beforeUpdatePet;