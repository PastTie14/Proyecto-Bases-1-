CREATE OR REPLACE PACKAGE BODY adminBlackList AS

-- ======================================== INSERT ========================================

PROCEDURE insertBlackList(pIdReport IN NUMBER, pIdUser IN NUMBER)
IS 
BEGIN
    INSERT INTO black_list (id_report, id_user)
    VALUES(pIdReport, pIdUser);
    COMMIT;
END;

PROCEDURE insertUserXBlackList(pReason IN VARCHAR2, pIdUser IN NUMBER, pIdReport IN NUMBER)
IS 
BEGIN
    INSERT INTO user_x_black_list (reason, id_user, id_report)
    VALUES(pReason, pIdUser, pIdReport);
    COMMIT;
END;

-- ======================================== GET ========================================

FUNCTION getBlackList RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM black_list;
    RETURN v_cursor;
END;

FUNCTION getUserXBlackList RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR SELECT * FROM user_x_black_list;
    RETURN v_cursor;
END;

-- ======================================== DELETE ========================================

PROCEDURE deleteUserFromBlackList(pIdReport IN NUMBER, pIdUser IN NUMBER)
IS
BEGIN
    DELETE FROM user_x_black_list
    WHERE id_report = pIdReport
    AND id_user = pIdUser;
    COMMIT;
END;

END;