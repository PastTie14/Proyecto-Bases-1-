CREATE OR REPLACE PACKAGE adminPetExtraInfo IS

FUNCTION insertPetExtraInfo(pIdPetExtraInfo IN NUMBER, pSize IN VARCHAR2, 
                                            pBeforePicture IN VARCHAR2, pAfterPicture IN VARCHAR2, 
                                            pIdPet IN NUMBER, pIdCurrentStatus IN NUMBER, pIdEnergyLevel IN NUMBER,
                                            pIdTrainingEase IN NUMBER) RETURN NUMBER;

PROCEDURE insertCurrentStatus(pIdCurrentStatus IN NUMBER, pStatusType IN VARCHAR2);

PROCEDURE insertEnergyLevel(pIdEnergyLevel IN NUMBER, pName IN VARCHAR2);

PROCEDURE insertTrainingEase(pIdTrainingEase IN NUMBER, pName IN VARCHAR2);

PROCEDURE insertBounty(pIdBounty IN NUMBER, pAmount IN NUMBER,
                        pIdPetExtraInfo IN NUMBER, pIdCurrency IN NUMBER);

END adminPetExtraInfo;