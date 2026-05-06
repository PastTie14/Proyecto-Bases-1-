CREATE OR REPLACE TRIGGER beforeUpdatePetTypeXCribHouse
BEFORE INSERT OR UPDATE
ON pet_type_x_crib_house
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Pet_Type_X_Crib_House', 'id_pet_type', 'empty', :new.id_pet_type);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Pet_Type_X_Crib_House', 'id_crib_house', 'empty', :new.id_crib_house);

    ELSE
        IF :old.id_pet_type <> :new.id_pet_type THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Pet_Type_X_Crib_House', 'id_pet_type', :old.id_pet_type, :new.id_pet_type);
        END IF;

        IF :old.id_crib_house <> :new.id_crib_house THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Pet_Type_X_Crib_House', 'id_crib_house', :old.id_crib_house, :new.id_crib_house);
        END IF;
    END IF;
END beforeUpdatePetTypeXCribHouse;