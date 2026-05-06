CREATE OR REPLACE TRIGGER beforeUpdateBounty
BEFORE INSERT OR UPDATE
ON bounty
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Bounty', 'id_bounty', 'empty', :new.id_bounty);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Bounty', 'amount', 'empty', :new.amount);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Bounty', 'id_pet_extra_info', 'empty', :new.id_pet_extra_info);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Bounty', 'id_currency', 'empty', :new.id_currency);

    ELSE
        IF :old.amount <> :new.amount THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Bounty', 'amount', :old.amount, :new.amount);
        END IF;

        IF :old.id_pet_extra_info <> :new.id_pet_extra_info THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Bounty', 'id_pet_extra_info', :old.id_pet_extra_info, :new.id_pet_extra_info);
        END IF;

        IF :old.id_currency <> :new.id_currency THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Bounty', 'id_currency', :old.id_currency, :new.id_currency);
        END IF;
    END IF;
END beforeUpdateBounty;