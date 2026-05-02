CREATE OR REPLACE PACKAGE BODY adminPet AS

FUNCTION insertPet(pIdPet IN NUMBER, pPicture IN VARCHAR2, pFirstName IN VARCHAR2,
                                       pBirthDate IN DATE, pDateLost IN DATE, pDateFound IN DATE,
                                       pEmail IN VARCHAR2, pIdStatus IN NUMBER, pIdPetType IN NUMBER, 
                                       pIdRescuer IN NUMBER)
RETURN NUMBER
AS
    n_pet_id NUMBER(8);
BEGIN
    INSERT INTO pet (id_pet, picture, first_name, birth_date,
                    date_lost, date_found, email, id_status, id_pet_type, id_rescuer)
    VALUES(pIdPet, pPicture, pFirstName, pBirthDate, pDateLost, pDateFound, 
            pEmail, pIdStatus, pIdPetType, pIdRescuer);
    COMMIT;
    SELECT s_pet.CURRVAL INTO n_pet_id FROM DUAL;
    RETURN (n_pet_id); -- returns the pet id to use it in intermediate tables
END insertPet;

PROCEDURE insertIdChip(pIdChip IN NUMBER, pChipNumber IN VARCHAR2,
                        pRegistrationDate IN DATE, pIdPet IN NUMBER)
IS 
BEGIN
    INSERT INTO identification_chip (id_chip, chip_number, registration_date, id_pet)
    VALUES(pIdChip, pChipNumber, pRegistrationDate, pIdPet);
    COMMIT;
END insertIdChip;

PROCEDURE insertPetXColor(pIdPet IN NUMBER, pIdColor IN NUMBER)
IS 
BEGIN
    INSERT INTO pet_x_color (id_pet, id_color)
    VALUES(pIdPet, pIdColor);
    COMMIT;
END insertPetXColor;

PROCEDURE insertPetXDistrict(pIdPet IN NUMBER, pIdDistrict IN NUMBER)
IS 
BEGIN
    INSERT INTO pet_x_district (id_pet, id_district)
    VALUES(pIdPet, pIdDistrict);
    
    COMMIT;
END insertPetXDistrict;

PROCEDURE insertPetTypeXCribHouse(pIdPetType IN NUMBER, pIdCribHouse IN NUMBER)
IS 
BEGIN
    INSERT INTO pet_type_x_crib_house (id_pet_type, id_crib_house)
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

-- ======================================== DELETE ========================================

PROCEDURE deletePet(pIdPet IN NUMBER)
IS
BEGIN
    DELETE FROM pet
    WHERE id_pet = pIdPet;
END;

END adminPet;