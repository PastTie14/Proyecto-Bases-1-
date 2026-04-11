CREATE OR REPLACE PROCEDURE insertAdoptionForm(id_adoption IN NUMBER, notes VARCHAR2,
                                        adoption_date IN DATE, "reference" IN VARCHAR2,
                                        id_adopter IN NUMBER, id_pet IN NUMBER)

AS 
BEGIN
    INSERT INTO adoption_form
    VALUES(id_adoption, notes, adoption_date, "reference", id_adopter, id_pet);
    COMMIT;
END insertAdoptionForm;