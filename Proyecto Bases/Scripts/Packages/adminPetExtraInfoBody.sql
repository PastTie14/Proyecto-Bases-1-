CREATE OR REPLACE PACKAGE BODY adminPetExtraInfo AS

PROCEDURE insertCurrentStatus(id_currentStatus IN NUMBER, status_type IN VARCHAR2)

IS 
BEGIN
    INSERT INTO current_status
    VALUES(id_currentStatus, status_type);
    COMMIT;
END insertCurrentStatus;

--=======================================================================================

PROCEDURE insertEnergyLevel(id_energyLevel IN NUMBER, "name" IN VARCHAR2)

IS 
BEGIN
    INSERT INTO energy_level
    VALUES(id_energyLevel, "name", id_petExtraInfo);
    COMMIT;
END insertEnergyLevel;

--=======================================================================================

PROCEDURE insertTrainingEase(id_trainingEase IN NUMBER, "name" IN VARCHAR2)

AS 
BEGIN
    INSERT INTO training_ease
    VALUES(id_trainingEase, "name", id_petExtraInfo);
    COMMIT;
END insertTrainingEase;

--=======================================================================================

PROCEDURE insertBounty(id_bounty IN NUMBER, amount IN NUMBER,
                                        id_petExtraInfo IN NUMBER, id_currency IN NUMBER)

AS 
BEGIN
    INSERT INTO bounty
    VALUES(id_bounty, amount, id_petExtraInfo, id_currency);
    COMMIT;
END insertBounty;

END adminPetExtraInfo;