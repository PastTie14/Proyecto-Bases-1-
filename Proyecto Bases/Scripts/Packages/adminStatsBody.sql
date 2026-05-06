CREATE OR REPLACE PACKAGE BODY adminStats AS
    
FUNCTION getPetsByTypeAndStatus(pIdType IN NUMBER, pIdStatus IN NUMBER,
                                    pStartDate IN DATE, pEndDate IN DATE) RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT pt.id_pet_type, pt."name", s.id_status, s.status_type,
                    COUNT(p.id_pet) AS pet_count FROM pet_type pt
                    
            CROSS JOIN status s -- cartesian product
            
            LEFT JOIN pet p 
            ON pt.id_pet_type = p.id_pet_type
            AND s.id_status = p.id_status
            AND p.createdAt BETWEEN NVL(pStartDate, TRUNC(SYSDATE, 'YYYY')) -- default: start of this year
                                     AND NVL(pEndDate, SYSDATE) -- default: today
            WHERE pt.id_pet_type = NVL(pIdType, pt.id_pet_type)
            AND s.id_status = NVL(pIdStatus, s.id_status)
            
            GROUP BY pt.id_pet_type, pt."name", s.id_status, s.status_type
            ORDER BY pt.id_pet_type, pt."name", s.id_status, s.status_type;
        RETURN v_cursor;
    END;
    
FUNCTION getDonationsByAssociation(pStartDate IN DATE, pEndDate IN DATE) RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;    
    BEGIN
        OPEN v_cursor FOR
            SELECT a.id_user, a."name", COUNT (d.id_donation) AS donation_count FROM association a
            
            LEFT JOIN donation d -- LEFT to include associations without donations
            ON a.id_user = d.id_association
            -- WHERE isn't here to avoid invalidating the LEFT JOIN
            AND d.created_at BETWEEN NVL(pStartDate, TRUNC(SYSDATE, 'YYYY')) -- default: start of this year
                                                    AND NVL(pEndDate, SYSDATE) -- default: today
            GROUP BY a.id_user, a."name"    
            ORDER BY a."name";
        RETURN v_cursor;
    END;
    
FUNCTION getDonationsByCribHouse(pStartDate IN DATE, pEndDate IN DATE) RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;    
    BEGIN
        OPEN v_cursor FOR
            SELECT cb.id_user, cb."name", COUNT (d.id_donation) AS donation_count FROM crib_house cb
            
            LEFT JOIN donation d -- LEFT to include crib houses without donations
            ON cb.id_user = d.id_crib_house
            -- WHERE isn't here to avoid invalidating the LEFT JOIN
            AND d.created_at BETWEEN NVL(pStartDate, TRUNC(SYSDATE, 'YYYY')) -- default: start of this year
                                                    AND NVL(pEndDate, SYSDATE) -- default: today
            GROUP BY cb.id_user, cb."name"    
            ORDER BY cb."name";
        RETURN v_cursor;
    END;
    
FUNCTION getUnadoptedPetsByAgeRange RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT p.first_name, '0-1' age_range, COUNT(p.id_pet) AS pet_count
            FROM pet p
            
            INNER JOIN status s
            ON p.id_status = s.id_status
            
            WHERE s.id_status = 2
            AND TRUNC((SYSDATE - p.birth_date) / 365) BETWEEN 0 AND 1
            
            UNION
            
            SELECT p.first_name, '1-5' age_range, COUNT(p.id_pet) AS pet_count
            FROM pet p
            
            INNER JOIN status s
            ON p.id_status = s.id_status
            
            WHERE s.id_status = 2
            AND TRUNC((SYSDATE - p.birth_date) / 365) BETWEEN 1 AND 5
            
            UNION
            
            SELECT p.first_name, '5-9' age_range, COUNT(p.id_pet) AS pet_count
            FROM pet p
            
            INNER JOIN status s
            ON p.id_status = s.id_status
            
            WHERE s.id_status = 2
            AND TRUNC((SYSDATE - p.birth_date) / 365) BETWEEN 5 AND 9
            
            UNION
            
            SELECT p.first_name, '10-12' age_range, COUNT(p.id_pet) AS pet_count
            FROM pet p
            
            INNER JOIN status s
            ON p.id_status = s.id_status
            
            WHERE s.id_status = 2
            AND TRUNC((SYSDATE - p.birth_date) / 365) BETWEEN 10 AND 12
            
            UNION
            
            SELECT p.first_name, '>12' age_range, COUNT(p.id_pet) AS pet_count
            FROM pet p
            
            INNER JOIN status s
            ON p.id_status = s.id_status
            
            WHERE s.id_status = 2
            AND TRUNC((SYSDATE - p.birth_date) / 365) > 12
            
            GROUP BY p.first_name, age_range, pet_count
            ORDER BY p.first_name, age_range, pet_count;
        RETURN v_cursor;
    END;
END adminStats;