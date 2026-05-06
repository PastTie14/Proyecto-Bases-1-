CREATE OR REPLACE TRIGGER beforeUpdateRace
BEFORE INSERT OR UPDATE
ON race
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Race', 'id_race', 'empty', :new.id_race);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Race', 'name', 'empty', :new."name");

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Race', 'id_pet_type', 'empty', :new.id_pet_type);

    ELSE
        IF :old."name" <> :new."name" THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                            previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Race', 'name', :old."name", :new."name");
        END IF;

        IF :old.id_pet_type <> :new.id_pet_type THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                            previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Race', 'id_pet_type', :old.id_pet_type, :new.id_pet_type);
        END IF;
    END IF;
END beforeUpdateRace;