create or replace PROCEDURE insertUser(id_user IN NUMBER, email IN VARCHAR2, "password" IN VARCHAR2,
                                       createdBy IN VARCHAR2, createdAt IN DATE,
                                       modifiedBy IN VARCHAR2, modifiedAt IN DATE)
AS
BEGIN
    INSERT INTO "user" 
    VALUES(id_user, email, "password",
            createdBy, createdAt, 
            modifiedBy, modifiedAt);
        COMMIT;
END insertUser;