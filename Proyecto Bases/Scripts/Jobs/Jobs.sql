BEGIN
DBMS_SCHEDULER.CREATE_SCHEDULE (
    schedule_name => 'schedule_jobs',
    start_date => TRUNC(SYSDATE),
    repeat_interval => 'FREQ=HOURLY;
                        INTERVAL=' || TRUNC(hours_job) || ';'
);
DBMS_SCHEDULER.CREATE_JOB
(
    job_name => 'MATCH_FOUND_JOB',
    job_type => 'PLSQL_BLOCK',
    job_action => '',
    schedule_name => 'schedule_jobs',
    enabled = false
);
DBMS_SCHEDULER.CREATE_JOB
(
    job_name => 'MATCH_LOST_JOB',
    job_type => 'PLSQL_BLOCK',
    job_action => '',
    schedule_name => 'schedule_jobs',
    enabled = false
);
DBMS_SCHEDULER.CREATE_JOB
(
    job_name => 'MOST_REQUESTED_JOB',
    job_type => 'PLSQL_BLOCK',
    job_action => '',
    schedule_name => 'schedule_jobs',
    enabled = false
);
END;