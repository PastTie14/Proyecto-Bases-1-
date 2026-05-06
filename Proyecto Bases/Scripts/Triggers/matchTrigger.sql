CREATE OR REPLACE TRIGGER beforeUpdateMatch
BEFORE INSERT OR UPDATE
ON match
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Match', 'id_match', 'empty', :new.id_match);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Match', 'match_date', 'empty', :new.match_date);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Match', 'similarity_percentage', 'empty', :new.similarity_percentage);

    ELSE
        IF :old.match_date <> :new.match_date THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Match', 'match_date', :old.match_date, :new.match_date);
        END IF;

        IF :old.similarity_percentage <> :new.similarity_percentage THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Match', 'similarity_percentage', :old.similarity_percentage, :new.similarity_percentage);
        END IF;
    END IF;
END beforeUpdateMatch;