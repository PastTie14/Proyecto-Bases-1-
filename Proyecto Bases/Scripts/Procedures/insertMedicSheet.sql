CREATE OR REPLACE PROCEDURE insertMedicSheet(id_medicSheet IN NUMBER, abandonmentDescription IN VARCHAR2,
                                            id_veterinarian IN NUMBER, id_petExtraInfo IN NUMBER)

AS 
BEGIN
    INSERT INTO medic_sheet
    VALUES(id_medicSheet, abandonmentDescription, id_veterinarian, id_petExtraInfo);
    COMMIT;
END insertMedicSheet;