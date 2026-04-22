CREATE OR REPLACE PROCEDURE getFilters (pIdChip IN NUMBER, pIdDistrict IN NUMBER, pIdCanton IN NUMBER,
                                        pIdProvince IN NUMBER, pIdStatus IN NUMBER, pIdPetType IN NUMBER,
                                        pIdRescuer IN NUMBER, pIdRace IN NUMBER, pIdColor IN NUMBER,
                                        petCursor OUT SYS_REFCURSOR)

AS
BEGIN 
    OPEN petCursor FOR
        SELECT DISTINCT ic.id_chip, d.id_district, c.id_canton,
                        pr.id_province, p.id_status, p.id_pet_type,
                        r.id_race, p.id_rescuer, c.id_color
        FROM pet p
        INNER JOIN identification_chip ic
        ON p.id_pet = ic.id_pet
        
        INNER JOIN pet_x_district pxd
        ON p.id_pet = pxd.id_pet
        
        INNER JOIN district d
        ON pxd.id_pet = d.id_district
        
        INNER JOIN canton c
        ON d.id_canton = c.id_canton
        
        INNER JOIN province pr
        ON c.id_province = pr.id_province
        
        INNER JOIN status s
        ON p.id_status = s.id_status
        
        INNER JOIN pet_type pt
        ON p.id_pet_type = pt.id_pet_type
        
        INNER JOIN race r
        ON pt.id_pet_type = r.id_pet_type
        
        INNER JOIN pet_x_color pxc
        ON p.id_pet = pxc.id_pet
        
        INNER JOIN color c
        ON pxc.id_color = c.id_color
        
        WHERE ic.id_chip = NVL(pIdChip, ic.id_chip)
        AND d.id_district = NVL(pIdDistrict, d.id_district)
        AND c.id_canton = NVL(pIdCanton, c.id_canton)
        AND pr.id_province = NVL(pIdProvince, pr.id_province)
        AND s.id_status = NVL(pIdStatus, s.id_status)
        AND p.id_pet_type = NVL(pIdPetType, p.id_pet_type)
        AND r.id_race = NVL(pIdRace, r.id_race)
        AND p.id_rescuer = NVL(pIdRescuer, p.id_rescuer)
        AND c.id_color = NVL(pIdColor, c.id_color)
        ORDER BY createdAt;
END getFilters;