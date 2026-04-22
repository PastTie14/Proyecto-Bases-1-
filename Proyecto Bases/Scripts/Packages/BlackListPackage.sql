CREATE OR REPLACE PACKAGE adminBlackList IS

PROCEDURE insertBlackList(id_report IN NUMBER, id_user IN NUMBER);
PROCEDURE insertUserXBlackList(reason VARCHAR2, id_user IN NUMBER, id_report IN NUMBER);

END adminBlackList;