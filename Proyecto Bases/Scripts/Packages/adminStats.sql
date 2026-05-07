CREATE OR REPLACE PACKAGE adminStats AS

FUNCTION getPetsByTypeAndStatus(pIdType IN NUMBER, pIdStatus IN NUMBER, 
                                pStartDate IN DATE, pEndDate IN DATE) RETURN SYS_REFCURSOR;

FUNCTION getDonationsByAssociation(pStartDate IN DATE, pEndDate IN DATE) RETURN SYS_REFCURSOR;
                                
FUNCTION getDonationsByCribHouse(pStartDate IN DATE, pEndDate IN DATE) RETURN SYS_REFCURSOR;

FUNCTION getAdoptedVSUnadopted(pIdType IN NUMBER, pIdRace IN NUMBER) RETURN SYS_REFCURSOR;

FUNCTION getUnadoptedPetsByAgeRange RETURN SYS_REFCURSOR;

FUNCTION getBestRescuersAndAdopters(pStartDate IN DATE, pEndDate IN DATE) RETURN SYS_REFCURSOR;

END adminStats;