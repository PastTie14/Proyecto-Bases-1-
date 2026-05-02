CREATE OR REPLACE PACKAGE adminPhoneNumber IS

-- INSERT
PROCEDURE insertPhoneNumber(pIdPhone IN NUMBER, pNumber IN NUMBER,
                                                pIdUser IN NUMBER, pIdPet IN NUMBER, 
                                                pIdVeterinarian IN NUMBER);

-- GET 
FUNCTION getPhoneNumber RETURN SYS_REFCURSOR;
FUNCTION getPhoneNumberById(pIdPhone IN NUMBER) RETURN SYS_REFCURSOR;
FUNCTION getUserPhones(pIdUser IN NUMBER) RETURN SYS_REFCURSOR;
FUNCTION getPetPhones(pIdPet IN NUMBER) RETURN SYS_REFCURSOR;
FUNCTION getVeterinarianPhones(pIdVeterinarian IN NUMBER) RETURN SYS_REFCURSOR;

-- DELETE
PROCEDURE deletePhoneNumber(pIdPhone IN NUMBER);

END adminPhoneNumber;