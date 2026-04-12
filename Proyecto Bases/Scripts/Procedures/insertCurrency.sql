CREATE OR REPLACE PROCEDURE insertCurrency(id_currency IN NUMBER, "name" VARCHAR2,
                                        acronym VARCHAR2)

AS 
BEGIN
    INSERT INTO currency
    VALUES(id_currency,"name",acronym);
    COMMIT;
END insertCurrency;