CREATE OR REPLACE TRIGGER beforeUpdateAdoptionForm
BEFORE INSERT OR UPDATE
ON adoption_form
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        :new.createdBY := USER;
        :new.createdAt := SYSTIMESTAMP;

    ELSE
        :new.modifiedBY := USER;
        :new.modifiedAt := SYSTIMESTAMP;
    END IF;
END beforeUpdateAdoptionForm;