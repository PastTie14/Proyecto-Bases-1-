CREATE OR REPLACE PROCEDURE insertIdChip(id_chip IN NUMBER, chip_number IN VARCHAR2,
                                            registration_date IN DATE, id_pet IN NUMBER)

AS 
BEGIN
    INSERT INTO identification_chip
    VALUES(id_chip, chip_number, registration_date, id_pet);
    COMMIT;
END insertIdChip;