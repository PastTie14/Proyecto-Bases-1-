CREATE OR REPLACE PACKAGE adminConsult AS
 
    FUNCTION getDonations(pStartDate IN DATE, pEndDate IN DATE, pIdDonor IN NUMBER, pIdAssociation IN NUMBER) RETURN SYS_REFCURSOR;
 
    FUNCTION getBlackListReport RETURN SYS_REFCURSOR;
 
    --FUNCTION getBlackListReportDetails(pIdUser IN NUMBER) RETURN SYS_REFCURSOR;

    --FUNCTION getMatches(pIdType IN NUMBER,pIdRace IN NUMBER) RETURN SYS_REFCURSOR;

    --FUNCTION getPetNecessaryTreatments(pMin IN NUMBER, pMax IN NUMBER) RETURN SYS_REFCURSOR;

    --FUNCTION getCompatibleCribHouses(pIdPetType IN NUMBER) RETURN SYS_REFCURSOR;

    --FUNCTION getBestRescuersAndAdopters(pStartDate IN DATE,pEndDate   IN DATE) RETURN SYS_REFCURSOR;
 
END adminConsult;