CREATE OR REPLACE PACKAGE adminPetExtraInfo IS

-- INSERT
FUNCTION insertPetExtraInfo(pIdPetExtraInfo IN NUMBER, pSize IN VARCHAR2, 
                                            pBeforePicture IN VARCHAR2, pAfterPicture IN VARCHAR2, 
                                            pIdPet IN NUMBER, pIdCurrentStatus IN NUMBER, pIdEnergyLevel IN NUMBER,
                                            pIdTrainingEase IN NUMBER) RETURN NUMBER;

PROCEDURE insertCurrentStatus(pIdCurrentStatus IN NUMBER, pStatusType IN VARCHAR2);

PROCEDURE insertEnergyLevel(pIdEnergyLevel IN NUMBER, pName IN VARCHAR2);

PROCEDURE insertTrainingEase(pIdTrainingEase IN NUMBER, pName IN VARCHAR2);

PROCEDURE insertBounty(pIdBounty IN NUMBER, pAmount IN NUMBER,
                        pIdPetExtraInfo IN NUMBER, pIdCurrency IN NUMBER);

-- UPDATE
PROCEDURE updatePetExtraInfo(pIdPetExtraInfo IN NUMBER, pSize IN VARCHAR2, 
                            pBeforePicture IN VARCHAR2, pAfterPicture IN VARCHAR2, pIdCurrentStatus IN NUMBER, 
                            pIdEnergyLevel IN NUMBER, pIdTrainingEase IN NUMBER);

PROCEDURE updateCurrentStatus(pIdCurrentStatus IN NUMBER, pStatusType IN VARCHAR2);

PROCEDURE updateEnergyLevel(pIdEnergyLevel IN NUMBER, pName IN VARCHAR2);

PROCEDURE updateTrainingEase(pIdTrainingEase IN NUMBER, pName IN VARCHAR2);

PROCEDURE updateBounty(pIdBounty IN NUMBER, pAmount IN NUMBER, pIdCurrency IN NUMBER);

-- GET
FUNCTION getPetExtraInfo RETURN SYS_REFCURSOR;
FUNCTION getPetExtraInfoById(pIdPet IN NUMBER) RETURN SYS_REFCURSOR;

FUNCTION getCurrentStatus RETURN SYS_REFCURSOR;
FUNCTION getCurrentStatusById(pIdCurrentStatus IN NUMBER) RETURN SYS_REFCURSOR;

FUNCTION getEnergyLevel RETURN SYS_REFCURSOR;
FUNCTION getEnergyLevelById(pIdEnergyLevel IN NUMBER) RETURN SYS_REFCURSOR;

FUNCTION getTrainingEase RETURN SYS_REFCURSOR;
FUNCTION getTrainingEaseById(pIdTrainingEase IN NUMBER) RETURN SYS_REFCURSOR;

FUNCTION getBounty RETURN SYS_REFCURSOR;
FUNCTION getBountyById(pIdBounty IN NUMBER) RETURN SYS_REFCURSOR;

-- DELETE
PROCEDURE deleteBounty(pIdBounty IN NUMBER);

END adminPetExtraInfo;