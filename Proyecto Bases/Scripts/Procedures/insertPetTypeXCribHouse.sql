CREATE OR REPLACE PROCEDURE insertPetTypeXCribHouse(id_petType IN NUMBER, id_cribHouse IN NUMBER)

AS 
BEGIN
    INSERT INTO pet_type_x_crib_house
    VALUES(id_petType, id_cribHouse);
    COMMIT;
END insertPetTypeXCribHouse;