CREATE OR REPLACE PACKAGE BODY adminUser AS

PROCEDURE insertUser(id_user IN NUMBER,email VARCHAR2,"password" VARCHAR2)
IS 
BEGIN
    INSERT INTO "user" (id_user, email, "password")
    VALUES(id_user,email,"password");
    COMMIT;
END;

PROCEDURE insertAssociation(id_user IN NUMBER, "name" VARCHAR2)
IS 
BEGIN
    INSERT INTO association
    VALUES(id_user,"name");
    COMMIT;
END;

PROCEDURE insertAdopter(id_user IN NUMBER, first_name VARCHAR2, second_name VARCHAR2, first_surname VARCHAR2, second_surname VARCHAR2)
IS 
BEGIN
    INSERT INTO adopter
    VALUES(id_user,first_name,second_name,first_surname,second_surname);
    COMMIT;
END;

PROCEDURE insertRescuer(id_user IN NUMBER, first_name VARCHAR2, second_name VARCHAR2, first_surname VARCHAR2, second_surname VARCHAR2)
IS 
BEGIN
    INSERT INTO rescuer
    VALUES(id_user,first_name,second_name,first_surname,second_surname);
    COMMIT;
END;

PROCEDURE insertCribHouse(id_user IN NUMBER, "name" VARCHAR2, requires_donations IN NUMBER, accepted_size IN NUMBER)
IS 
BEGIN
    INSERT INTO crib_house
    VALUES(id_user,"name",requires_donations,accepted_size);
    COMMIT;
END;

PROCEDURE insertLog(id_log IN NUMBER, changeDate DATE, changeBy VARCHAR2, tableName VARCHAR2, fieldName VARCHAR2, previousValue VARCHAR2,
currentValue VARCHAR2, id_user IN NUMBER)
IS 
BEGIN
    INSERT INTO "log"
    VALUES(id_log,changeDate,changeBy,tableName,fieldName,previousValue,currentValue,id_user);
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