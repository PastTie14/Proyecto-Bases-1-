CREATE OR REPLACE PROCEDURE insertMatch(id_match IN NUMBER, match_date IN DATE,
                                        similarity_percentage IN NUMBER)

AS 
BEGIN
    INSERT INTO match
    VALUES(id_match, match_date, similarity_percentage);
    COMMIT;
END insertMatch;