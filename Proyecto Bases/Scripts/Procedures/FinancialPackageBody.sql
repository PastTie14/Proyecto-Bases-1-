CREATE OR REPLACE PACKAGE BODY adminFinancial AS

PROCEDURE insertDonation(id_donation IN NUMBER, amount IN NUMBER, id_association IN NUMBER, id_currency IN NUMBER)
IS 
BEGIN
    INSERT INTO donation
    VALUES(id_donation,amount,id_association,id_currency);
    COMMIT;
END;

PROCEDURE insertDonationXUser(id_user IN NUMBER, id_donation IN NUMBER)
IS 
BEGIN
    INSERT INTO donation_x_user
    VALUES(id_user,id_donation);
    COMMIT;
END;

END;