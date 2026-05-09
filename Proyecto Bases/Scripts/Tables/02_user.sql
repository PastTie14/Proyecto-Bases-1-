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

COMMENT ON TABLE "user"
IS 'Stores user account information';

COMMENT ON COLUMN "user".id_user
IS 'Primary key, identifier for the user';

COMMENT ON COLUMN "user".email
IS 'Email address used for login';

COMMENT ON COLUMN "user"."password"
IS 'Password for user authentication';

COMMENT ON COLUMN "user".CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN "user".CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN "user".ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN "user".ModifiedAt
IS 'The date the table was modified';

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

COMMENT ON TABLE association
IS 'Extends user table with organization-specific information';

COMMENT ON COLUMN association.id_user
IS 'Foreign key, references the user id';

COMMENT ON COLUMN association."name"
IS 'Official name of the association';

COMMENT ON COLUMN association.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN association.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN association.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN association.ModifiedAt
IS 'The date the table was modified';

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

COMMENT ON TABLE adopter
IS 'Extends user table with person-specific information for adopters';

COMMENT ON COLUMN adopter.id_user
IS 'Foreign key, references the user id';

COMMENT ON COLUMN adopter.first_name
IS 'First name of the adopter';

COMMENT ON COLUMN adopter.second_name
IS 'Second name of the adopter';

COMMENT ON COLUMN adopter.first_surname
IS 'Paternal surname of the adopter';

COMMENT ON COLUMN adopter.second_surname
IS 'Maternal surname of the adopter';

COMMENT ON COLUMN adopter.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN adopter.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN adopter.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN adopter.ModifiedAt
IS 'The date the table was modified';

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

COMMENT ON TABLE rescuer
IS 'Extends user table with person-specific information for rescuers';

COMMENT ON COLUMN rescuer.id_user
IS 'Foreign key referencing the user id';

COMMENT ON COLUMN rescuer.first_name
IS 'First name of the rescuer';

COMMENT ON COLUMN rescuer.second_name
IS 'Second name of the rescuer';

COMMENT ON COLUMN rescuer.first_surname
IS 'Paternal surname of the rescuer';

COMMENT ON COLUMN rescuer.second_surname
IS 'Maternal surname of the rescuer';

COMMENT ON COLUMN rescuer.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN rescuer.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN rescuer.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN rescuer.ModifiedAt
IS 'The date the table was modified';

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

COMMENT ON TABLE crib_house
IS 'Extends user table with crib house specific information';

COMMENT ON COLUMN crib_house.id_user
IS 'Foreign keym, references the user id';

COMMENT ON COLUMN crib_house."name"
IS 'Name of the crib house';

COMMENT ON COLUMN crib_house.requires_donations
IS 'Flag indicating if the crib house accepts donations';

COMMENT ON COLUMN crib_house.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN crib_house.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN crib_house.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN crib_house.ModifiedAt
IS 'The date the table was modified';

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
    currentValue  VARCHAR2(20)
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
    ADD CONSTRAINT pk_log PRIMARY KEY (id_log)
    USING INDEX TABLESPACE TS_INDEX;

COMMENT ON TABLE "log"
IS 'Stores data changes across tables';

COMMENT ON COLUMN "log".id_log
IS 'Primary key, identifier for the log entry';

COMMENT ON COLUMN "log".changeDate
IS 'The date when the change occurred';

COMMENT ON COLUMN "log".changeBy
IS 'The user who made the change';

COMMENT ON COLUMN "log".tableName
IS 'Name of the table where the change happened';

COMMENT ON COLUMN "log".fieldName
IS 'Name of the column that was modified';

COMMENT ON COLUMN "log".previousValue
IS 'Value before the change was applied';

COMMENT ON COLUMN "log".currentValue
IS 'Value after the change was applied';
