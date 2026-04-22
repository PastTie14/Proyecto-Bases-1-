CREATE OR REPLACE PACKAGE BODY adminPet AS

PROCEDURE insertIdChip(id_chip IN NUMBER, chip_number IN VARCHAR2,
                                            registration_date IN DATE, id_pet IN NUMBER)

IS 
BEGIN
    INSERT INTO identification_chip
    VALUES(id_chip, chip_number, registration_date, id_pet);
    COMMIT;
END insertIdChip;

--=======================================================================================

PROCEDURE insertPetXColor(id_pet IN NUMBER, id_color IN NUMBER)

IS 
BEGIN
    INSERT INTO pet_x_color
    VALUES(id_pet, id_color);
    COMMIT;
END insertPetXColor;

--=======================================================================================

PROCEDURE insertPetXDistrict(id_pet IN NUMBER, id_district IN NUMBER)

IS 
BEGIN
    INSERT INTO pet_x_district
    VALUES(id_pet, id_district);
    
    COMMIT;
END insertPetXDistrict;

--=======================================================================================

PROCEDURE insertPetTypeXCribHouse(id_petType IN NUMBER, id_cribHouse IN NUMBER)

IS 
BEGIN
    INSERT INTO pet_type_x_crib_house
    VALUES(id_petType, id_cribHouse);
    COMMIT;
END insertPetTypeXCribHouse;

END adminPet;