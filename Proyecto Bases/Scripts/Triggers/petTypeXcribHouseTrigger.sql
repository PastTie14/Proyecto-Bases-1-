CREATE OR REPLACE TRIGGER beforeUpdatePetTypeXCribHouse
BEFORE INSERT OR UPDATE
ON pet_type_x_crib_house
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        :new.createdBY := USER;
        :new.createdAt := SYSTIMESTAMP;

    ELSE
        :new.modifiedBY := USER;
        :new.modifiedAt := SYSTIMESTAMP;
    END IF;
END beforeUpdatePetTypeXCribHouse;