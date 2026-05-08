CREATE OR REPLACE TRIGGER beforeUpdatePhoto
BEFORE INSERT OR UPDATE
ON photo
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        :new.createdBY := USER;
        :new.createdAt := SYSTIMESTAMP;

    ELSE
        :new.modifiedBY := USER;
        :new.modifiedAt := SYSTIMESTAMP;
    END IF;
END beforeUpdatePhoto;