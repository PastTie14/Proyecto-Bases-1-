CREATE OR REPLACE PACKAGE BODY adminFinancial AS

-- ======================================== INSERT ========================================

PROCEDURE insertDonation(pIdDonation IN NUMBER, pAmount IN NUMBER, pIdAssociation IN NUMBER, 
                            pIdCurrency IN NUMBER, pIdCribHouse IN NUMBER)
IS 
BEGIN
    INSERT INTO donation (id_donation, amount, id_association, id_currency, id_crib_house)
    VALUES(s_donation.nextVal, pAmount, pIdAssociation, pIdCurrency, pIdCribHouse);
    COMMIT;
END;

PROCEDURE insertDonationXUser(pIdUser IN NUMBER, pIdDonation IN NUMBER)
IS
BEGIN
    INSERT INTO donation_x_user (id_user, id_donation)
    VALUES(pIdUser, pIDDonation);
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

FUNCTION getDonationById(pIdDonation IN NUMBER) RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT d.amount FROM donation d
        WHERE d.id_donation = pIdDonation;
    RETURN v_cursor;
END;

FUNCTION getDonationXUser RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM donation_x_user;
    RETURN v_cursor;
END;

FUNCTION getRecipients RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
        SELECT a.id_user    AS ID,
               a."name"     AS NOMBRE,
               'Asociación' AS TIPO
        FROM   association a
 
        UNION ALL
 
        SELECT ch.id_user   AS ID,
               ch."name"    AS NOMBRE,
               'Casa Cuna'  AS TIPO
        FROM   crib_house ch
        WHERE  ch.requires_donations = 1
 
        ORDER BY TIPO, NOMBRE;
    RETURN v_cursor;
END getRecipients;

FUNCTION getLastDonationId RETURN NUMBER
IS
    v_id NUMBER;
BEGIN
    SELECT s_donation.CURRVAL INTO v_id FROM DUAL;
    RETURN v_id;
END getLastDonationId;




END adminFinancial;