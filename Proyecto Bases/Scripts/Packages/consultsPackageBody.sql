CREATE OR REPLACE PACKAGE BODY adminConsult AS

FUNCTION getDonations(pStartDate IN DATE, pEndDate IN DATE, pIdDonor IN NUMBER, 
                        pIdAssociation IN NUMBER) RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
        SELECT d.amount, d.id_donnor, d.createdAt, a."name" FROM donation d
        
        INNER JOIN association a
        ON d.id_association = a.id_user
        
        WHERE d.createdAt BETWEEN NVL(pStartDate, TRUNC(SYSDATE, 'YYYY')) -- default: start of this year
                                     AND NVL(pEndDate, SYSDATE) -- default: today
        AND d.id_donnor = NVL(pIdDonor, d.id_donnor)
        AND d.id_association = NVL(pIdAssociation, d.id_association)
        
        GROUP BY d.amount, d.id_donnor, d.createdAt, a."name"
        ORDER BY d.amount;

        RETURN v_cursor;
    END;

END adminConsult;