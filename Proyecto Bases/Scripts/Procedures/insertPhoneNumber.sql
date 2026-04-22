CREATE OR REPLACE PROCEDURE insertPhoneNumber(id_phone IN NUMBER, "number" IN NUMBER,
                                                id_user IN NUMBER, id_pet IN NUMBER, 
                                                id_veterinarian IN NUMBER)

AS 
BEGIN
    INSERT INTO phone_number
    VALUES(id_phone, "number", id_user, id_pet, id_veterinarian);
    
    COMMIT;
END insertPhoneNumber;