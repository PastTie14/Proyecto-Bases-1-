CREATE OR REPLACE PACKAGE BODY adminStats AS
    
FUNCTION getPetsByTypeAndStatus(pIdType IN NUMBER, pIdStatus IN NUMBER,
                                    pStartDate IN DATE, pEndDate IN DATE) RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT pt."name", s.status_type, COUNT(p.id_pet) AS pet_count FROM pet_type pt
                    
            CROSS JOIN status s -- cartesian product
            
            LEFT JOIN pet p 
            ON pt.id_pet_type = p.id_pet_type
            AND s.id_status = p.id_status
            AND p.createdAt BETWEEN NVL(pStartDate, TRUNC(SYSDATE, 'YYYY')) -- default: start of this year
                                     AND NVL(pEndDate, SYSDATE) -- default: today
            WHERE pt.id_pet_type = NVL(pIdType, pt.id_pet_type)
            AND s.id_status = NVL(pIdStatus, s.id_status)
            
            GROUP BY pt."name", s.status_type
            ORDER BY pt."name", s.status_type;
        RETURN v_cursor;
    END;
    
FUNCTION getDonationsByAssociation(pStartDate IN DATE, pEndDate IN DATE) RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;    
    BEGIN
        OPEN v_cursor FOR
            SELECT a."name", COUNT (d.id_donation) AS donation_count FROM association a
            
            LEFT JOIN donation d -- LEFT to include associations without donations
            ON a.id_user = d.id_association
            -- WHERE isn't here to avoid invalidating the LEFT JOIN
            AND d.createdAt BETWEEN NVL(pStartDate, TRUNC(SYSDATE, 'YYYY')) -- default: start of this year
                                                    AND NVL(pEndDate, SYSDATE) -- default: today
            GROUP BY a."name"  
            ORDER BY a."name";
        RETURN v_cursor;
    END;
    
FUNCTION getDonationsByCribHouse(pStartDate IN DATE, pEndDate IN DATE) RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;    
    BEGIN
        OPEN v_cursor FOR
            SELECT cb."name", COUNT (d.id_donation) AS donation_count FROM crib_house cb
            
            LEFT JOIN donation d -- LEFT to include crib houses without donations
            ON cb.id_user = d.id_crib_house
            -- WHERE isn't here to avoid invalidating the LEFT JOIN
            AND d.createdAt BETWEEN NVL(pStartDate, TRUNC(SYSDATE, 'YYYY')) -- default: start of this year
                                                    AND NVL(pEndDate, SYSDATE) -- default: today
            GROUP BY cb."name"
            ORDER BY cb."name";
        RETURN v_cursor;
    END;
    
FUNCTION getAdoptedVSUnadopted(pIdType IN NUMBER, pIdRace IN NUMBER) RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;    
    BEGIN
        OPEN v_cursor FOR
            SELECT s.status_type, pt."name", r."name", COUNT(p.id_pet) AS count_adopted FROM pet p
            
            INNER JOIN pet_type pt
            ON p.id_pet_type = pt.id_pet_type
            
            INNER JOIN race r
            ON pt.id_pet_type = r.id_pet_type
            
            INNER JOIN status s
            ON p.id_status = s.id_status
            
            WHERE pt.id_pet_type = NVL(pIdType, pt.id_pet_type)
            AND r.id_race = NVL(pIdRace, r.id_race)
            AND s.id_status = 1
            
            UNION
            
            SELECT s.status_type, pt."name", r."name", COUNT(p.id_pet) AS count_unadopted FROM pet p
            
            INNER JOIN pet_type pt
            ON p.id_pet_type = pt.id_pet_type
            
            INNER JOIN race r
            ON pt.id_pet_type = r.id_pet_type
            
            INNER JOIN status s
            ON p.id_status = s.id_status
            
            WHERE pt.id_pet_type = NVL(pIdType, pt.id_pet_type)
            AND r.id_race = NVL(pIdRace, r.id_race)
            AND s.id_status = 2;
        RETURN v_cursor;
    END;
    
