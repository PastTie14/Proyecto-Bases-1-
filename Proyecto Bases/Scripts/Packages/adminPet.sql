CREATE OR REPLACE PACKAGE adminPet IS
              
-- INSERT
FUNCTION insertPet(pIdPet IN NUMBER, pPicture IN VARCHAR2, pFirstName IN VARCHAR2,
                                       pBirthDate IN DATE, pDateLost IN DATE, pDateFound IN DATE,
                                       pEmail IN VARCHAR2, pIdStatus IN NUMBER, pIdPetType IN NUMBER, 
                                       pIdRescuer IN NUMBER) RETURN NUMBER;
                                       
PROCEDURE insertIdChip(pIdChip IN NUMBER, pChipNumber IN VARCHAR2,
                        pRegistrationDate IN DATE, pIdPet IN NUMBER);
PROCEDURE insertPetXColor(pIdPet IN NUMBER, pIdColor IN NUMBER);

PROCEDURE insertPetXDistrict(pIdPet IN NUMBER, pIdDistrict IN NUMBER);

PROCEDURE insertPetTypeXCribHouse(pIdPetType IN NUMBER, pIdCribHouse IN NUMBER);

-- UPDATE
PROCEDURE updatePet(pIdPet IN NUMBER, pPicture IN VARCHAR2, pFirstName IN VARCHAR2,
                    pBirthDate IN DATE, pDateLost IN DATE, pDateFound IN DATE,
                    pEmail IN VARCHAR2, pIdStatus IN NUMBER);

/*
PROCEDURE updatePetXColor(pIdPet IN NUMBER, pIdColor IN NUMBER);

PROCEDURE updatePetXDistrict(pIdPet IN NUMBER, pIdDistrict IN NUMBER);

PROCEDURE updatePetTypeXCribHouse(pIdPetType IN NUMBER, pIdCribHouse IN NUMBER);
*/

-- GET 
FUNCTION getPet RETURN SYS_REFCURSOR;
FUNCTION getIdChip RETURN SYS_REFCURSOR;
FUNCTION getPetXColor RETURN SYS_REFCURSOR;
FUNCTION getPetXDistrict RETURN SYS_REFCURSOR;
FUNCTION getPetTypeXCribHouse RETURN SYS_REFCURSOR;

-- DELETE
PROCEDURE deletePet(pIdPet IN NUMBER);

END adminPet;
