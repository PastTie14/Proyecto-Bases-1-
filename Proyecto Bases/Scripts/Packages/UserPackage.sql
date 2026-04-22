CREATE OR REPLACE PACKAGE adminUser IS

PROCEDURE insertUser(id_user IN NUMBER,email VARCHAR2,"password" VARCHAR2, createdBy VARCHAR2, createdAt DATE, modifiedBy VARCHAR2, modifiedAt DATE);
PROCEDURE insertAssociation(id_user IN NUMBER, "name" VARCHAR2);
PROCEDURE insertAdopter(id_user IN NUMBER, first_name VARCHAR2, second_name VARCHAR2, first_surname VARCHAR2, second_surname VARCHAR2);
PROCEDURE insertRescuer(id_user IN NUMBER, first_name VARCHAR2, second_name VARCHAR2, first_surname VARCHAR2, second_surname VARCHAR2);
PROCEDURE insertCribHouse(id_user IN NUMBER, "name" VARCHAR2, requires_donations IN NUMBER, accepted_size IN NUMBER);
PROCEDURE insertLog(id_log IN NUMBER, changeDate DATE, changeBy VARCHAR2, tableName VARCHAR2, fieldName VARCHAR2, previousValue VARCHAR2,
currentValue VARCHAR2, id_user IN NUMBER);

END adminUser;