BEGIN
DBMS_SCHEDULER.CREATE_SCHEDULE (
    schedule_name => 'schedule_jobs',
    start_date => TRUNC(SYSDATE),
    repeat_interval => 'FREQ=HOURLY;
                        INTERVAL=' || TRUNC(4) || ';',
    end_date => TRUNC(TO_DATE('12/05/2026', 'DD-MM-YYYY'))
);
DBMS_SCHEDULER.CREATE_JOB
(
    job_name => 'MATCH_FOUND_LOST_JOB',
    job_type => 'PLSQL_BLOCK',
    job_action => 'BEGIN
    INSERT INTO "MATCH"
    (
        id_match,
        match_date,
        id_pet_lost,
        id_pet_found
    )
    SELECT
        s_match.NEXTVAL,
        SYSDATE,
        p.id_pet,
        q.id_pet
    FROM pet p
    JOIN pet q
        ON p.id_race = q.id_race
    LEFT JOIN "MATCH" m1
        ON m1.id_pet_lost = p.id_pet
       AND m1.id_pet_found = q.id_pet
    LEFT JOIN "MATCH" m2
        ON m2.id_pet_lost = q.id_pet
       AND m2.id_pet_found = p.id_pet
    WHERE p.id_pet < q.id_pet
      AND p.date_lost IS NOT NULL
      AND q.date_found IS NOT NULL
      AND q.date_found > p.date_lost
      AND m1.id_match IS NULL
      AND m2.id_match IS NULL;
END;',
    schedule_name => 'schedule_jobs',
    enabled => true
);
END;