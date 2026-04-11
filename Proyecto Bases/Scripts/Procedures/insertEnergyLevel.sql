CREATE OR REPLACE PROCEDURE insertEnergyLevel(id_energyLevel IN NUMBER, "name" IN VARCHAR2,
                                           id_petExtraInfo IN NUMBER)

AS 
BEGIN
    INSERT INTO energy_level
    VALUES(id_energyLevel, "name", id_petExtraInfo);
    COMMIT;
END insertEnergyLevel;