CREATE OR REPLACE PACKAGE adminPet IS
                                       
PROCEDURE insertIdChip(id_chip IN NUMBER, chip_number IN VARCHAR2,
                                            registration_date IN DATE, id_pet IN NUMBER);
PROCEDURE insertPetXColor(id_pet IN NUMBER, id_color IN NUMBER);

PROCEDURE insertPetXDistrict(id_pet IN NUMBER, id_district IN NUMBER);

PROCEDURE insertPetTypeXCribHouse(id_petType IN NUMBER, id_cribHouse IN NUMBER);

END adminPet;
