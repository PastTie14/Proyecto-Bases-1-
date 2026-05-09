CREATE OR REPLACE PACKAGE BODY adminConsult AS
/*
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
*/
FUNCTION getBlackListReport RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
        SELECT u.email, ad.first_name, NVL(ad.second_name, 'None'), ad.first_surname, 
                ad.second_surname, NVL(r.score, 0), uxb.reason FROM user_x_black_list uxb
        
        INNER JOIN black_list bl
        ON bl.id_report = uxb.id_report
        
        -- the user is the reporter
        INNER JOIN "user" u
        ON bl.id_user = u.id_user
        
        -- adopter the reported
        INNER JOIN adopter ad
        ON uxb.id_user = ad.id_user
        
        LEFT JOIN rating r
        ON ad.id_user = r.id_adopter;

        RETURN v_cursor;
    END;

/*FUNCTION getMatches(pIdType IN NUMBER,pIdRace IN NUMBER) RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
        SELECT  FROM matches
        
        INNER JOIN 
        ON 
        
        WHERE 
        AND 
        
        GROUP BY 
        ORDER BY ;

        RETURN v_cursor;
    END;*/
/*
FUNCTION getPetNecessaryTreatments(pMin IN NUMBER, pMax IN NUMBER) RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
        SELECT p.first_name, d."name", t."name" FROM pet p
        
        INNER JOIN pet_extra_info pei
        ON p.id_pet = pei.id_pet
        
        INNER JOIN medic_sheet ms
        ON pei.id_pet_extra_info = ms.id_pet_extra_info
        
        INNER JOIN
        
        WHERE 
        AND 
        
        GROUP BY 
        ORDER BY ;

        RETURN v_cursor;
    END;*/

FUNCTION getCompatibleCribHouses(pIdPetType IN NUMBER) RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
        SELECT cb.id_user, cb."name", cb.requires_donations, pt."name" FROM crib_house cb
        
        INNER JOIN pet_type_x_crib_house ptxcb
        ON cb.id_user = ptxcb.id_crib_house
        
        INNER JOIN pet_type pt
        ON ptxcb.id_pet_type = pt.id_pet_type
        
        WHERE pt.id_pet_type = NVL(pIdPetType, pt.id_pet_type)

        ORDER BY cb.id_user;
        RETURN v_cursor;
    END;
END adminConsult;