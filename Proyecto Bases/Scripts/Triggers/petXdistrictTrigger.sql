CREATE OR REPLACE TRIGGER beforeUpdatePetXDistrict
BEFORE INSERT OR UPDATE
ON pet_x_district
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        :new.createdBY := USER;
        :new.createdAt := SYSTIMESTAMP;

    ELSE
        :new.modifiedBY := USER;
        :new.modifiedAt := SYSTIMESTAMP;
    END IF;
END beforeUpdatePetXDistrict;