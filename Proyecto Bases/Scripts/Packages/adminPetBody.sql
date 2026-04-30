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

PROCEDURE insertPetXColor(pIdPet IN NUMBER, pIdColor IN NUMBER)
IS 
BEGIN
    INSERT INTO pet_x_color
    VALUES(pIdPet, pIdColor);
    COMMIT;
END insertPetXColor;

PROCEDURE insertPetXDistrict(pIdPet IN NUMBER, pIdDistrict IN NUMBER)
IS 
BEGIN
    INSERT INTO pet_x_district
    VALUES(pIdPet, pIdDistrict);
    
    COMMIT;
END insertPetXDistrict;

PROCEDURE insertPetTypeXCribHouse(pIdPetType IN NUMBER, pIdCribHouse IN NUMBER)
IS 
BEGIN
    INSERT INTO pet_type_x_crib_house
    VALUES(pIdPetType, pIdCribHouse);
    COMMIT;
END insertPetTypeXCribHouse;

-- ======================================== UPDATE ========================================

PROCEDURE updatePet(pIdPet IN NUMBER, pPicture IN VARCHAR2, pFirstName IN VARCHAR2,
                    pBirthDate IN DATE, pDateLost IN DATE, pDateFound IN DATE,
                    pEmail IN VARCHAR2, pIdStatus IN NUMBER)
IS
BEGIN
    UPDATE pet
    SET picture = pPicture,
        first_name = pFirstName,
        birth_date = pBirthDate,
        date_lost = pDateLost,
        date_found = pDateFound,
        email = pEmail,
        id_status = pIdStatus
    WHERE id_pet = pIdPet;
    COMMIT;
END;                                       

/*
PROCEDURE updatePetXColor(pIdPet IN NUMBER, pIdColor IN NUMBER)
IS
BEGIN
    UPDATE pet_x_color
    SET 
    WHERE ;
    COMMIT;
END;

PROCEDURE updatePetXDistrict(pIdPet IN NUMBER, pIdDistrict IN NUMBER)
IS
BEGIN
    UPDATE pet_x_district
    SET 
    WHERE ;
    COMMIT;
END;

PROCEDURE updatePetTypeXCribHouse(pIdPetType IN NUMBER, pIdCribHouse IN NUMBER)
IS
BEGIN
    UPDATE pet_type_x_crib_house
    SET 
    WHERE ;
    COMMIT;
END;
*/

-- ======================================== GET ========================================

FUNCTION getPet RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM pet;
    RETURN v_cursor;
END;

FUNCTION getIdChip RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM identification_chip;
    RETURN v_cursor;
END;

FUNCTION getPetXColor RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM pet_x_color;
    RETURN v_cursor;
END;

FUNCTION getPetXDistrict RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM pet_x_district;
    RETURN v_cursor;
END;

FUNCTION getPetTypeXCribHouse RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM pet_type_x_crib_house;
    RETURN v_cursor;
END;

END adminPet;