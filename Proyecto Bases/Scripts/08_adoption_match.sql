-- ============================================================
-- FILE 08 - ADOPTION, PHOTO, RATING & MATCH
-- Tables: adoption_form, photo, rating, match, parameters
-- Depends on: user (02), pet (06), value_type (01)
-- ============================================================

-- ============================================
-- ADOPTION FORM
-- ============================================
CREATE TABLE adoption_form
(
    id_adoption   NUMBER(8),
    notes         VARCHAR2(500),
    adoption_date DATE,
    "reference"   VARCHAR2(200),
    id_adopter    NUMBER(8),
    id_pet        NUMBER(8)
)
TABLESPACE TS_DATA;

ALTER TABLE adoption_form
    MODIFY id_adoption CONSTRAINT adoptionForm_idAdoption_nn NOT NULL;

ALTER TABLE adoption_form
    MODIFY adoption_date CONSTRAINT adoptionForm_adoptionDate_nn NOT NULL;

ALTER TABLE adoption_form
    MODIFY id_adopter CONSTRAINT adoptionForm_idAdopter_nn NOT NULL;

ALTER TABLE adoption_form
    MODIFY id_pet CONSTRAINT adoptionForm_idPet_nn NOT NULL;

ALTER TABLE adoption_form
    ADD CONSTRAINT pk_adoption_form PRIMARY KEY (id_adoption)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE adoption_form
    ADD CONSTRAINT fk_af_adopter
    FOREIGN KEY (id_adopter) REFERENCES adopter (id_user);

ALTER TABLE adoption_form
    ADD CONSTRAINT fk_af_pet
    FOREIGN KEY (id_pet) REFERENCES pet (id_pet);

-- ============================================
-- PHOTO
-- (Photos uploaded by adopter to show the pet's current state)
-- ============================================
CREATE TABLE photo
(
    id_photo  NUMBER(8),
    "date"    DATE,
    photo_dir VARCHAR2(200),
    id_user   NUMBER(8)
)
TABLESPACE TS_DATA;

ALTER TABLE photo
    MODIFY id_photo CONSTRAINT photo_idPhoto_nn NOT NULL;

ALTER TABLE photo
    MODIFY "date" CONSTRAINT photo_date_nn NOT NULL;

ALTER TABLE photo
    MODIFY photo_dir CONSTRAINT photo_photoDir_nn NOT NULL;

ALTER TABLE photo
    MODIFY id_user CONSTRAINT photo_idUser_nn NOT NULL;

ALTER TABLE photo
    ADD CONSTRAINT pk_photo PRIMARY KEY (id_photo)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE photo
    ADD CONSTRAINT fk_photo_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user);

-- ============================================
-- RATING
-- ============================================
CREATE TABLE rating
(
    id_rating  NUMBER(8),
    score      NUMBER(3,1),
    id_user    NUMBER(8),
    id_adopter NUMBER(8)
)
TABLESPACE TS_DATA;

ALTER TABLE rating
    MODIFY id_rating CONSTRAINT rating_idRating_nn NOT NULL;

ALTER TABLE rating
    MODIFY score CONSTRAINT rating_score_nn NOT NULL;

ALTER TABLE rating
    MODIFY id_user CONSTRAINT rating_idUser_nn NOT NULL;

ALTER TABLE rating
    MODIFY id_adopter CONSTRAINT rating_idAdopter_nn NOT NULL;

ALTER TABLE rating
    ADD CONSTRAINT pk_rating PRIMARY KEY (id_rating)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE rating
    ADD CONSTRAINT fk_rating_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user);

ALTER TABLE rating
    ADD CONSTRAINT fk_rating_adopter
    FOREIGN KEY (id_adopter) REFERENCES adopter (id_user);

-- ============================================
-- MATCH
-- ============================================
CREATE TABLE match
(
    id_match              NUMBER(8),
    match_date            DATE,
    similarity_percentage NUMBER(5,2)
)
TABLESPACE TS_DATA;

ALTER TABLE match
    MODIFY id_match CONSTRAINT match_idMatch_nn NOT NULL;

ALTER TABLE match
    MODIFY match_date CONSTRAINT match_matchDate_nn NOT NULL;

ALTER TABLE match
    MODIFY similarity_percentage CONSTRAINT match_similarityPercentage_nn NOT NULL;

ALTER TABLE match
    ADD CONSTRAINT pk_match PRIMARY KEY (id_match)
    USING INDEX TABLESPACE TS_INDEX;

-- ============================================
-- PARAMETERS
-- ============================================
CREATE TABLE parameters
(
    id_parameter  NUMBER(8),
    "value"       VARCHAR2(100),
    id_match      NUMBER(8),
    id_value_type NUMBER(4)
)
TABLESPACE TS_DATA;

ALTER TABLE parameters
    MODIFY id_parameter CONSTRAINT parameters_idParameter_nn NOT NULL;

ALTER TABLE parameters
    MODIFY "value" CONSTRAINT parameters_value_nn NOT NULL;

ALTER TABLE parameters
    MODIFY id_match CONSTRAINT parameters_idMatch_nn NOT NULL;

ALTER TABLE parameters
    MODIFY id_value_type CONSTRAINT parameters_idValueType_nn NOT NULL;

ALTER TABLE parameters
    ADD CONSTRAINT pk_parameters PRIMARY KEY (id_parameter)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE parameters
    ADD CONSTRAINT fk_param_match
    FOREIGN KEY (id_match) REFERENCES match (id_match);

ALTER TABLE parameters
    ADD CONSTRAINT fk_param_value_type
    FOREIGN KEY (id_value_type) REFERENCES value_type (id_value_type);