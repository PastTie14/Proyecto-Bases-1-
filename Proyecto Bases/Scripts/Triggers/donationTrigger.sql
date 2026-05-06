CREATE OR REPLACE TRIGGER beforeUpdateDonation
BEFORE INSERT OR UPDATE
ON donation
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Donation', 'id_donation', 'empty', :new.id_donation);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Donation', 'amount', 'empty', :new.amount);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Donation', 'id_association', 'empty', :new.id_association);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Donation', 'id_currency', 'empty', :new.id_currency);

    ELSE
        IF :old.amount <> :new.amount THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Donation', 'amount', :old.amount, :new.amount);
        END IF;

        IF :old.id_association <> :new.id_association THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Donation', 'id_association', :old.id_association, :new.id_association);
        END IF;

        IF :old.id_currency <> :new.id_currency THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Donation', 'id_currency', :old.id_currency, :new.id_currency);
        END IF;
    END IF;
END beforeUpdateDonation;