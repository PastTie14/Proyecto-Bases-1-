CREATE OR REPLACE PACKAGE BODY adminUser AS

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
    INSERT INTO association
    VALUES(pIdUser, pName);
    COMMIT;
END;

PROCEDURE insertAdopter(pIdUser IN NUMBER, pFirstName VARCHAR2, pSecondName VARCHAR2, pFirstSurname VARCHAR2, pSecondSurname VARCHAR2)
IS 
BEGIN
    INSERT INTO adopter
    VALUES(pIdUser, pFirstName, pSecondName, pFirstSurname, pSecondSurname);
    COMMIT;
END;

PROCEDURE insertRescuer(pIdUser IN NUMBER, pFirstName VARCHAR2, pSecondName VARCHAR2, pFirstSurname VARCHAR2, pSecondSurname VARCHAR2)
IS 
BEGIN
    INSERT INTO rescuer
    VALUES(pIdUser, pFirstName, pSecondName, pFirstSurname, pSecondSurname);
    COMMIT;
END;

PROCEDURE insertCribHouse(pIdUser IN NUMBER, pName VARCHAR2, pRequiresDonations IN NUMBER, pAcceptedSize IN NUMBER)
IS 
BEGIN
    INSERT INTO crib_house
    VALUES(pIdUser, pName, pRequiresDonations, pAcceptedSize);
    COMMIT;
END;

PROCEDURE insertLog(pIdLog IN NUMBER, pChangeDate DATE, pChangeBy VARCHAR2, pTableName VARCHAR2, pFieldName VARCHAR2, pPreviousValue VARCHAR2,
                    pCurrentValue VARCHAR2, pIdUser IN NUMBER)
IS 
BEGIN
    INSERT INTO "log"
    VALUES(pIdLog, pChangeDate, pChangeBy, pTableName, pFieldName, pPreviousValue, pCurrentValue, pIdUser);
    COMMIT;
END;



FUNCTION getAssociation RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM association;
    RETURN v_cursor;
END;

FUNCTION getAdopter RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM adopter;
    RETURN v_cursor;
END;

FUNCTION getRescuer RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM rescuer;
    RETURN v_cursor;
END;

FUNCTION getCribHouse RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM crib_house;
    RETURN v_cursor;
END;

END;