CREATE OR REPLACE TRIGGER beforeUpdateIdentificationChip
BEFORE INSERT OR UPDATE
ON identification_chip
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        :new.createdBY := USER;
        :new.createdAt := SYSTIMESTAMP;
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Identification_Chip', 'chip_number', 'empty', :new.chip_number);
    ELSE
        :new.modifiedBY := USER;
        :new.modifiedAt := SYSTIMESTAMP;
        IF :old.chip_number <> :new.chip_number THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Identification_Chip', 'chip_number', :old.chip_number, :new.chip_number);
        END IF;
    END IF;
END beforeUpdateIdentificationChip;