CREATE OR REPLACE PACKAGE BODY adminUser AS

-- ======================================== INSERT ========================================

PROCEDURE insertUser(pIdUser IN NUMBER, pEmail VARCHAR2, pPassword VARCHAR2)
IS 
BEGIN
    INSERT INTO "user" (id_user, email, "password")
    VALUES(pIdUser, pEmail, pPassword);
    COMMIT;
END;

PROCEDURE insertAssociation(pIdUser IN NUMBER, pName VARCHAR2)
IS 
BEGIN
    INSERT INTO association (id_user, "name")
    VALUES(pIdUser, pName);
    COMMIT;
END;

PROCEDURE insertAdopter(pIdUser IN NUMBER, pFirstName VARCHAR2, pSecondName VARCHAR2, pFirstSurname VARCHAR2, pSecondSurname VARCHAR2)
IS 
BEGIN
    INSERT INTO adopter (id_user, first_name, second_name, first_surname, second_surname)
    VALUES(pIdUser, pFirstName, pSecondName, pFirstSurname, pSecondSurname);
    COMMIT;
END;

PROCEDURE insertRescuer(pIdUser IN NUMBER, pFirstName VARCHAR2, pSecondName VARCHAR2, pFirstSurname VARCHAR2, pSecondSurname VARCHAR2)
IS 
BEGIN
    INSERT INTO rescuer (id_user, first_name, second_name, first_surname, second_surname)
    VALUES(pIdUser, pFirstName, pSecondName, pFirstSurname, pSecondSurname);
    COMMIT;
END;

PROCEDURE insertCribHouse(pIdUser IN NUMBER, pName VARCHAR2, pRequiresDonations IN NUMBER, pAcceptedSize IN NUMBER)
IS 
BEGIN
    INSERT INTO crib_house (id_user, "name", requires_donations, accepted_size)
    VALUES(pIdUser, pName, pRequiresDonations, pAcceptedSize);
    COMMIT;
END;

PROCEDURE insertLog(pIdLog IN NUMBER, pChangeDate DATE, pChangeBy VARCHAR2, pTableName VARCHAR2, pFieldName VARCHAR2, pPreviousValue VARCHAR2,
                    pCurrentValue VARCHAR2, pIdUser IN NUMBER)
IS 
BEGIN
    INSERT INTO "log" (id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
    VALUES(pIdLog, pChangeDate, pChangeBy, pTableName, pFieldName, pPreviousValue, pCurrentValue);
    COMMIT;
END;

-- ======================================== UPDATE ========================================

PROCEDURE updateUser(pIdUser IN NUMBER, pEmail IN VARCHAR2, pPassword IN VARCHAR2)
IS
BEGIN
    UPDATE "user"
    SET email = pEmail, 
        "password" = pPassword
    WHERE  id_user = pIdUser;
    COMMIT;
END;

PROCEDURE updateAssociation(pIdUser IN NUMBER, pName IN VARCHAR2)
IS
BEGIN
    UPDATE association
    SET "name" = pName
    WHERE id_user = pIdUser;
    COMMIT;
END;

PROCEDURE updateAdopter(pIdUser IN NUMBER, pFirstName IN VARCHAR2, pSecondName VARCHAR2, 
                        pFirstSurname VARCHAR2, pSecondSurname VARCHAR2)
IS
BEGIN
    UPDATE adopter
    SET first_name = pFirstName,
        second_name = pSecondName,
        first_surname = pFirstSurname,
        second_surname = pSecondSurname
    WHERE id_user = pIdUser;
    COMMIT;
END;

PROCEDURE updateRescuer(pIdUser IN NUMBER, pFirstName IN VARCHAR2, pSecondName IN VARCHAR2, 
                        pFirstSurname IN VARCHAR2, pSecondSurname IN VARCHAR2)
IS
BEGIN
    UPDATE rescuer
    SET first_name = pFirstName,
        second_name = pSecondName,
        first_surname = pFirstSurname,
        second_surname = pSecondSurname
    WHERE id_user = pIdUser;
    COMMIT;
END;

PROCEDURE updateCribHouse(pIdUser IN NUMBER, pName IN VARCHAR2, pRequiresDonations IN NUMBER, pAcceptedSize IN NUMBER)
IS
BEGIN
    UPDATE crib_house
    SET "name" = pName,
        requires_donations = pRequiresDonations,
        accepted_size = pAcceptedSize
    WHERE id_user = pIdUser;
    COMMIT;
END;

-- ======================================== GET ========================================

FUNCTION getUser RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM "user";
    RETURN v_cursor;
END;

FUNCTION getUserById(pIdUser IN NUMBER) RETURN SYS_REFCURSOR 
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
        SELECT u.email FROM "user" u
        WHERE u.id_user = pIdUser;
    RETURN v_cursor;
END;


FUNCTION getAssociation RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM association;
    RETURN v_cursor;
END;

FUNCTION getAssociationById(pIdAssociation IN NUMBER) RETURN SYS_REFCURSOR 
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
        SELECT a."name" FROM association a
        WHERE a.id_user = pIdAssociation;
    RETURN v_cursor;
END;


FUNCTION getAdopter RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM adopter;
    RETURN v_cursor;
END;

FUNCTION getAdopterById(pIdAdopter IN NUMBER) RETURN SYS_REFCURSOR 
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
        SELECT a.first_name, a.second_name, a.first_surname, a.second_surname 
        FROM adopter a
        WHERE a.id_user = pIdAdopter;
    RETURN v_cursor;
END;


FUNCTION getRescuer RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM rescuer;
    RETURN v_cursor;
END;

FUNCTION getRescuerById(pIdRescuer IN NUMBER) RETURN SYS_REFCURSOR 
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
        SELECT r.first_name, r.second_name, r.first_surname, r.second_surname 
        FROM rescuer r
        WHERE r.id_user = pIdRescuer;
    RETURN v_cursor;
END;


FUNCTION getCribHouse RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM crib_house;
    RETURN v_cursor;
END;

FUNCTION getCribHouseById(pIdCribHouse IN NUMBER) RETURN SYS_REFCURSOR 
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
        SELECT ch."name" FROM crib_house ch
        WHERE ch.id_user = pIdCribHouse;
    RETURN v_cursor;
END;


-- ======================================== DELETE ========================================
PROCEDURE deleteUser(pidUser IN NUMBER)
IS
BEGIN
    DELETE FROM "user"
    WHERE id_user = pIdUser;
END;

PROCEDURE deleteAssociation(pidUser IN NUMBER)
IS
BEGIN
    DELETE FROM association
    WHERE id_user = pIdUser;
END;

PROCEDURE deleteAdopter(pidUser IN NUMBER)
IS
BEGIN
    DELETE FROM adopter
    WHERE id_user = pIdUser;
END;

PROCEDURE deleteRescuer(pidUser IN NUMBER)
IS
BEGIN
    DELETE FROM rescuer
    WHERE id_user = pIdUser;
END;

PROCEDURE deleteCribHouse(pidUser IN NUMBER)
IS
BEGIN
    DELETE FROM crib_house
    WHERE id_user = pIdUser;
END;

END;