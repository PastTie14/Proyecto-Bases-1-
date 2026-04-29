CREATE OR REPLACE PACKAGE adminPet IS
              
FUNCTION insertPet(pIdPet IN NUMBER, pPicture IN VARCHAR2, pFirstName IN VARCHAR2,
                                       pBirthDate IN DATE, pDateLost IN DATE, pDateFound IN DATE,
                                       pEmail IN VARCHAR2, pCreatedBy IN VARCHAR2, pCreatedAt IN DATE,
                                       pModifiedBy IN VARCHAR2, pModifiedAt IN DATE, pIdStatus IN NUMBER,
                                       pIdPetType IN NUMBER, pIdRescuer IN NUMBER) RETURN NUMBER;
                                       
PROCEDURE insertIdChip(pIdChip IN NUMBER, pChipNumber IN VARCHAR2,
                        pRegistrationDate IN DATE, pIdPet IN NUMBER);
PROCEDURE insertPetXColor(pIdPet IN NUMBER, pIdColor IN NUMBER);

PROCEDURE insertPetXDistrict(pIdPet IN NUMBER, pIdDistrict IN NUMBER);

PROCEDURE insertPetTypeXCribHouse(pIdPetType IN NUMBER, pIdCribHouse IN NUMBER);

END adminPet;
