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
