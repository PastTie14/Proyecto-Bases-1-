CREATE OR REPLACE TRIGGER beforeUpdateDonationXUser
BEFORE INSERT OR UPDATE
ON donation_x_user
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Donation_X_User', 'id_user', 'empty', :new.id_user);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Donation_X_User', 'id_donation', 'empty', :new.id_donation);
    ELSE
        IF :old.id_user <> :new.id_user THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Donation_X_User', 'id_user', :old.id_user, :new.id_user);
        END IF;

        IF :old.id_donation <> :new.id_donation THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Donation_X_User', 'id_donation', :old.id_donation, :new.id_donation);
        END IF;
    END IF;
END beforeUpdateDonationXUser;