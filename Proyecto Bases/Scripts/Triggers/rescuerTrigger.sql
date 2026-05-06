CREATE OR REPLACE TRIGGER beforeUpdateRescuer
BEFORE INSERT OR UPDATE
ON rescuer
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Rescuer', 'id_user', 'empty', :new.id_user);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Rescuer', 'first_name', 'empty', :new.first_name);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Rescuer', 'second_name', 'empty', :new.second_name);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Rescuer', 'first_surname', 'empty', :new.first_surname);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Rescuer', 'second_surname', 'empty', :new.second_surname);

    ELSE
        IF :old.first_name <> :new.first_name THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Rescuer', 'first_name', :old.first_name, :new.first_name);
        END IF;

        IF :old.second_name <> :new.second_name THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Rescuer', 'second_name', :old.second_name, :new.second_name);
        END IF;

        IF :old.first_surname <> :new.first_surname THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Rescuer', 'first_surname', :old.first_surname, :new.first_surname);
        END IF;

        IF :old.second_surname <> :new.second_surname THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Rescuer', 'second_surname', :old.second_surname, :new.second_surname);
        END IF;
    END IF;
END beforeUpdateRescuer;