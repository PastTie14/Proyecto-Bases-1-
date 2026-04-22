CREATE OR REPLACE FUNCTION insertUser(id_user IN NUMBER, email IN VARCHAR2, "password" IN VARCHAR2,
                                       createdBy IN VARCHAR2, createdAt IN DATE,
                                       modifiedBy IN VARCHAR2, modifiedAt IN DATE)
RETURN NUMBER
AS
    n_user_id NUMBER(8);
BEGIN
    INSERT INTO "user" 
    VALUES(id_user, email, "password",
            createdBy, createdAt, 
            modifiedBy, modifiedAt);
    COMMIT;
    SELECT s_user.CURRVAL INTO n_user_id FROM DUAL;
    RETURN (n_user_id); -- returns the user id to use it in intermediate tables    
END insertUser;