CREATE OR REPLACE PROCEDURE getPetFilters (pIdChip IN NUMBER, pIdDistrict IN NUMBER, pIdCanton IN NUMBER,
                                        pIdProvince IN NUMBER, pIdStatus IN NUMBER, pIdPetType IN NUMBER,
                                        pIdRescuer IN NUMBER, pIdRace IN NUMBER, pIdColor IN NUMBER,
                                        petCursor OUT SYS_REFCURSOR)

AS
BEGIN 
    OPEN petCursor FOR
        SELECT DISTINCT ic.id_chip, d.id_district, ca.id_canton,
                        pr.id_province, p.id_status, p.id_pet_type,
                        r.id_race, p.id_rescuer, co.id_color, p.createdat
        FROM pet p
        LEFT JOIN identification_chip ic
        ON p.id_pet = ic.id_pet
        
        INNER JOIN pet_x_district pxd
        ON p.id_pet = pxd.id_pet
        
        INNER JOIN district d
        ON pxd.id_district = d.id_district
        
        INNER JOIN canton ca
        ON d.id_canton = ca.id_canton
        
        INNER JOIN province pr
        ON ca.id_province = pr.id_province
        
        INNER JOIN status s
        ON p.id_status = s.id_status
        
        INNER JOIN pet_type pt
        ON p.id_pet_type = pt.id_pet_type
        
        INNER JOIN race r
        ON pt.id_pet_type = r.id_pet_type
        
        INNER JOIN pet_x_color pxc
        ON p.id_pet = pxc.id_pet
        
        INNER JOIN color co
        ON pxc.id_color = co.id_color
        
        WHERE (pIdChip     IS NULL OR ic.id_chip      = pIdChip)
        AND   (pIdDistrict IS NULL OR d.id_district   = pIdDistrict)
        AND   (pIdCanton   IS NULL OR ca.id_canton    = pIdCanton)
        AND   (pIdProvince IS NULL OR pr.id_province  = pIdProvince)
        AND   (pIdStatus   IS NULL OR s.id_status     = pIdStatus)
        AND   (pIdPetType  IS NULL OR p.id_pet_type   = pIdPetType)
        AND   (pIdRace     IS NULL OR r.id_race       = pIdRace)
        AND   (pIdRescuer  IS NULL OR p.id_rescuer    = pIdRescuer)
        AND   (pIdColor    IS NULL OR co.id_color     = pIdColor)
        ORDER BY p.createdAt;
END getPetFilters;