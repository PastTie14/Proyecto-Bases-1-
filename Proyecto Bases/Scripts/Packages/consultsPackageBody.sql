CREATE OR REPLACE PACKAGE BODY adminConsult AS
/*
FUNCTION getDonations(pStartDate IN DATE, pEndDate IN DATE, pIdDonor IN NUMBER, 
                        pIdAssociation IN NUMBER) RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
        SELECT d.amount, d.id_donnor, d.createdAt, a."name", COUNT(1) OVER () FROM donation d
        
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
                ad.second_surname, NVL(r.score, 0), uxb.reason, COUNT(1) OVER () FROM user_x_black_list uxb
        
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

FUNCTION getMatches(pIdLostPet IN NUMBER, pIdFoundPet IN NUMBER) RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
        SELECT (
                (
                -- inspired by these posts: https://forums.oracle.com/ords/apexds/post/calculating-percentages-3532
                -- https://stackoverflow.com/questions/77622815/create-a-percentage-formula-with-using-a-case-when-expression
                
                -- if the ids are the same, add 1 and sum the next one, then divide
                -- by the total (3) and multiply by 100 to get the percentage
                CASE WHEN p1.id_size = p2.id_size 
                THEN 1 ELSE 0 END + 
                
                CASE WHEN p1.id_race = p2.id_race
                THEN 1 ELSE 0 END +
                
                CASE WHEN p1.id_district = p2.id_district
                THEN 1 ELSE 0 END
                ) / 3
            ) * 100, COUNT(1) OVER () FROM pet p1
        
        CROSS JOIN pet p2 -- cartesian product to get all combinations
        -- https://www.datacamp.com/tutorial/cartesian-product
        
        /*
        INNER JOIN color_x_pet cxp1
        ON p1.id_pet = cxp.id_pet
        
        INNER JOIN color_x_pet cxp2
        ON p2.id_pet = cxp2.id_pet
        */
        WHERE p1.id_pet = pIdLostPet
        AND p2.id_pet = pIdFoundPet;

        RETURN v_cursor;
    END;

FUNCTION getPetNecessaryTreatments(pIdPet IN NUMBER) RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
        SELECT p.first_name, COUNT(dxms.id_disease), COUNT(*) OVER () FROM pet p
        
        INNER JOIN pet_extra_info pei
        ON p.id_pet = pei.id_pet
        
        INNER JOIN medic_sheet ms
        ON pei.id_pet_extra_info = ms.id_pet_extra_info
        
        INNER JOIN disease_x_medic_sheet dxms
        ON ms.id_medic_sheet = dxms.id_medic_sheet
        
        INNER JOIN disease d
        ON dxms.id_disease = d.id_disease
        
        WHERE p.id_pet = pIdPet
        
        GROUP BY p.first_name;

        RETURN v_cursor;
    END;

FUNCTION getCompatibleCribHouses(pIdPetType IN NUMBER) RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
        SELECT cb.id_user, cb."name", cb.requires_donations, pt."name", COUNT(1) OVER () FROM crib_house cb
        
        INNER JOIN pet_type_x_crib_house ptxcb
        ON cb.id_user = ptxcb.id_crib_house
        
        INNER JOIN pet_type pt
        ON ptxcb.id_pet_type = pt.id_pet_type
        
        WHERE pt.id_pet_type = NVL(pIdPetType, pt.id_pet_type)
        
        GROUP BY cb.id_user, cb."name", cb.requires_donations, pt."name"
        ORDER BY cb.id_user;
        
        RETURN v_cursor;
    END;
END adminConsult;