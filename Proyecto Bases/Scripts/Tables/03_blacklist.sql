-- ============================================================
-- FILE 03 - BLACKLIST
-- Tables: black_list, user_x_black_list
-- Depends on: user (02)
-- ============================================================

-- ============================================
-- BLACK LIST
-- ============================================
CREATE TABLE black_list
(
    id_report NUMBER(8),
    id_user   NUMBER(8)
)
TABLESPACE TS_DATA;

ALTER TABLE black_list
    MODIFY id_report CONSTRAINT blackList_idReport_nn NOT NULL;

ALTER TABLE black_list
    MODIFY id_user CONSTRAINT blackList_idUser_nn NOT NULL;

ALTER TABLE black_list
    ADD CONSTRAINT pk_black_list PRIMARY KEY (id_report)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE black_list
    ADD CONSTRAINT fk_black_list_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user);

COMMENT ON TABLE black_list
IS 'Stores the black list information';

COMMENT ON COLUMN black_list.id_report
IS 'Primary key, identifier for the black list id';

COMMENT ON COLUMN black_list.id_user
IS 'Foreign key, references the user who owns the black list';

COMMENT ON COLUMN black_list.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN black_list.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN black_list.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN black_list.ModifiedAt
IS 'The date the table was modified';

-- ============================================
-- USER X BLACK LIST
-- ============================================
CREATE TABLE user_x_black_list
(
    reason    VARCHAR2(200),
    id_user   NUMBER(8),
    id_report NUMBER(8)
)
TABLESPACE TS_DATA;

ALTER TABLE user_x_black_list
    MODIFY reason CONSTRAINT userXBlackList_reason_nn NOT NULL;

ALTER TABLE user_x_black_list
    MODIFY id_user CONSTRAINT userXBlackList_idUser_nn NOT NULL;

ALTER TABLE user_x_black_list
    MODIFY id_report CONSTRAINT userXBlackList_idReport_nn NOT NULL;

ALTER TABLE user_x_black_list
    ADD CONSTRAINT pk_user_x_black_list PRIMARY KEY (id_user, id_report)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE user_x_black_list
    ADD CONSTRAINT fk_uxbl_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user)
    ON DELETE CASCADE;

ALTER TABLE user_x_black_list
    ADD CONSTRAINT fk_uxbl_report
    FOREIGN KEY (id_report) REFERENCES black_list (id_report)
    ON DELETE CASCADE;

COMMENT ON TABLE user_x_black_list
IS 'Intermediate table, stores the blocked users from a black list';

COMMENT ON COLUMN user_x_black_list.reason
IS 'Reason why the user was blocked';

COMMENT ON COLUMN user_x_black_list.id_report
IS 'Composite Primary key. Foreign key references the id of the black list';

COMMENT ON COLUMN user_x_black_list.id_user
IS 'Composite Foreign key. Foreign key references the id of the blocked user';

COMMENT ON COLUMN user_x_black_list.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN user_x_black_list.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN user_x_black_list.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN user_x_black_list.ModifiedAt
IS 'The date the table was modified';
