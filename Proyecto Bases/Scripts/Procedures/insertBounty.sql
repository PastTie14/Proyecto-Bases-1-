CREATE OR REPLACE PROCEDURE insertBounty(id_bounty IN NUMBER, amount IN NUMBER,
                                        id_petExtraInfo IN NUMBER, id_currency IN NUMBER)

AS 
BEGIN
    INSERT INTO bounty
    VALUES(id_bounty, amount, id_petExtraInfo, id_currency);
    COMMIT;
END insertBounty;