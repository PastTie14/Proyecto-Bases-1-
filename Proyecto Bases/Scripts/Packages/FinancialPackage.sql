CREATE OR REPLACE PACKAGE adminFinancial IS

-- INSERT
PROCEDURE insertDonation(pIdDonation IN NUMBER, pAmount IN NUMBER, pIdAssociation IN NUMBER, 
                            pIdCurrency IN NUMBER, pIdCribHouse IN NUMBER, pIdDonnor IN NUMBER);

-- GET
FUNCTION getDonation RETURN SYS_REFCURSOR;
FUNCTION getDonationById(pIdDonation IN NUMBER) RETURN SYS_REFCURSOR;
FUNCTION getRecipients RETURN SYS_REFCURSOR;

FUNCTION getLastDonationId RETURN NUMBER;
FUNCTION getDonationByUser(pIdUser IN NUMBER) RETURN SYS_REFCURSOR;

END adminFinancial;