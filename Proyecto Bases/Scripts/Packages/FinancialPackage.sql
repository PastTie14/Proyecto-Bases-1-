CREATE OR REPLACE PACKAGE adminFinancial IS

-- INSERT
PROCEDURE insertDonation(pIdDonation IN NUMBER, pAmount IN NUMBER, pIdAssociation IN NUMBER, pIdCurrency IN NUMBER);
PROCEDURE insertDonationXUser(pIdUser IN NUMBER, pIdDonation IN NUMBER);

-- GET
FUNCTION getDonation RETURN SYS_REFCURSOR;
FUNCTION getDonationXUser RETURN SYS_REFCURSOR;

END adminFinancial;