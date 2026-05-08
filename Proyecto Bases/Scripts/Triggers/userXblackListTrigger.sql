CREATE OR REPLACE TRIGGER beforeUpdateUserXBlackList
BEFORE INSERT OR UPDATE
ON user_x_black_list
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        :new.createdBY := USER;
        :new.createdAt := SYSTIMESTAMP;

    ELSE
        :new.modifiedBY := USER;
        :new.modifiedAt := SYSTIMESTAMP;
    END IF;
END beforeUpdateUserXBlackList;