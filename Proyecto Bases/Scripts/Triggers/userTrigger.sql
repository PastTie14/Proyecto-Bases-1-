CREATE OR REPLACE TRIGGER beforeUpdateUser
BEFORE INSERT OR UPDATE
ON "user"
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        :new.createdBY := USER;
        :new.createdAt := SYSTIMESTAMP;
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'User', 'email', 'empty', :new.email);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'User', 'password', 'empty', :new."password");
    ELSE
        :new.modifiedBY := USER;
        :new.modifiedAt := SYSTIMESTAMP;
        IF :old.email <> :new.email THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'User', 'email', :old.email, :new.email);
        END IF;

        IF :old."password" <> :new."password" THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'User', 'password', :old."password", :new."password");
        END IF;
    END IF;
END beforeUpdateUser;