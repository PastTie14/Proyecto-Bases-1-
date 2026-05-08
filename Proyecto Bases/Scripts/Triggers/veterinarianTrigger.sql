CREATE OR REPLACE TRIGGER beforeUpdateVeterinarian
BEFORE INSERT OR UPDATE
ON veterinarian
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        :new.createdBY := USER;
        :new.createdAt := SYSTIMESTAMP;

    ELSE
        :new.modifiedBY := USER;
        :new.modifiedAt := SYSTIMESTAMP;
    END IF;
END beforeUpdateVeterinarian;