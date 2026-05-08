CREATE OR REPLACE TRIGGER beforeUpdateValueType
BEFORE INSERT OR UPDATE
ON value_type
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        :new.createdBY := USER;
        :new.createdAt := SYSTIMESTAMP;

    ELSE
        :new.modifiedBY := USER;
        :new.modifiedAt := SYSTIMESTAMP;
    END IF;
END beforeUpdateValueType;