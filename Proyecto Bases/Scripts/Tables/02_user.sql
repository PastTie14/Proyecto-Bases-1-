-- ============================================================
-- FILE 02 - USER & USER SUBTYPES
-- Tables: user, association, adopter, rescuer, crib_house, log
-- ============================================================

-- ============================================
-- USER
-- ============================================
CREATE TABLE "user"
(
    id_user    NUMBER(8),
    email      VARCHAR2(50),
    "password" VARCHAR2(20),
    createdBy  VARCHAR2(20),
    createdAt  DATE,
    modifiedBy VARCHAR2(20),
    modifiedAt DATE
)
TABLESPACE TS_DATA;

ALTER TABLE "user"
    MODIFY id_user CONSTRAINT user_idUser_nn NOT NULL;

ALTER TABLE "user"
    MODIFY email CONSTRAINT user_email_nn NOT NULL;

ALTER TABLE "user"
    MODIFY "password" CONSTRAINT user_password_nn NOT NULL;

ALTER TABLE "user"
    MODIFY createdBy CONSTRAINT user_createdBy_nn NOT NULL;

ALTER TABLE "user"
    MODIFY createdAt CONSTRAINT user_createdAt_nn NOT NULL;

ALTER TABLE "user"
    ADD CONSTRAINT pk_user PRIMARY KEY (id_user)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE "user"
    ADD CONSTRAINT uq_user_email UNIQUE (email);

-- ============================================
-- ASSOCIATION
-- ============================================
CREATE TABLE association
(
    id_user NUMBER(8),
    "name"  VARCHAR2(100)
)
TABLESPACE TS_DATA;

ALTER TABLE association
    MODIFY id_user CONSTRAINT association_idUser_nn NOT NULL;

ALTER TABLE association
    MODIFY "name" CONSTRAINT association_name_nn NOT NULL;

ALTER TABLE association
    ADD CONSTRAINT pk_association PRIMARY KEY (id_user)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE association
    ADD CONSTRAINT fk_association_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user)
    ON DELETE CASCADE;

-- ============================================
-- ADOPTER
-- ============================================
CREATE TABLE adopter
(
    id_user        NUMBER(8),
    first_name     VARCHAR2(50),
    second_name    VARCHAR2(50),
    first_surname  VARCHAR2(50),
    second_surname VARCHAR2(50)
)
TABLESPACE TS_DATA;

ALTER TABLE adopter
    MODIFY id_user CONSTRAINT adopter_idUser_nn NOT NULL;

ALTER TABLE adopter
    MODIFY first_name CONSTRAINT adopter_firstName_nn NOT NULL;

ALTER TABLE adopter
    MODIFY first_surname CONSTRAINT adopter_firstSurname_nn NOT NULL;

ALTER TABLE adopter
    ADD CONSTRAINT pk_adopter PRIMARY KEY (id_user)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE adopter
    ADD CONSTRAINT fk_adopter_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user)
    ON DELETE CASCADE;

-- ============================================
-- RESCUER
-- ============================================
CREATE TABLE rescuer
(
    id_user        NUMBER(8),
    first_name     VARCHAR2(50),
    second_name    VARCHAR2(50),
    first_surname  VARCHAR2(50),
    second_surname VARCHAR2(50)
)
TABLESPACE TS_DATA;

ALTER TABLE rescuer
    MODIFY id_user CONSTRAINT rescuer_idUser_nn NOT NULL;

ALTER TABLE rescuer
    MODIFY first_name CONSTRAINT rescuer_firstName_nn NOT NULL;

ALTER TABLE rescuer
    MODIFY first_surname CONSTRAINT rescuer_firstSurname_nn NOT NULL;

ALTER TABLE rescuer
    ADD CONSTRAINT pk_rescuer PRIMARY KEY (id_user)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE rescuer
    ADD CONSTRAINT fk_rescuer_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user)
    ON DELETE CASCADE;

-- ============================================
-- CRIB HOUSE
-- ============================================
CREATE TABLE crib_house
(
    id_user            NUMBER(8),
    "name"             VARCHAR2(100),
    requires_donations NUMBER(1) DEFAULT 0
)
TABLESPACE TS_DATA;

ALTER TABLE crib_house
    MODIFY id_user CONSTRAINT cribHouse_idUser_nn NOT NULL;

ALTER TABLE crib_house
    MODIFY "name" CONSTRAINT cribHouse_name_nn NOT NULL;

ALTER TABLE crib_house
    MODIFY requires_donations CONSTRAINT cribHouse_requiresDonations_nn NOT NULL;

ALTER TABLE crib_house
    ADD CONSTRAINT pk_crib_house PRIMARY KEY (id_user)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE crib_house
    ADD CONSTRAINT fk_crib_house_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user)
    ON DELETE CASCADE;

ALTER TABLE crib_house
    ADD CONSTRAINT chk_requires_donations
    CHECK (requires_donations IN (0, 1));

-- ============================================
-- LOG
-- ============================================
CREATE TABLE "log"
(
    id_log        NUMBER(8),
    changeDate    DATE,
    changeBy      VARCHAR2(20),
    tableName     VARCHAR2(20),
    fieldName     VARCHAR2(20),
    previousValue VARCHAR2(20),
    currentValue  VARCHAR2(20),
    id_user       NUMBER(8)
)
TABLESPACE TS_DATA;

ALTER TABLE "log"
    MODIFY id_log CONSTRAINT log_idLog_nn NOT NULL;

ALTER TABLE "log"
    MODIFY changeDate CONSTRAINT log_changeDate_nn NOT NULL;

ALTER TABLE "log"
    MODIFY changeBy CONSTRAINT log_changeBy_nn NOT NULL;

ALTER TABLE "log"
    MODIFY tableName CONSTRAINT log_tableName_nn NOT NULL;

ALTER TABLE "log"
    MODIFY fieldName CONSTRAINT log_fieldName_nn NOT NULL;

ALTER TABLE "log"
    MODIFY id_user CONSTRAINT log_idUser_nn NOT NULL;

ALTER TABLE "log"
    ADD CONSTRAINT pk_log PRIMARY KEY (id_log)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE "log"
    ADD CONSTRAINT fk_log_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user);
