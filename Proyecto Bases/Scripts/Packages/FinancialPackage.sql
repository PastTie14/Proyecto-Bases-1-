CREATE OR REPLACE PACKAGE adminFinancial IS

PROCEDURE insertDonation(pIdDonation IN NUMBER, pAmount IN NUMBER, pIdAssociation IN NUMBER, pIdCurrency IN NUMBER);
PROCEDURE insertDonationXUser(pIdUser IN NUMBER, pIdDonation IN NUMBER);

END adminFinancial;