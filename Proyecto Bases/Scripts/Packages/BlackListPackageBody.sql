CREATE OR REPLACE PACKAGE BODY adminBlackList AS

PROCEDURE insertBlackList(pIdReport IN NUMBER, pIdUser IN NUMBER)
IS 
BEGIN
    INSERT INTO black_list
    VALUES(pIdReport, pIdUser);
    COMMIT;
END;

PROCEDURE insertUserXBlackList(pReason IN VARCHAR2, pIdUser IN NUMBER, pIdReport IN NUMBER)
IS 
BEGIN
    INSERT INTO user_x_black_list
    VALUES(pReason, pIdUser, pIdReport);
    COMMIT;
END;

END;