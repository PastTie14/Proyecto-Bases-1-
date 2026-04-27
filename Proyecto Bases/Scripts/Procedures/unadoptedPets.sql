CREATE OR REPLACE PROCEDURE unadoptedPets(unadopted_cursor OUT SYS_REFCURSOR)
AS
BEGIN
    OPEN unadopted_cursor FOR
    SELECT p.createdAt, s.id_status
    FROM pet p
    
    INNER JOIN status s
    ON p.id_status = s.id_status
    
    -- unadopted pets registered in the last 2 months
    WHERE p.createdAt BETWEEN SYSDATE - 60 AND SYSDATE 
    AND s.id_status = 1
    ORDER BY p.createdAt;
END unadoptedPets;