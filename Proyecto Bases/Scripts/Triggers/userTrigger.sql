CREATE OR REPLACE TRIGGER beforeUpdateUser
BEFORE INSERT OR UPDATE
ON "user"
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'User', 'id_user', 'empty', :new.id_user);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'User', 'email', 'empty', :new.email);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'User', 'password', 'empty', :new."password");

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'User', 'createdBy', 'empty', :new.createdBy);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'User', 'createdAt', 'empty', :new.createdAt);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'User', 'modifiedBy', 'empty', :new.modifiedBy);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'User', 'modifiedAt', 'empty', :new.modifiedAt);

    ELSE
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

        IF :old.createdBy <> :new.createdBy THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'User', 'createdBy', :old.createdBy, :new.createdBy);
        END IF;

        IF :old.createdAt <> :new.createdAt THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'User', 'createdAt', :old.createdAt, :new.createdAt);
        END IF;

        IF :old.modifiedBy <> :new.modifiedBy THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'User', 'modifiedBy', :old.modifiedBy, :new.modifiedBy);
        END IF;

        IF :old.modifiedAt <> :new.modifiedAt THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'User', 'modifiedAt', :old.modifiedAt, :new.modifiedAt);
        END IF;
    END IF;
END beforeUpdateUser;