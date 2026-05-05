CREATE OR REPLACE PACKAGE BODY adminStatsBody AS
    
FUNCTION getPetsByTypeAndStatus(pIdType IN NUMBER, pIdStatus IN NUMBER) RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT p.id_type, p.id_status FROM pet p
            
            WHERE p.id_type = NVL(pIdType, p.id_type)
            AND p.id_status = NVL(pIdStatus, p.id_status);
            -- ORDER BY;
        RETURN v_cursor;
    END;

FUNCTION getDonationsByAssociation(pIdDonation IN NUMBER, pIdAssociation IN NUMBER) RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;    
    BEGIN
        OPEN v_cursor FOR
            SELECT d.id_donation, d.id_association FROM donation d
            
            WHERE d.id_donation = NVL(pIdDonation, d.id_donation)
            AND d.id_association = NVL(pIdAssociation, d.id_association);
            -- ORDER BY;
        RETURN v_cursor;
    END;
    
FUNCTION getDonationsByCribHouse(pIdDonation IN NUMBER, pIdCribHouse IN NUMBER) RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;    
    BEGIN
        OPEN v_cursor FOR
            SELECT d.id_donation, d.id_crib_house FROM donation d
            
            WHERE d.id_donation = NVL(pIdDonation, d.id_donation)
            AND d.id_crib_house = NVL(pIdCribHouse, d.id_crib_house);
            -- ORDER BY;
        RETURN v_cursor;
    END;

END adminStatsBody;