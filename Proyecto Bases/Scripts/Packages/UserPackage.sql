CREATE OR REPLACE PACKAGE adminUser IS

-- INSERT
PROCEDURE insertUser(pIdUser OUT NUMBER, pEmail VARCHAR2, pPassword VARCHAR2);
PROCEDURE insertAssociation(pIdUser IN NUMBER, pName VARCHAR2);
PROCEDURE insertAdopter(pIdUser IN NUMBER, pFirstName VARCHAR2, pSecondName VARCHAR2, pFirstSurname VARCHAR2, pSecondSurname VARCHAR2);
PROCEDURE insertRescuer(pIdUser IN NUMBER, pFirstName VARCHAR2, pSecondName VARCHAR2, pFirstSurname VARCHAR2, pSecondSurname VARCHAR2);
PROCEDURE insertCribHouse(pIdUser IN NUMBER, pName VARCHAR2, pRequiresDonations IN NUMBER);
PROCEDURE insertLog(pIdLog IN NUMBER, pChangeDate DATE, pChangeBy VARCHAR2, pTableName VARCHAR2, pFieldName VARCHAR2, pPreviousValue VARCHAR2,
                    pCurrentValue VARCHAR2, pIdUser IN NUMBER);

-- UPDATE
PROCEDURE updateUser(pIdUser IN NUMBER, pEmail IN VARCHAR2, pPassword IN VARCHAR2);
PROCEDURE updateAssociation(pIdUser IN NUMBER, pName IN VARCHAR2);
PROCEDURE updateAdopter(pIdUser IN NUMBER, pFirstName IN VARCHAR2, pSecondName VARCHAR2, 
                        pFirstSurname VARCHAR2, pSecondSurname VARCHAR2);
PROCEDURE updateRescuer(pIdUser IN NUMBER, pFirstName IN VARCHAR2, pSecondName IN VARCHAR2, 
                        pFirstSurname IN VARCHAR2, pSecondSurname IN VARCHAR2);
PROCEDURE updateCribHouse(pIdUser IN NUMBER, pName IN VARCHAR2, pRequiresDonations IN NUMBER);

-- GET
FUNCTION getUser RETURN SYS_REFCURSOR;
FUNCTION getUserById(pIdUser IN NUMBER) RETURN SYS_REFCURSOR;
FUNCTION logIn(p_email IN VARCHAR2, p_password IN VARCHAR2) RETURN SYS_REFCURSOR;

FUNCTION getAssociation RETURN SYS_REFCURSOR;
FUNCTION getAssociationById(pIdAssociation IN NUMBER) RETURN SYS_REFCURSOR;

FUNCTION getAdopter RETURN SYS_REFCURSOR;
FUNCTION getAdopterById(pIdAdopter IN NUMBER) RETURN SYS_REFCURSOR;

FUNCTION getRescuer RETURN SYS_REFCURSOR;
FUNCTION getRescuerById(pIdRescuer IN NUMBER) RETURN SYS_REFCURSOR;

FUNCTION getCribHouse RETURN SYS_REFCURSOR;
FUNCTION getCribHouseById(pIdCribHouse IN NUMBER) RETURN SYS_REFCURSOR;

-- DELETE
PROCEDURE deleteUser(pidUser IN NUMBER);
PROCEDURE deleteAssociation(pidUser IN NUMBER);
PROCEDURE deleteAdopter(pidUser IN NUMBER);
PROCEDURE deleteRescuer(pidUser IN NUMBER);
PROCEDURE deleteCribHouse(pidUser IN NUMBER);

END adminUser;