create or replace PACKAGE BODY adminCatalogs AS

    -- ==================== INSERT ====================

    PROCEDURE insertCurrency(p_id_currency IN NUMBER, p_name VARCHAR2, p_acronym VARCHAR2) IS
    BEGIN
        INSERT INTO currency (id_currency, "name", acronym)
        VALUES(p_id_currency, p_name, p_acronym);
        COMMIT;
    END;

    PROCEDURE insertProvince(p_id_province IN NUMBER, p_name VARCHAR2) IS
    BEGIN
        INSERT INTO province (id_province, "name")
        VALUES(p_id_province, p_name);
        COMMIT;
    END;

    PROCEDURE insertCanton(p_id_canton IN NUMBER, p_name VARCHAR2, p_id_province IN NUMBER) IS
    BEGIN
        INSERT INTO canton (id_canton, "name", id_province)
        VALUES(p_id_canton, p_name, p_id_province);
        COMMIT;
    END;

    PROCEDURE insertDistrict(p_id_district IN NUMBER, p_name VARCHAR2, p_id_canton IN NUMBER) IS
    BEGIN
        INSERT INTO district (id_district, "name", id_canton)
        VALUES(p_id_district, p_name, p_id_canton);
        COMMIT;
    END;

    PROCEDURE insertPetType(p_id_pet_type IN NUMBER, p_name VARCHAR2) IS
    BEGIN
        INSERT INTO pet_type (id_pet_type, "name")
        VALUES(p_id_pet_type, p_name);
        COMMIT;
    END;

    PROCEDURE insertRace(p_id_race IN NUMBER, p_name VARCHAR2, p_id_pet_type IN NUMBER) IS
    BEGIN
        INSERT INTO race (id_race, "name", id_pet_type)
        VALUES(p_id_race, p_name, p_id_pet_type);
        COMMIT;
    END;

    PROCEDURE insertStatus(p_id_status IN NUMBER, p_status_type VARCHAR2) IS
    BEGIN
        INSERT INTO status (id_status, status_type)
        VALUES(p_id_status, p_status_type);
        COMMIT;
    END;

    PROCEDURE insertColor(p_id_color IN NUMBER, p_name VARCHAR2) IS
    BEGIN
        INSERT INTO color (id_color, "name")
        VALUES(p_id_color, p_name);
        COMMIT;
    END;

    PROCEDURE insertValueType(p_id_value_type IN NUMBER, p_type VARCHAR2) IS
    BEGIN
        INSERT INTO value_type (id_value_type, "type")
        VALUES(p_id_value_type, p_type);
        COMMIT;
    END;


    -- ==================== UPDATE ====================

    PROCEDURE updateCurrency(p_id_currency IN NUMBER, p_name VARCHAR2, p_acronym VARCHAR2) IS
    BEGIN
        UPDATE currency
        SET    "name"    = p_name,
               acronym = p_acronym
        WHERE  id_currency = p_id_currency;
        COMMIT;
    END;

    PROCEDURE updateProvince(p_id_province IN NUMBER, p_name VARCHAR2) IS
    BEGIN
        UPDATE province
        SET    "name" = p_name
        WHERE  id_province = p_id_province;
        COMMIT;
    END;

    PROCEDURE updateCanton(p_id_canton IN NUMBER, p_name VARCHAR2, p_id_province IN NUMBER) IS
    BEGIN
        UPDATE canton
        SET    "name"        = p_name,
               id_province = p_id_province
        WHERE  id_canton = p_id_canton;
        COMMIT;
    END;

    PROCEDURE updateDistrict(p_id_district IN NUMBER, p_name VARCHAR2, p_id_canton IN NUMBER) IS
    BEGIN
        UPDATE district
        SET    "name"      = p_name,
               id_canton = p_id_canton
        WHERE  id_district = p_id_district;
        COMMIT;
    END;

    PROCEDURE updatePetType(p_id_pet_type IN NUMBER, p_name VARCHAR2) IS
    BEGIN
        UPDATE pet_type
        SET    "name" = p_name
        WHERE  id_pet_type = p_id_pet_type;
        COMMIT;
    END;

    PROCEDURE updateRace(p_id_race IN NUMBER, p_name VARCHAR2, p_id_pet_type IN NUMBER) IS
    BEGIN
        UPDATE race
        SET    "name"        = p_name,
               id_pet_type = p_id_pet_type
        WHERE  id_race = p_id_race;
        COMMIT;
    END;

    PROCEDURE updateStatus(p_id_status IN NUMBER, p_status_type VARCHAR2) IS
    BEGIN
        UPDATE status
        SET    status_type = p_status_type
        WHERE  id_status = p_id_status;
        COMMIT;
    END;

    PROCEDURE updateColor(p_id_color IN NUMBER, p_name VARCHAR2) IS
    BEGIN
        UPDATE color
        SET    "name" = p_name
        WHERE  id_color = p_id_color;
        COMMIT;
    END;

    PROCEDURE updateValueType(p_id_value_type IN NUMBER, p_type VARCHAR2) IS
    BEGIN
        UPDATE value_type
        SET    "type" = p_type
        WHERE  id_value_type = p_id_value_type;
        COMMIT;
    END;


    -- ==================== GET ====================

    FUNCTION getCurrency RETURN SYS_REFCURSOR 
    IS
        v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT * FROM currency;
        RETURN v_cursor;
    END;
    

    FUNCTION getProvince RETURN SYS_REFCURSOR IS
        v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT * FROM province;
        RETURN v_cursor;
    END;

    FUNCTION getCanton RETURN SYS_REFCURSOR IS
        v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT * FROM canton;
        RETURN v_cursor;
    END;

    FUNCTION getDistrict RETURN SYS_REFCURSOR IS
        v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT * FROM district;
        RETURN v_cursor;
    END;

    FUNCTION getPetType RETURN SYS_REFCURSOR IS
        v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT * FROM pet_type;
        RETURN v_cursor;
    END;

    FUNCTION getRace RETURN SYS_REFCURSOR IS
        v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT * FROM race;
        RETURN v_cursor;
    END;

    FUNCTION getStatus RETURN SYS_REFCURSOR IS
        v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT * FROM status;
        RETURN v_cursor;
    END;

    FUNCTION getColor RETURN SYS_REFCURSOR IS
        v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT * FROM color;
        RETURN v_cursor;
    END;

    FUNCTION getValueType RETURN SYS_REFCURSOR IS
        v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT * FROM value_type;
        RETURN v_cursor;
    END;

END adminCatalogs;