CREATE OR REPLACE TRIGGER beforeUpdateEnergyLevel
BEFORE INSERT OR UPDATE
ON energy_level
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Energy_Level', 'id_energy_level', 'empty', :new.id_energy_level);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Energy_Level', 'name', 'empty', :new."name");

    ELSE
        IF :old."name" <> :new."name" THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Energy_Level', 'name', :old."name", :new."name");
        END IF;
    END IF;
END beforeUpdateEnergyLevel;