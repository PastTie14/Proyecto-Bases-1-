CREATE OR REPLACE PROCEDURE insertDiseaseXMedicSheet(id_treatment IN NUMBER, id_disease IN NUMBER)

AS 
BEGIN
    INSERT INTO disease_x_medic_sheet
    VALUES(id_treatment, id_disease);
    COMMIT;
END insertDiseaseXMedicSheet;