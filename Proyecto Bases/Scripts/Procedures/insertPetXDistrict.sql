CREATE OR REPLACE PROCEDURE insertPetXDistrict(id_pet IN NUMBER, id_district IN NUMBER)

AS 
BEGIN
    INSERT INTO pet_x_district
    VALUES(id_pet, id_district);
    
    COMMIT;
END insertPetXDistrict;