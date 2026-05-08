CREATE OR REPLACE TRIGGER beforeUpdateRating
BEFORE INSERT OR UPDATE
ON rating
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        :new.createdBY := USER;
        :new.createdAt := SYSTIMESTAMP;
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Rating', 'score', 'empty', :new.score);

    ELSE
        :new.modifiedBY := USER;
        :new.modifiedAt := SYSTIMESTAMP;
        IF :old.score <> :new.score THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Rating', 'score', :old.score, :new.score);
        END IF;
    END IF;
END beforeUpdateRating;