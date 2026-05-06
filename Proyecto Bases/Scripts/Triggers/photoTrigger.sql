CREATE OR REPLACE TRIGGER beforeUpdatePhoto
BEFORE INSERT OR UPDATE
ON photo
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Photo', 'id_photo', 'empty', :new.id_photo);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Photo', 'date', 'empty', :new."date");

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Photo', 'photo_dir', 'empty', :new.photo_dir);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Photo', 'id_user', 'empty', :new.id_user);

    ELSE
        IF :old."date" <> :new."date" THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Photo', 'date', :old."date", :new."date");
        END IF;

        IF :old.photo_dir <> :new.photo_dir THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Photo', 'photo_dir', :old.photo_dir, :new.photo_dir);
        END IF;

        IF :old.id_user <> :new.id_user THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Photo', 'id_user', :old.id_user, :new.id_user);
        END IF;
    END IF;
END beforeUpdatePhoto;