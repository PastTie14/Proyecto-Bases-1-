CREATE OR REPLACE PACKAGE adminUser IS

PROCEDURE insertUser(pIdUser IN NUMBER, pEmail VARCHAR2, pPassword VARCHAR2);
PROCEDURE insertAssociation(pIdUser IN NUMBER, pName VARCHAR2);
PROCEDURE insertAdopter(pIdUser IN NUMBER, pFirstName VARCHAR2, pSecondName VARCHAR2, pFirstSurname VARCHAR2, pSecondSurname VARCHAR2);
PROCEDURE insertRescuer(pIdUser IN NUMBER, pFirstName VARCHAR2, pSecondName VARCHAR2, pFirstSurname VARCHAR2, pSecondSurname VARCHAR2);
PROCEDURE insertCribHouse(pIdUser IN NUMBER, pName VARCHAR2, pRequiresDonations IN NUMBER, pAcceptedSize IN NUMBER);
PROCEDURE insertLog(pIdLog IN NUMBER, pChangeDate DATE, pChangeBy VARCHAR2, pTableName VARCHAR2, pFieldName VARCHAR2, pPreviousValue VARCHAR2,
                    pCurrentValue VARCHAR2, pIdUser IN NUMBER);

FUNCTION getAssociation RETURN SYS_REFCURSOR;
FUNCTION getAdopter RETURN SYS_REFCURSOR;
FUNCTION getRescuer RETURN SYS_REFCURSOR;
FUNCTION getCribHouse RETURN SYS_REFCURSOR;

END adminUser;