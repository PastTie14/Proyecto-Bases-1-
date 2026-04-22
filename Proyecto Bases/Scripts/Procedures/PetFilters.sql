CREATE OR REPLACE PROCEDURE getFilters (pIdChip IN NUMBER, pIdDistrict IN NUMBER, pIdCanton IN NUMBER,
                                        pIdProvince IN NUMBER, pIdStatus IN NUMBER, pIdPetType IN NUMBER, 
                                        pIdRace IN NUMBER, pIdColor IN NUMBER, petCursor OUT SYS_REFCURSOR)

AS
BEGIN 
    OPEN petCursor FOR
        SELECT id_chip, id_district, id_canton, id_province, id_status, id_pet_type, id_race, id_color
        FROM pet p
        INNER JOIN identification_chip ic
        ON p.id_pet = ic.id_pet
        
        INNER JOIN district d
        ON p.id_pet = d.id_pet
        
        INNER JOIN canton c
        ON d.id_canton = c.id_canton
        
        INNER JOIN province pr
        ON c_id_province = pr.id_province
        
        INNER JOIN status s
        ON p.id_status = s.id_status
        
        INNER JOIN pet_type pt
        ON p.id_pet_type = pt.id_pet_type
        
        INNER JOIN race r
        ON p.id_race = r.id_race
        
        INNER JOIN color_x_pet cxp
        ON p.id_pet = cxp.id_pet
        
        INNER JOIN color c
        ON cxp.id_color = c.id_color
        
        WHERE ic.id_chip = NVL(pIdChip, ic.id_chip)
        AND d.id_district = NVL(pIdDistrict, d.id_district)
        AND c.id_canton = NVL(pIdCanton, c.id_canton)
        AND pr.id_province = NVL(pIdProvince, pr.id_province)
        AND s.id_status = NVL(pIdStatus, s.id_status)
        AND pt.id_pet_type = NVL(pIdPetType, pt.id_pet_type)
        AND r.id_race = NVL(pIdRace, r.id_race)
        AND c.id_color = NVL(pIdColor, c.id_color);
END getFilters;