CREATE OR REPLACE FUNCTION insertPetExtraInfo(id_petExtraInfo IN NUMBER, "size" IN VARCHAR2, 
                                            before_picture IN VARCHAR2, after_picture IN VARCHAR2, 
                                            id_pet IN NUMBER)
RETURN NUMBER
AS 
    n_petExtraInfo_id NUMBER(8);
BEGIN
    INSERT INTO pet_extra_info
    VALUES(id_petExtraInfo, "size", before_picture, after_picture, id_pet);
    COMMIT;
    SELECT s_petExtraInfo.CURRVAL INTO n_petExtraInfo_id FROM DUAL;
    RETURN (n_petExtraInfo_id);
END insertPetExtraInfo;