CREATE OR REPLACE PACKAGE adminFinancial IS

PROCEDURE insertDonation(id_donation IN NUMBER, amount IN NUMBER, id_association IN NUMBER, id_currency IN NUMBER);
PROCEDURE insertDonationXUser(id_user IN NUMBER, id_donation IN NUMBER);

END adminFinancial;