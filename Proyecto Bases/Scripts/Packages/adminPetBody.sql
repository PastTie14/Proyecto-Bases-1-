CREATE OR REPLACE PACKAGE BODY adminPet AS

FUNCTION insertPet(pIdPet IN NUMBER, pPicture IN VARCHAR2, pFirstName IN VARCHAR2,
                                       pBirthDate IN DATE, pDateLost IN DATE, pDateFound IN DATE,
                                       pEmail IN VARCHAR2, pIdStatus IN NUMBER, pIdPetType IN NUMBER, 
                                       pIdRescuer IN NUMBER, pIdSize IN NUMBER)
RETURN NUMBER
AS
    n_pet_id NUMBER(8);
BEGIN
    INSERT INTO pet (id_pet, picture, first_name, birth_date,
                    date_lost, date_found, email, id_status, id_pet_type,id_size, id_rescuer,CREATEDBY,CREATEDAT)
    VALUES(s_pet.nextVal, pPicture, pFirstName, pBirthDate, pDateLost, pDateFound, 
            pEmail, pIdStatus, pIdPetType, pIdSize, pIdRescuer, USER, SYSTIMESTAMP);
    COMMIT;
    SELECT s_pet.CURRVAL INTO n_pet_id FROM DUAL;
    RETURN (n_pet_id); 
END insertPet;



PROCEDURE insertIdChip(pIdChip IN NUMBER, pChipNumber IN VARCHAR2,
                        pRegistrationDate IN DATE, pIdPet IN NUMBER)
IS 
BEGIN
    INSERT INTO identification_chip (id_chip, chip_number, registration_date, id_pet)
    VALUES(s_identificationChip.nextVal, pChipNumber, pRegistrationDate, pIdPet);
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

FUNCTION getPetById(p_idPet IN NUMBER) RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM pet
    Where id_pet = p_idPet;
    Return v_cursor;
END;

FUNCTION getPetByStatus(p_idStatus IN NUMBER) RETURN SYS_REFCURSOR
IS 
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM pet
    WHERE id_status = p_idStatus;
    Return v_cursor;
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

FUNCTION getPetColors(pIdPet IN NUMBER) RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR 
        SELECT c."name"
        FROM color c
        
        INNER JOIN pet_x_color pxc
        ON c.id_color = pxc.id_color
        
        WHERE pxc.id_pet = pIdPet;
    RETURN v_cursor;
END;

FUNCTION getDistrictPets(pIdDistrict IN NUMBER) RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR 
        SELECT p.id_pet, p.first_name
        FROM pet p
        
        INNER JOIN pet_x_district pxd
        ON p.id_pet= pxd.id_pet
        
        WHERE pxd.id_district = pIdDistrict;
    RETURN v_cursor;
END;

FUNCTION getCribHousePetTypes(pIdCribHouse IN NUMBER) RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR 
        SELECT pt."name"
        FROM pet_type pt
        
        INNER JOIN pet_type_x_crib_house ptxch
        ON pt.id_pet_type = ptxch.id_pet_type
        
        WHERE ptxch.id_crib_house = pIdCribHouse;
    RETURN v_cursor;
END;

FUNCTION getCardInfo(p_id_pet IN NUMBER) RETURN SYS_REFCURSOR
IS 
v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR 
        SELECT a.picture,b.Status_Type, a.First_name, c.ID_pet_extra_info, d."name", e.email, f."name", g."name", h."name"   FROM pet a
        LEFT JOIN status b
        ON b.id_status = a.Id_status
        LEFT JOIN  pet_extra_info c
        ON c.ID_pet = a.Id_pet
        LEFT JOIN ENERGY_LEVEL d
        ON c.id_energy_level = d.id_energy_level
        LEFT JOIN "user" e
        ON a.id_rescuer = e.id_user
        LEFT JOIN "size" f
        ON a.id_size = f.id_size
        LEFT JOIN TRAINING_EASE g
        ON g.id_training_ease = c.id_training_ease
        LEFT JOIN PET_TYPE h
        ON a.id_pet_type = h.id_pet_type
        WHERE a.id_pet = p_id_pet; 
    RETURN v_cursor;
END;

FUNCTION getPopUpInfo(p_id_pet IN NUMBER) RETURN SYS_REFCURSOR
IS 
v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR 
        SELECT 
            a.picture,
            b.Status_Type, 
            h."name",
            a.first_name,
            a.birth_date,
            e.email,
            a.date_lost,
            a.date_found,
            f."name",
            d."name",
            g."name",
            i."name",
            j.amount,
            k.acronym,
            l.abandonment_description
            FROM pet a
        LEFT JOIN status b
        ON b.id_status = a.Id_status
        LEFT JOIN  pet_extra_info c
        ON c.ID_pet = a.Id_pet
        LEFT JOIN ENERGY_LEVEL d
        ON c.id_energy_level = d.id_energy_level
        LEFT JOIN "user" e
        ON a.id_rescuer = e.id_user
        LEFT JOIN "size" f
        ON a.id_size = f.id_size
        LEFT JOIN TRAINING_EASE g
        ON g.id_training_ease = c.id_training_ease
        LEFT JOIN PET_TYPE h
        ON a.id_pet_type = h.id_pet_type
        LEFT JOIN crib_house i
        ON a.id_crib_House = i.id_user
        LEFT JOIN Bounty j
        ON c.id_pet_extra_info = j.id_pet_extra_info
        LEFT JOIN currency k
        ON j.id_currency = k.id_currency
        LEFT JOIN medic_sheet l
        ON c.id_pet_extra_info = l.id_pet_extra_info
        WHERE a.id_pet = p_id_pet; 
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