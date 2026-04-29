CREATE OR REPLACE PACKAGE BODY adminPet AS

FUNCTION insertPet(pIdPet IN NUMBER, pPicture IN VARCHAR2, pFirstName IN VARCHAR2,
                                       pBirthDate IN DATE, pDateLost IN DATE, pDateFound IN DATE,
                                       pEmail IN VARCHAR2, pCreatedBy IN VARCHAR2, pCreatedAt IN DATE,
                                       pModifiedBy IN VARCHAR2, pModifiedAt IN DATE, pIdStatus IN NUMBER,
                                       pIdPetType IN NUMBER, pIdRescuer IN NUMBER)
RETURN NUMBER
AS
    n_pet_id NUMBER(8);
BEGIN
    INSERT INTO pet
    VALUES(pIdPet, pPicture, pFirstName, pBirthDate, pDateLost, pDateFound, 
            pEmail, pCreatedBy, pCreatedAt, pModifiedBy, pModifiedAt,
            pIdStatus, pIdPetType, pIdRescuer);
    COMMIT;
    SELECT s_pet.CURRVAL INTO n_pet_id FROM DUAL;
    RETURN (n_pet_id); -- returns the pet id to use it in intermediate tables
END insertPet;


PROCEDURE insertIdChip(pIdChip IN NUMBER, pChipNumber IN VARCHAR2,
                        pRegistrationDate IN DATE, pIdPet IN NUMBER)
IS 
BEGIN
    INSERT INTO identification_chip
    VALUES(pIdChip, pChipNumber, pRegistrationDate, pIdPet);
    COMMIT;
END insertIdChip;

--=======================================================================================

PROCEDURE insertPetXColor(pIdPet IN NUMBER, pIdColor IN NUMBER)
IS 
BEGIN
    INSERT INTO pet_x_color
    VALUES(pIdPet, pIdColor);
    COMMIT;
END insertPetXColor;

--=======================================================================================

PROCEDURE insertPetXDistrict(pIdPet IN NUMBER, pIdDistrict IN NUMBER)
IS 
BEGIN
    INSERT INTO pet_x_district
    VALUES(pIdPet, pIdDistrict);
    
    COMMIT;
END insertPetXDistrict;

--=======================================================================================

PROCEDURE insertPetTypeXCribHouse(pIdPetType IN NUMBER, pIdCribHouse IN NUMBER)
IS 
BEGIN
    INSERT INTO pet_type_x_crib_house
    VALUES(pIdPetType, pIdCribHouse);
    COMMIT;
END insertPetTypeXCribHouse;

END adminPet;