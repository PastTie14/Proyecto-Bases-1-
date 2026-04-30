CREATE OR REPLACE TRIGGER beforeUpdatePetXColor
BEFORE INSERT OR UPDATE
ON pet_x_color
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Pet_X_Color', 'id_pet', 'empty', :new.id_pet);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Pet_X_Color', 'id_color', 'empty', :new.id_color);

    ELSE
        IF :old.id_pet <> :new.id_pet THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Pet_X_Color', 'id_pet', :old.id_pet, :new.id_pet);
        END IF;

        IF :old.id_color <> :new.id_color THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Pet_X_Color', 'id_color', :old.id_color, :new.id_color);
        END IF;
    END IF;
END beforeUpdatePetXColor;