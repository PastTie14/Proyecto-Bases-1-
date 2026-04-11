CREATE OR REPLACE PROCEDURE insertPetXColor(id_pet IN NUMBER, id_color IN NUMBER)

AS 
BEGIN
    INSERT INTO pet_x_color
    VALUES(id_pet, id_color);
    COMMIT;
END insertPetXColor;