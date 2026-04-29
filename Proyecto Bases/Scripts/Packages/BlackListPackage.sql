CREATE OR REPLACE PACKAGE adminBlackList IS

PROCEDURE insertBlackList(pIdReport IN NUMBER, pIdUser IN NUMBER);
PROCEDURE insertUserXBlackList(pReason IN VARCHAR2, pIdUser IN NUMBER, pIdReport IN NUMBER);

END adminBlackList;