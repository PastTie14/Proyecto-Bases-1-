CREATE OR REPLACE PROCEDURE insertParameters(id_parameter IN NUMBER, "value" IN VARCHAR2,
                                            id_match IN NUMBER, id_value_type IN NUMBER)

AS 
BEGIN
    INSERT INTO parameters
    VALUES(id_parameter, "value", id_match, id_value_type);
    COMMIT;
END insertParameters;