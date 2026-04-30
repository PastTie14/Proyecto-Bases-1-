CREATE OR REPLACE PACKAGE BODY adminFinancial AS

-- ======================================== INSERT ========================================

PROCEDURE insertDonation(pIdDonation IN NUMBER, pAmount IN NUMBER, pIdAssociation IN NUMBER, pIdCurrency IN NUMBER)
IS 
BEGIN
    INSERT INTO donation (id_donation, amount, id_association, id_currency)
    VALUES(pIdDonation, pAmount, pIdAssociation, pIdCurrency);
    COMMIT;
END;

PROCEDURE insertDonationXUser(pIdUser IN NUMBER, pIdDonation IN NUMBER)
IS 
BEGIN
    INSERT INTO donation_x_user (id_user, id_donation)
    VALUES(pIdUser, pIdDonation);
    COMMIT;
END;

-- ======================================== GET ========================================

FUNCTION getDonation RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM donation;
    RETURN v_cursor;
END;

FUNCTION getDonationXUser RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM donation_x_user;
    RETURN v_cursor;
END;

END;