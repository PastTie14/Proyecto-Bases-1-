CREATE OR REPLACE TRIGGER beforeUpdateDxMedicSheet
BEFORE INSERT OR UPDATE
ON disease_x_medic_sheet
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        :new.createdBY := USER;
        :new.createdAt := SYSTIMESTAMP;

    ELSE
        :new.modifiedBY := USER;
        :new.modifiedAt := SYSTIMESTAMP;
    END IF;
END beforeUpdateDxMedicSheet;