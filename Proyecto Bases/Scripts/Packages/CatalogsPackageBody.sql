CREATE OR REPLACE PACKAGE BODY adminCatalogs AS

PROCEDURE insertCurrency(id_currency IN NUMBER, "name" VARCHAR2,acronym VARCHAR2)
IS 
BEGIN
    INSERT INTO currency
    VALUES(id_currency,"name",acronym);
    COMMIT;
END;

PROCEDURE insertProvince(id_province IN NUMBER, "name" VARCHAR2)
IS 
BEGIN
    INSERT INTO province
    VALUES(id_province,"name");
    COMMIT;
END;

PROCEDURE insertCanton(id_canton IN NUMBER, "name" VARCHAR2,id_province IN NUMBER)
IS 
BEGIN
    INSERT INTO canton
    VALUES(id_canton,"name",id_province);
    COMMIT;
END;

PROCEDURE insertDistrict(id_district IN NUMBER, "name" VARCHAR2,id_canton IN NUMBER)
IS 
BEGIN
    INSERT INTO district
    VALUES(id_district,"name",id_canton);
    COMMIT;
END;

PROCEDURE insertPetType(id_pet_type IN NUMBER, "name" VARCHAR2)
IS 
BEGIN
    INSERT INTO pet_type
    VALUES(id_pet_type,"name");
    COMMIT;
END;

PROCEDURE insertRace(id_race IN NUMBER, "name" VARCHAR2,id_pet_type IN NUMBER)
IS 
BEGIN
    INSERT INTO race
    VALUES(id_race,"name",id_pet_type);
    COMMIT;
END;

PROCEDURE insertStatus(id_status IN NUMBER, status_type VARCHAR2)
IS 
BEGIN
    INSERT INTO status
    VALUES(id_status,status_type);
    COMMIT;
END;

PROCEDURE insertColor(id_color IN NUMBER, "name" VARCHAR2)
IS 
BEGIN
    INSERT INTO color
    VALUES(id_color,"name");
    COMMIT;
END;

PROCEDURE insertValueType(id_value_type IN NUMBER, "value" VARCHAR2)
IS 
BEGIN
    INSERT INTO value_type
    VALUES(id_value_type,"value");
    COMMIT;
END;

END adminCatalogs;