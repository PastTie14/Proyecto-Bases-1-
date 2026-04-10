CREATE OR REPLACE PROCEDURE insertPet(id_pet IN NUMBER, picture IN VARCHAR2, first_name IN VARCHAR2,
                                       birth_date IN DATE, date_lost IN DATE, date_found IN DATE,
                                       email IN DATE, createdBy IN VARCHAR2, createdAt IN DATE,
                                       modifiedBy IN VARCHAR2, modifiedAt IN DATE, id_status IN NUMBER,
                                       id_pet_type IN NUMBER, id_rescuer NUMBER)
AS
BEGIN
    INSERT INTO pet
    VALUES(id_user, picture, first_name, birth_date, date_lost, date_found, 
            email, createdBy, createdAt, modifiedBy, modifiedAt,
            id_status, id_pet_type, id_rescuer);
        COMMIT;
END insertPet;