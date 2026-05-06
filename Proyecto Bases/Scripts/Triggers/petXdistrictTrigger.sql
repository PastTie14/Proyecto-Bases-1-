CREATE OR REPLACE TRIGGER beforeUpdatePetXDistrict
BEFORE INSERT OR UPDATE
ON pet_x_district
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Pet_X_District', 'id_pet', 'empty', :new.id_pet);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Pet_X_District', 'id_district', 'empty', :new.id_district);

    ELSE
        IF :old.id_pet <> :new.id_pet THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Pet_X_District', 'id_pet', :old.id_pet, :new.id_pet);
        END IF;

        IF :old.id_district <> :new.id_district THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Pet_X_District', 'id_district', :old.id_district, :new.id_district);
        END IF;
    END IF;
END beforeUpdatePetXDistrict;