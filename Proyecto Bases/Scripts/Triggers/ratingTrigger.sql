CREATE OR REPLACE TRIGGER beforeUpdateRating
BEFORE INSERT OR UPDATE
ON rating
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Rating', 'id_rating', 'empty', :new.id_rating);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Rating', 'score', 'empty', :new.score);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Rating', 'id_user', 'empty', :new.id_user);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Rating', 'id_adopter', 'empty', :new.id_adopter);

    ELSE
        IF :old.score <> :new.score THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Rating', 'score', :old.score, :new.score);
        END IF;

        IF :old.id_user <> :new.id_user THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Rating', 'id_user', :old.id_user, :new.id_user);
        END IF;

        IF :old.id_adopter <> :new.id_adopter THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Rating', 'id_adopter', :old.id_adopter, :new.id_adopter);
        END IF;
    END IF;
END beforeUpdateRating;