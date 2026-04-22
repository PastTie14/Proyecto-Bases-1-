CREATE OR REPLACE PACKAGE adminPetExtraInfo IS

PROCEDURE insertCurrentStatus(id_currentStatus IN NUMBER, status_type IN VARCHAR2);

PROCEDURE insertEnergyLevel(id_energyLevel IN NUMBER, "name" IN VARCHAR2);

PROCEDURE insertTrainingEase(id_trainingEase IN NUMBER, "name" IN VARCHAR2);

PROCEDURE insertBounty(id_bounty IN NUMBER, amount IN NUMBER,
                        id_petExtraInfo IN NUMBER, id_currency IN NUMBER);

END adminPetExtraInfo;