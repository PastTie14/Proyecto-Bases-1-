CREATE OR REPLACE TRIGGER beforeUpdateUserXBlackList
BEFORE INSERT OR UPDATE
ON user_x_black_list
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'User_X_Black_List', 'reason', 'empty', :new.reason);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'User_X_Black_List', 'id_user', 'empty', :new.id_user);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'User_X_Black_List', 'id_report', 'empty', :new.id_report);

    ELSE
        IF :old.reason <> :new.reason THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'User_X_Black_List', 'reason', :old.reason, :new.reason);
        END IF;

        IF :old.id_user <> :new.id_user THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'User_X_Black_List', 'id_user', :old.id_user, :new.id_user);
        END IF;

        IF :old.id_report <> :new.id_report THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'User_X_Black_List', 'id_report', :old.id_report, :new.id_report);
        END IF;
    END IF;
END beforeUpdateUserXBlackList;