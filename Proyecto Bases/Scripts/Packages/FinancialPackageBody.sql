CREATE OR REPLACE PACKAGE BODY adminFinancial AS

PROCEDURE insertDonation(pIdDonation IN NUMBER, pAmount IN NUMBER, pIdAssociation IN NUMBER, pIdCurrency IN NUMBER)
IS 
BEGIN
    INSERT INTO donation
    VALUES(pIdDonation, pAmount, pIdAssociation, pIdCurrency);
    COMMIT;
END;

PROCEDURE insertDonationXUser(pIdUser IN NUMBER, pIdDonation IN NUMBER)
IS 
BEGIN
    INSERT INTO donation_x_user
    VALUES(pIdUser, pIdDonation);
    COMMIT;
END;

END;