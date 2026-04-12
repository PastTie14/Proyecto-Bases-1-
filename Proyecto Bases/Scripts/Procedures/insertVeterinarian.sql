CREATE OR REPLACE PROCEDURE insertVeterinarian(id_veterinarian IN NUMBER, first_name VARCHAR2, second_name VARCHAR2, first_surname VARCHAR2,
second_surname VARCHAR2, clinic_name VARCHAR2)

AS
BEGIN
    INSERT INTO veterinarian
    VALUES(id_veterinarian, first_name, second_name, first_surname, second_surname, clinic_name);
    COMMIT;
END insertVeterinarian;