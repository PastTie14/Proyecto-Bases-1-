CREATE OR REPLACE TRIGGER beforeUpdateCribHouse
BEFORE INSERT OR UPDATE
ON crib_house
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Crib_House', 'id_user', 'empty', :new.id_user);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Crib_House', 'name', 'empty', :new."name");

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Crib_House', 'requires_donations', 'empty', :new.requires_donations);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Crib_House', 'accepted_size', 'empty', :new.accepted_size);

    ELSE
        IF :old."name" <> :new."name" THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Crib_House', 'name', :old."name", :new."name");
        END IF;

        IF :old.requires_donations <> :new.requires_donations THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Crib_House', 'requires_donations', :old.requires_donations, :new.requires_donations);
        END IF;

        IF :old.accepted_size <> :new.accepted_size THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Crib_House', 'accepted_size', :old.accepted_size, :new.accepted_size);
        END IF;
    END IF;
END beforeUpdateCribHouse;