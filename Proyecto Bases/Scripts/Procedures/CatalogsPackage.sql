CREATE OR REPLACE PACKAGE adminCatalogs IS

PROCEDURE insertCurrency(id_currency IN NUMBER, "name" VARCHAR2,acronym VARCHAR2);
PROCEDURE insertProvince(id_province IN NUMBER, "name" VARCHAR2);
PROCEDURE insertCanton(id_canton IN NUMBER, "name" VARCHAR2,id_province IN NUMBER);
PROCEDURE insertDistrict(id_district IN NUMBER, "name" VARCHAR2,id_canton IN NUMBER);
PROCEDURE insertPetType(id_pet_type IN NUMBER, "name" VARCHAR2);
PROCEDURE insertRace(id_race IN NUMBER, "name" VARCHAR2,id_pet_type IN NUMBER);
PROCEDURE insertStatus(id_status IN NUMBER, status_type VARCHAR2);
PROCEDURE insertColor(id_color IN NUMBER, "name" VARCHAR2);
PROCEDURE insertValueType(id_value_type IN NUMBER, "type" VARCHAR2);

END adminCatalogs;