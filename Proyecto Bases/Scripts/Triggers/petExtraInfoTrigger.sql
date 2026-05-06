CREATE OR REPLACE TRIGGER beforeUpdatePetExtraInfo
BEFORE INSERT OR UPDATE
ON pet_extra_info
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Pet_Extra_Info', 'id_pet_extra_info', 'empty', :new.id_pet_extra_info);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Pet_Extra_Info', 'size', 'empty', :new."size");

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Pet_Extra_Info', 'before_picture', 'empty', :new.before_picture);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Pet_Extra_Info', 'after_picture', 'empty', :new.after_picture);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Pet_Extra_Info', 'id_pet', 'empty', :new.id_pet);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Pet_Extra_Info', 'id_current_status', 'empty', :new.id_current_status);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Pet_Extra_Info', 'id_energy_level', 'empty', :new.id_energy_level);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Pet_Extra_Info', 'id_training_ease', 'empty', :new.id_training_ease);

    ELSE
        IF :old."size" <> :new."size" THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Pet_Extra_Info', 'size', :old."size", :new."size");
        END IF;

        IF :old.before_picture <> :new.before_picture THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Pet_Extra_Info', 'before_picture', :old.before_picture, :new.before_picture);
        END IF;

        IF :old.after_picture <> :new.after_picture THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Pet_Extra_Info', 'after_picture', :old.after_picture, :new.after_picture);
        END IF;

        IF :old.id_pet <> :new.id_pet THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Pet_Extra_Info', 'id_pet', :old.id_pet, :new.id_pet);
        END IF;

        IF :old.id_current_status <> :new.id_current_status THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Pet_Extra_Info', 'id_current_status', :old.id_current_status, :new.id_current_status);
        END IF;

        IF :old.id_energy_level <> :new.id_energy_level THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Pet_Extra_Info', 'id_energy_level', :old.id_energy_level, :new.id_energy_level);
        END IF;

        IF :old.id_training_ease <> :new.id_training_ease THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Pet_Extra_Info', 'id_training_ease', :old.id_training_ease, :new.id_training_ease);
        END IF;
    END IF;
END beforeUpdatePetExtraInfo;