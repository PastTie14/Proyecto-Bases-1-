CREATE OR REPLACE TRIGGER beforeUpdatePhoneNumber
BEFORE INSERT OR UPDATE
ON phone_number
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Phone_Number', 'id_phone', 'empty', :new.id_phone);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Phone_Number', 'number', 'empty', :new."number");

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Phone_Number', 'id_user', 'empty', :new.id_user);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Phone_Number', 'id_pet', 'empty', :new.id_pet);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Phone_Number', 'id_veterinarian', 'empty', :new.id_veterinarian);

    ELSE
        IF :old."number" <> :new."number" THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Phone_Number', 'number', :old."number", :new."number");
        END IF;

        IF :old.id_user <> :new.id_user THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Phone_Number', 'id_user', :old.id_user, :new.id_user);
        END IF;

        IF :old.id_pet <> :new.id_pet THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Phone_Number', 'id_pet', :old.id_pet, :new.id_pet);
        END IF;

        IF :old.id_veterinarian <> :new.id_veterinarian THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Phone_Number', 'id_veterinarian', :old.id_veterinarian, :new.id_veterinarian);
        END IF;
    END IF;
END beforeUpdatePhoneNumber;