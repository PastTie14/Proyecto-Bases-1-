CREATE OR REPLACE PACKAGE BODY adminConsult AS

FUNCTION getDonations(pStartDate IN DATE, pEndDate IN DATE, pIdDonor IN NUMBER, 
                        pIdAssociation IN NUMBER) RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
        SELECT d.amount, d.id_donnor, d.createdAt, a."name", COUNT(1) OVER () FROM donation d
        -- COUNT(1) OVER () to count all registers
        -- https://learnsql.com/blog/count-over-partition-by/
        
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
        SELECT ad.id_user, ua.email, ad.first_name, NVL(ad.second_name, 'None'), ad.first_surname, 
                ad.second_surname, NVL(r.score, 0), uxb.reason, COUNT(1) OVER () FROM user_x_black_list uxb
        
        INNER JOIN black_list bl
        ON bl.id_report = uxb.id_report
        
        -- the user is the reporter
        INNER JOIN "user" u
        ON bl.id_user = u.id_user
        
        -- adopter is the reported
        INNER JOIN adopter ad
        ON uxb.id_user = ad.id_user
        
        INNER JOIN "user" ua
        ON ad.id_user = ua.id_user
        
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

FUNCTION getPetNecessaryTreatments(pMin IN NUMBER, pMax IN NUMBER) RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
        SELECT p.id_pet, p.first_name, pt."name", cs.status_type, 
                NVL(COUNT(txd.id_disease), 0), NVL(COUNT(txd.id_treatment), 0) AS treatment_count, 
                COUNT(*) OVER () FROM pet p
        
        INNER JOIN race r
        ON p.id_race = r.id_race
        
        INNER JOIN pet_type pt
        ON r.id_pet_type = pt.id_pet_type
        
        INNER JOIN pet_extra_info pei
        ON p.id_pet = pei.id_pet
        
        INNER JOIN current_status cs
        ON pei.id_current_status = cs.id_current_status
        
        INNER JOIN medic_sheet ms
        ON pei.id_pet_extra_info = ms.id_pet_extra_info
        
        INNER JOIN disease_x_medic_sheet dxms
        ON ms.id_medic_sheet = dxms.id_medic_sheet
        
        INNER JOIN disease d
        ON dxms.id_disease = d.id_disease
        
        INNER JOIN treatment_x_disease txd
        ON d.id_disease = txd.id_disease
        
        GROUP BY p.id_pet, p.first_name, pt."name", cs.status_type

        HAVING NVL(COUNT(txd.id_treatment), 0) BETWEEN pMin AND pMax
        ORDER BY treatment_count;

        RETURN v_cursor;
    END;

FUNCTION getCompatibleCribHouses(pIdPetType IN NUMBER) RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
        SELECT cb.id_user, cb."name", u.email, cb.requires_donations, pt."name", s."name", COUNT(1) OVER () FROM crib_house cb
        
        INNER JOIN pet_type_x_crib_house ptxcb
        ON cb.id_user = ptxcb.id_crib_house
        
        INNER JOIN pet_type pt
        ON ptxcb.id_pet_type = pt.id_pet_type
        
        INNER JOIN "user" u
        ON cb.id_user = u.id_user
        
        INNER JOIN size_x_crib_house sxcb
        ON cb.id_user = sxcb.id_crib_house
        
        INNER JOIN "size" s
        ON sxcb.id_size = s.id_size
        
        WHERE pt.id_pet_type = NVL(pIdPetType, pt.id_pet_type)
        
        GROUP BY cb.id_user, cb."name", u.email, cb.requires_donations, pt."name", s."name"
        ORDER BY cb.id_user;
        
        RETURN v_cursor;
    END;
    
FUNCTION getBestRescuersAndAdopters(pStartDate IN DATE, pEndDate IN DATE) RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            -- all of them have NVL to include names from both rescuer and adopter
            SELECT NVL(r.id_user, a.id_user) AS id_user,
                NVL(r.email, a.email) AS email,
                NVL(r.first_name, a.first_name) AS first_name, 
                NVL(r.second_name, a.second_name) AS second_name,
                NVL(r.first_surname, a.first_surname) AS first_surname,
                NVL(r.second_surname, a.second_surname) AS second_surname, 
                NVL(r.rescues_count, 0) AS rescues, 
                NVL(a.adoptions_count, 0) AS adoptions,
                NVL(r.rescues_count, 0) + NVL(a.adoptions_count, 0) AS total_registers
                
            FROM
            -- selects from rescuer
            (
            SELECT r.id_user, u.email, r.first_name, r.second_name, r.first_surname, r.second_surname, COUNT(p.id_pet) AS rescues_count 
            FROM rescuer r
                
            INNER JOIN pet p
            ON r.id_user = p.id_user
            
            INNER JOIN "user" u
            ON r.id_user = u.id_user
            
            GROUP BY r.id_user, u.email, r.first_name, r.second_name, r.first_surname, r.second_surname
            ) r
    
            FULL OUTER JOIN -- outer join for all combinations
            -- selects from adopter
            (
            SELECT a.id_user, u.email, a.first_name, a.second_name, a.first_surname, a.second_surname, COUNT(af.id_pet) AS adoptions_count 
            FROM adopter a
            
            INNER JOIN adoption_form af
            ON a.id_user = af.id_adopter
            
            INNER JOIN "user" u
            ON a.id_user = u.id_user
            
            GROUP BY a.id_user, u.email, a.first_name, a.second_name, a.first_surname, a.second_surname
            ) a
            
            ON r.first_name = a.first_name
            AND NVL(r.second_name, '') = NVL(a.second_name, '') -- in cases where the user has no second name
            AND r.first_surname = a.first_surname 
            AND r.second_surname = a.second_surname
            ORDER BY rescues DESC, adoptions DESC;
        RETURN v_cursor;
    END;

END adminConsult;