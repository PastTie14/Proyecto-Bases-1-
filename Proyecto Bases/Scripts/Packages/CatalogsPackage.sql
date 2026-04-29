CREATE OR REPLACE PACKAGE adminCatalogs IS

PROCEDURE insertCurrency(pIdCurrency IN NUMBER, pName VARCHAR2, pAcronym VARCHAR2);
PROCEDURE insertProvince(pIdProvince IN NUMBER, pName VARCHAR2);
PROCEDURE insertCanton(pIdCanton IN NUMBER, pName VARCHAR2, pIdProvince IN NUMBER);
PROCEDURE insertDistrict(pIdDistrict IN NUMBER, pName VARCHAR2, pIdCanton IN NUMBER);
PROCEDURE insertPetType(pIdPetType IN NUMBER, pName VARCHAR2);
PROCEDURE insertRace(pIdRace IN NUMBER, pName VARCHAR2, pIdPetType IN NUMBER);
PROCEDURE insertStatus(pIdStatus IN NUMBER, pStatusType VARCHAR2);
PROCEDURE insertColor(pIdColor IN NUMBER, pName VARCHAR2);
PROCEDURE insertValueType(pIdValueType IN NUMBER, pType VARCHAR2);

END adminCatalogs;