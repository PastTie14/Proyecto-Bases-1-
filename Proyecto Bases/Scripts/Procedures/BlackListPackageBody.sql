CREATE OR REPLACE PACKAGE BODY adminBlackList AS

PROCEDURE insertBlackList(id_report IN NUMBER, id_user IN NUMBER)
IS 
BEGIN
    INSERT INTO black_list
    VALUES(id_report,id_user);
    COMMIT;
END;

PROCEDURE insertUserXBlackList(reason VARCHAR2, id_user IN NUMBER, id_report IN NUMBER)
IS 
BEGIN
    INSERT INTO user_x_black_list
    VALUES(reason,id_user,id_report);
    COMMIT;
END;

END;