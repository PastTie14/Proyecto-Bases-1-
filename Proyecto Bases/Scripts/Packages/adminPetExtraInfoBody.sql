CREATE OR REPLACE PACKAGE BODY adminPetExtraInfo AS

FUNCTION insertPetExtraInfo(pIdPetExtraInfo IN NUMBER, pSize IN VARCHAR2, 
                            pBeforePicture IN VARCHAR2, pAfterPicture IN VARCHAR2, 
                            pIdPet IN NUMBER, pIdCurrentStatus IN NUMBER, pIdEnergyLevel IN NUMBER,
                            pIdTrainingEase IN NUMBER)
RETURN NUMBER
AS 
    n_petExtraInfo_id NUMBER(8);
BEGIN
    INSERT INTO pet_extra_info
    VALUES(pIdPetExtraInfo, pSize, pBeforePicture, pAfterPicture, pIdPet,
            pIdCurrentStatus, pIdEnergyLevel, pIdTrainingEase);
    COMMIT;
    SELECT s_petExtraInfo.CURRVAL INTO n_petExtraInfo_id FROM DUAL;
    RETURN (n_petExtraInfo_id);
END insertPetExtraInfo;

--=======================================================================================

PROCEDURE insertCurrentStatus(pIdCurrentStatus IN NUMBER, pStatusType IN VARCHAR2)
IS 
BEGIN
    INSERT INTO current_status
    VALUES(pIdCurrentStatus, pStatusType);
    COMMIT;
END insertCurrentStatus;

--=======================================================================================

PROCEDURE insertEnergyLevel(pIdEnergyLevel IN NUMBER, pName IN VARCHAR2)
IS 
BEGIN
    INSERT INTO energy_level
    VALUES(pIdEnergyLevel, pName);
    COMMIT;
END insertEnergyLevel;

--=======================================================================================

PROCEDURE insertTrainingEase(pIdTrainingEase IN NUMBER, pName IN VARCHAR2)
AS 
BEGIN
    INSERT INTO training_ease
    VALUES(pIdTrainingEase, pName);
    COMMIT;
END insertTrainingEase;

--=======================================================================================

PROCEDURE insertBounty(pIdBounty IN NUMBER, pAmount IN NUMBER,
                       pIdPetExtraInfo IN NUMBER, pIdCurrency IN NUMBER)
AS 
BEGIN
    INSERT INTO bounty
    VALUES(pIdBounty, pAmount, pIdPetExtraInfo, pIdCurrency);
    COMMIT;
END insertBounty;

END adminPetExtraInfo;