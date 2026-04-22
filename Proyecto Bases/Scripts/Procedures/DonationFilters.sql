CREATE OR REPLACE PROCEDURE getDonationFilters (pAmount IN NUMBER, pIdAssociation IN NUMBER, pIdCurrency IN NUMBER, 
                                                pIdUser IN NUMBER, pCreatedAt IN DATE, donationCursor OUT SYS_REFCURSOR)

AS
BEGIN
    OPEN donationCursor FOR
        SELECT d.amount, d.id_association, d.id_currency, u.id_user, d.createdAt
        FROM donation d
        
        INNER JOIN association a
        ON d.id_association = a.id_user
        
        INNER JOIN donation_x_user dxu
        ON d.id_donation = dxu.id_donation
        
        INNER JOIN "user" u
        ON dxu.id_user = u.id_user

        WHERE d.amount = NVL(pAmount, d.amount)
        AND d.id_currency = NVL(pIdCurrency, d.id_currency)
        AND d.id_association = NVL(pIdAssociation, d.id_association)
        AND u.id_user = NVL(pIdUser, u.id_user)
        AND d.createdAt = NVL(pCreatedAt, d.createdAt)
        ORDER BY d.createdAt;
END getDonationFilters;