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

FUNCTION getBlackListReport RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
        SELECT u.email AS Reporter_email, ad.first_name AS Reported_first_name, 
                NVL(ad.second_name, 'None') AS Reported_second_name, ad.first_surname AS Reported_first_surname, 
                ad.second_surname AS Reported_second_surname, NVL(r.score, 'None') AS Score, 
                uxb.reason FROM user_x_black_list uxb
        
        INNER JOIN black_list bl
        ON bl.id_report = uxb.id_report
        
        INNER JOIN "user" u
        ON bl.id_user = u.id_user
        
        INNER JOIN adopter ad
        ON uxb.id_user = ad.id_user
        
        LEFT JOIN rating r
        ON ad.id_user = r.id_adopter;

        RETURN v_cursor;
    END;


/*IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
        SELECT  FROM 
        
        INNER JOIN 
        ON 
        
        WHERE 
        AND 
        
        GROUP BY 
        ORDER BY ;

        RETURN v_cursor;
    END;*/

END adminConsult;