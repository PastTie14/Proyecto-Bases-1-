CREATE OR REPLACE PACKAGE BODY adminPetExtraInfo AS

-- ======================================== INSERT ========================================

FUNCTION insertPetExtraInfo(pIdPetExtraInfo IN NUMBER,
                            pBeforePicture IN VARCHAR2, pAfterPicture IN VARCHAR2, 
                            pIdPet IN NUMBER, pIdCurrentStatus IN NUMBER, pIdEnergyLevel IN NUMBER,
                            pIdTrainingEase IN NUMBER)
RETURN NUMBER
AS 
    n_petExtraInfo_id NUMBER(8);
BEGIN
    INSERT INTO pet_extra_info (id_pet_extra_info, before_picture, after_picture, id_pet, id_current_status, 
                                id_energy_level, id_training_ease)
    VALUES(s_petExtraInfo.nextVal, pBeforePicture, pAfterPicture, pIdPet,
            pIdCurrentStatus, pIdEnergyLevel, pIdTrainingEase);
    COMMIT;
    SELECT s_petExtraInfo.CURRVAL INTO n_petExtraInfo_id FROM DUAL;
    RETURN (n_petExtraInfo_id);
END insertPetExtraInfo;

PROCEDURE insertCurrentStatus(pIdCurrentStatus IN NUMBER, pStatusType IN VARCHAR2)
IS 
BEGIN
    INSERT INTO current_status (id_current_status, status_type)
    VALUES(s_currentStatus.nextVal, pStatusType);
    COMMIT;
END insertCurrentStatus;

PROCEDURE insertEnergyLevel(pIdEnergyLevel IN NUMBER, pName IN VARCHAR2)
IS 
BEGIN
    INSERT INTO energy_level (id_energy_level, "name")
    VALUES(pIdEnergyLevel, pName);
    COMMIT;
END insertEnergyLevel;

PROCEDURE insertTrainingEase(pIdTrainingEase IN NUMBER, pName IN VARCHAR2)
AS 
BEGIN
    INSERT INTO training_ease (id_training_ease, "name")
    VALUES(s_trainingEase.nextVal, pName);
    COMMIT;
END insertTrainingEase;

PROCEDURE insertBounty(pIdBounty IN NUMBER, pAmount IN NUMBER,
                       pIdPetExtraInfo IN NUMBER, pIdCurrency IN NUMBER)
AS 
BEGIN
    INSERT INTO bounty (id_bounty, amount, id_pet_extra_info, id_currency)
    VALUES(s_bounty.nextVal, pAmount, pIdPetExtraInfo, pIdCurrency);
    COMMIT;
END insertBounty;

-- ======================================== UPDATE ========================================

PROCEDURE updatePetExtraInfo(pIdPetExtraInfo IN NUMBER,  
                            pBeforePicture IN VARCHAR2, pAfterPicture IN VARCHAR2, pIdCurrentStatus IN NUMBER, 
                            pIdEnergyLevel IN NUMBER, pIdTrainingEase IN NUMBER)
IS
BEGIN
    UPDATE pet_extra_info
    SET 
        before_picture = pBeforePicture,
        after_picture = pAfterPicture,
        id_current_status = pIdCurrentStatus,
        id_energy_level = pIdEnergyLevel,
        id_training_ease = pIdTrainingEase
    WHERE id_pet_extra_info = pIdPetExtraInfo;
    COMMIT;
END;

PROCEDURE updateCurrentStatus(pIdCurrentStatus IN NUMBER, pStatusType IN VARCHAR2)
IS
BEGIN
    UPDATE current_status
    SET status_type = pStatusType
    WHERE id_current_status = pIdCurrentStatus;
    COMMIT;
END;

PROCEDURE updateEnergyLevel(pIdEnergyLevel IN NUMBER, pName IN VARCHAR2)
IS
BEGIN
    UPDATE energy_level
    SET "name" = pName
    WHERE id_energy_level = pIdEnergyLevel;
    COMMIT;
END;

PROCEDURE updateTrainingEase(pIdTrainingEase IN NUMBER, pName IN VARCHAR2)
IS
BEGIN
    UPDATE training_ease
    SET "name" = pName
    WHERE id_training_ease = pIdTrainingEase;
    COMMIT;
END;

PROCEDURE updateBounty(pIdBounty IN NUMBER, pAmount IN NUMBER, pIdCurrency IN NUMBER)
IS
BEGIN
    UPDATE bounty
    SET amount = pAmount,
        id_currency = pIdCurrency
    WHERE id_bounty = pIdBounty;
    COMMIT;
END;

-- ======================================== GET ========================================

FUNCTION getPetExtraInfo RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM pet_extra_info;
    RETURN v_cursor;
END;


FUNCTION getPetExtraInfoById(pIdPet IN NUMBER) RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM pet_extra_info
    WHERE id_pet = pIdPet;
    RETURN v_cursor;
END;


FUNCTION getCurrentStatus RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM current_status;
    RETURN v_cursor;
END;

FUNCTION getCurrentStatusById(pIdCurrentStatus IN NUMBER) RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT cs.status_type FROM current_status cs 
        WHERE cs.id_current_status = pIdCurrentStatus;
    RETURN v_cursor;
END;


FUNCTION getEnergyLevel RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM energy_level;
    RETURN v_cursor;
END;

FUNCTION getEnergyLevelById(pIdEnergyLevel IN NUMBER) RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT el."name" FROM energy_level el
        WHERE el.id_energy_level= pIdEnergyLevel;
    RETURN v_cursor;
END;


FUNCTION getTrainingEase RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM training_ease;
    RETURN v_cursor;
END;

FUNCTION getTrainingEaseById(pIdTrainingEase IN NUMBER) RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT te."name" FROM training_ease te
        WHERE te.id_training_ease = pIdTrainingEase;
    RETURN v_cursor;
END;


FUNCTION getBounty RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM bounty;
    RETURN v_cursor;
END;

FUNCTION getBountyById(pIdBounty IN NUMBER) RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT b.amount FROM bounty b
        WHERE b.id_bounty= pIdBounty;
    RETURN v_cursor;
END;

-- DELETE
PROCEDURE deleteBounty(pIdBounty IN NUMBER)
IS
BEGIN
    DELETE FROM bounty
    WHERE id_bounty = pIdBounty;
END;

END adminPetExtraInfo;