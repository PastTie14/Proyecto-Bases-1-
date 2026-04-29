CREATE OR REPLACE PACKAGE BODY adminCatalogs AS

PROCEDURE insertCurrency(pIdCurrency IN NUMBER, pName VARCHAR2, pAcronym VARCHAR2)
IS 
BEGIN
    INSERT INTO currency
    VALUES(pIdCurrency, pName, pAcronym);
    COMMIT;
END;

PROCEDURE insertProvince(pIdProvince IN NUMBER, pName VARCHAR2)
IS 
BEGIN
    INSERT INTO province
    VALUES(pIdProvince, pName);
    COMMIT;
END;

PROCEDURE insertCanton(pIdCanton IN NUMBER, pName VARCHAR2, pIdProvince IN NUMBER)
IS 
BEGIN
    INSERT INTO canton
    VALUES(pIdCanton, pName, pIdProvince);
    COMMIT;
END;

PROCEDURE insertDistrict(pIdDistrict IN NUMBER, pName VARCHAR2, pIdCanton IN NUMBER)
IS 
BEGIN
    INSERT INTO district
    VALUES(pIdDistrict, pName, pIdCanton);
    COMMIT;
END;

PROCEDURE insertPetType(pIdPetType IN NUMBER, pName VARCHAR2)
IS 
BEGIN
    INSERT INTO pet_type
    VALUES(pIdPetType, pName);
    COMMIT;
END;

PROCEDURE insertRace(pIdRace IN NUMBER, pName VARCHAR2, pIdPetType IN NUMBER)
IS 
BEGIN
    INSERT INTO race
    VALUES(pIdRace, pName, pIdPetType);
    COMMIT;
END;

PROCEDURE insertStatus(pIdStatus IN NUMBER, pStatusType VARCHAR2)
IS 
BEGIN
    INSERT INTO status
    VALUES(pIdStatus, pStatusType);
    COMMIT;
END;

PROCEDURE insertColor(pIdColor IN NUMBER, pName VARCHAR2)
IS 
BEGIN
    INSERT INTO color
    VALUES(pIdColor, pName);
    COMMIT;
END;

PROCEDURE insertValueType(pIdValueType IN NUMBER, pType VARCHAR2)
IS 
BEGIN
    INSERT INTO value_type
    VALUES(pIdValueType, pType);
    COMMIT;
END;

END adminCatalogs;