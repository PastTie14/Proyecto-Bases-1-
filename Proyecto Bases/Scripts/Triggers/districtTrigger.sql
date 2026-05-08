CREATE OR REPLACE TRIGGER beforeUpdateDistrict
BEFORE INSERT OR UPDATE
ON district
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        :new.createdBY := USER;
        :new.createdAt := SYSTIMESTAMP;
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'District', 'name', 'empty', :new."name");

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                        previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'District', 'id_canton', 'empty', :new.id_canton);

    ELSE
        :new.modifiedBY := USER;
        :new.modifiedAt := SYSTIMESTAMP;
        IF :old."name" <> :new."name" THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                            previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'District', 'name', :old."name", :new."name");
        END IF;

        IF :old.id_canton <> :new.id_canton THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName,
                            previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'District', 'id_canton', :old.id_canton, :new.id_canton);
        END IF;
    END IF;
END beforeUpdateDistrict;