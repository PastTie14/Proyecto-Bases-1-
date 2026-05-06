CREATE OR REPLACE TRIGGER beforeUpdateBlackList
BEFORE INSERT OR UPDATE
ON black_list
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Black_List', 'id_report', 'empty', :new.id_report);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Black_List', 'id_user', 'empty', :new.id_user);

    ELSE
        IF :old.id_user <> :new.id_user THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Black_List', 'id_user', :old.id_user, :new.id_user);
        END IF;
    END IF;
END beforeUpdateBlackList;