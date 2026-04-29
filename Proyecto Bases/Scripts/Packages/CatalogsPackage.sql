create or replace PACKAGE adminCatalogs IS
    -- INSERT
    PROCEDURE insertCurrency(p_id_currency IN NUMBER, p_name VARCHAR2, p_acronym VARCHAR2);
    PROCEDURE insertProvince(p_id_province IN NUMBER, p_name VARCHAR2);
    PROCEDURE insertCanton(p_id_canton IN NUMBER, p_name VARCHAR2, p_id_province IN NUMBER);
    PROCEDURE insertDistrict(p_id_district IN NUMBER, p_name VARCHAR2, p_id_canton IN NUMBER);
    PROCEDURE insertPetType(p_id_pet_type IN NUMBER, p_name VARCHAR2);
    PROCEDURE insertRace(p_id_race IN NUMBER, p_name VARCHAR2, p_id_pet_type IN NUMBER);
    PROCEDURE insertStatus(p_id_status IN NUMBER, p_status_type VARCHAR2);
    PROCEDURE insertColor(p_id_color IN NUMBER, p_name VARCHAR2);
    PROCEDURE insertValueType(p_id_value_type IN NUMBER, p_type VARCHAR2);

    -- UPDATE
    PROCEDURE updateCurrency(p_id_currency IN NUMBER, p_name VARCHAR2, p_acronym VARCHAR2);
    PROCEDURE updateProvince(p_id_province IN NUMBER, p_name VARCHAR2);
    PROCEDURE updateCanton(p_id_canton IN NUMBER, p_name VARCHAR2, p_id_province IN NUMBER);
    PROCEDURE updateDistrict(p_id_district IN NUMBER, p_name VARCHAR2, p_id_canton IN NUMBER);
    PROCEDURE updatePetType(p_id_pet_type IN NUMBER, p_name VARCHAR2);
    PROCEDURE updateRace(p_id_race IN NUMBER, p_name VARCHAR2, p_id_pet_type IN NUMBER);
    PROCEDURE updateStatus(p_id_status IN NUMBER, p_status_type VARCHAR2);
    PROCEDURE updateColor(p_id_color IN NUMBER, p_name VARCHAR2);
    PROCEDURE updateValueType(p_id_value_type IN NUMBER, p_type VARCHAR2);

    -- GET
    FUNCTION getCurrency RETURN SYS_REFCURSOR;
    FUNCTION getProvince RETURN SYS_REFCURSOR;
    FUNCTION getCanton RETURN SYS_REFCURSOR;
    FUNCTION getDistrict RETURN SYS_REFCURSOR;
    FUNCTION getPetType RETURN SYS_REFCURSOR;
    FUNCTION getRace RETURN SYS_REFCURSOR;
    FUNCTION getStatus RETURN SYS_REFCURSOR;
    FUNCTION getColor RETURN SYS_REFCURSOR;
    FUNCTION getValueType RETURN SYS_REFCURSOR;
END adminCatalogs;