FUNCTION getUnadoptedPetsByAgeRange RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT '0-1' age_range, COUNT(p.id_pet) AS pet_count
            FROM pet p
            
            INNER JOIN status s
            ON p.id_status = s.id_status
            
            WHERE s.id_status = 2
            AND TRUNC((SYSDATE - p.birth_date) / 365) BETWEEN 0 AND 1
            
            UNION
            
            SELECT '1-5' age_range, COUNT(p.id_pet) AS pet_count
            FROM pet p
            
            INNER JOIN status s
            ON p.id_status = s.id_status
            
            WHERE s.id_status = 2
            AND TRUNC((SYSDATE - p.birth_date) / 365) BETWEEN 1 AND 5
            
            UNION
            
            SELECT '5-9' age_range, COUNT(p.id_pet) AS pet_count
            FROM pet p
            
            INNER JOIN status s
            ON p.id_status = s.id_status
            
            WHERE s.id_status = 2
            AND TRUNC((SYSDATE - p.birth_date) / 365) BETWEEN 5 AND 9
            
            UNION
            
            SELECT '10-12' age_range, COUNT(p.id_pet) AS pet_count
            FROM pet p
            
            INNER JOIN status s
            ON p.id_status = s.id_status
            
            WHERE s.id_status = 2
            AND TRUNC((SYSDATE - p.birth_date) / 365) BETWEEN 10 AND 12
            
            UNION
            
            SELECT '>12' age_range, COUNT(p.id_pet) AS pet_count
            FROM pet p
            
            INNER JOIN status s
            ON p.id_status = s.id_status
            
            WHERE s.id_status = 2
            AND TRUNC((SYSDATE - p.birth_date) / 365) > 12;
        RETURN v_cursor;
    END;
    
FUNCTION getBestRescuersAndAdopters(pStartDate IN DATE, pEndDate IN DATE) RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            -- all of them have NVL to include names from both rescuer and adopter
            SELECT NVL(r.first_name, a.first_name) AS first_name, 
                NVL(r.second_name, a.second_name) AS second_name,
                NVL(r.first_surname, a.first_surname) AS first_surname,
                NVL(r.second_surname, a.second_surname) AS second_surname, 
                NVL(r.rescues_count, 0) AS rescues, 
                NVL(a.adoptions_count, 0) AS adoptions
                
            FROM
            -- selects from rescuer
            (
            SELECT r.first_name, r.second_name, r.first_surname, r.second_surname, COUNT(p.id_pet) AS rescues_count 
            FROM rescuer r
                
            INNER JOIN pet p
            ON r.id_user = p.id_rescuer
            
            WHERE p.createdAt BETWEEN NVL(pStartDate, TRUNC(SYSDATE, 'YYYY')) -- default: start of this year
                                                    AND NVL(pEndDate, SYSDATE) -- default: today
            GROUP BY r.first_name, r.second_name, r.first_surname, r.second_surname
            ) r
    
            FULL OUTER JOIN -- outer join for all combinations
            -- selects from adopter
            (
            SELECT a.first_name, a.second_name, a.first_surname, a.second_surname, COUNT(af.id_pet) AS adoptions_count 
            FROM adopter a
            
            INNER JOIN adoption_form af
            ON a.id_user = af.id_adopter
            
            WHERE af.createdAt BETWEEN NVL(pStartDate, TRUNC(SYSDATE, 'YYYY')) -- default: start of this year
                                                    AND NVL(pEndDate, SYSDATE) -- default: today
            GROUP BY a.first_name, a.second_name, a.first_surname, a.second_surname
            ) a
            
            ON a.first_name = a.first_name
            AND NVL(r.second_name, '') = NVL(a.second_name, '') -- in cases where the user has no second name
            AND r.first_surname = a.first_surname 
            AND r.second_surname = a.second_surname
            ORDER BY rescues DESC, adoptions DESC;
        RETURN v_cursor;
    END;
END adminStats;