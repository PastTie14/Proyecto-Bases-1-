CREATE OR REPLACE PACKAGE BODY adminPhoneNumber AS

-- ======================================== INSERT ========================================

PROCEDURE insertPhoneNumber(pIdPhone IN NUMBER, pNumber IN NUMBER,
                                                pIdUser IN NUMBER, pIdPet IN NUMBER, 
                                                pIdVeterinarian IN NUMBER)
AS 
BEGIN
    INSERT INTO phone_number (id_phone, "number", id_user, id_pet, id_veterinarian)
    VALUES(pIdPhone, pNumber, pIdUser, pIdPet, pIdVeterinarian);
    COMMIT;
END;

-- ======================================== GET ========================================

FUNCTION getPhoneNumber RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM phone_number;
    RETURN v_cursor;
END;

FUNCTION getUserPhones(pIdUser IN NUMBER) RETURN SYS_REFCURSOR 
IS
    phoneCursor SYS_REFCURSOR;
BEGIN
    OPEN phoneCursor FOR 
        SELECT p."number"
        FROM phone_number p
        
        WHERE p.id_user = pIdUser;
        RETURN phoneCursor;
END;

FUNCTION getPetPhones(pIdPet IN NUMBER) RETURN SYS_REFCURSOR 
IS
    phoneCursor SYS_REFCURSOR;
BEGIN
    OPEN phoneCursor FOR 
        SELECT p."number"
        FROM phone_number p
        
        WHERE p.id_pet = pIdPet;
        RETURN phoneCursor;
END;

FUNCTION getVeterinarianPhones(pIdVeterinarian IN NUMBER) RETURN SYS_REFCURSOR 
IS
    phoneCursor SYS_REFCURSOR;
BEGIN
    OPEN phoneCursor FOR 
        SELECT p."number"
        FROM phone_number p
        
        WHERE p.id_veterinarian = pIdVeterinarian;
        RETURN phoneCursor;
END;

-- ======================================== DELETE ========================================

PROCEDURE deletePhoneNumber(pIdPhone IN NUMBER)
IS
BEGIN
    DELETE FROM phone_number
    WHERE id_phone = pIdPhone;
END;

END adminPhoneNumber;