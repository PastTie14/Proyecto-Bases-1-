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
    FOREIGN KEY (id_adopter) REFERENCES adopter (id_user)
    ON DELETE CASCADE;

ALTER TABLE adoption_form
    ADD CONSTRAINT fk_af_pet
    FOREIGN KEY (id_pet) REFERENCES pet (id_pet)
    ON DELETE CASCADE;

COMMENT ON TABLE adoption_form
IS 'Stores information about adoption forms for adopting pets';

COMMENT ON COLUMN adoption_form.id_adoption
IS 'Primary key, identifier for the adoption form';

COMMENT ON COLUMN adoption_form.notes
IS 'Notes from the adoption';

COMMENT ON COLUMN adoption_form.adoption_date
IS 'The date the adoption was made';

COMMENT ON COLUMN adoption_form."reference"
IS 'The reference';

COMMENT ON COLUMN adoption_form.id_adopter
IS 'Foreign key, references the adopter who filled the form';

COMMENT ON COLUMN adoption_form.id_pet
IS 'Foreign key, references the adopted pet';

COMMENT ON COLUMN adoption_form.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN adoption_form.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN adoption_form.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN adoption_form.ModifiedAt
IS 'The date the table was modified';

-- ============================================
-- PHOTO
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
    FOREIGN KEY (id_user) REFERENCES "user" (id_user)
    ON DELETE CASCADE;

COMMENT ON TABLE photo
IS 'Stores information about photos uploaded by the adopter showing the current state of the pet';

COMMENT ON COLUMN photo.id_photo
IS 'Primary key, identifier for the photo';

COMMENT ON COLUMN photo."date"
IS 'The date the photo was uploaded';

COMMENT ON COLUMN photo.photo_dir
IS 'File path to the photo';

COMMENT ON COLUMN photo.id_user
IS 'Foreign key, references the user who uploaded the photo';

COMMENT ON COLUMN photo.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN photo.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN photo.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN photo.ModifiedAt
IS 'The date the table was modified';

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
    FOREIGN KEY (id_user) REFERENCES "user" (id_user)
    ON DELETE CASCADE;

ALTER TABLE rating
    ADD CONSTRAINT fk_rating_adopter
    FOREIGN KEY (id_adopter) REFERENCES adopter (id_user)
    ON DELETE CASCADE;

COMMENT ON TABLE rating
IS 'Stores information about ratings assigned to adopters';

COMMENT ON COLUMN rating.id_rating
IS 'Primary key, identifier for the rating';

COMMENT ON COLUMN rating.score
IS 'The score assigned to the adopter';

COMMENT ON COLUMN rating.id_user
IS 'Foreign key, references the user who does the rating';

COMMENT ON COLUMN rating.id_adopter
IS 'Foreign key, references the adopter who receives the rating';

COMMENT ON COLUMN rating.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN rating.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN rating.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN rating.ModifiedAt
IS 'The date the table was modified';

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

COMMENT ON TABLE match
IS 'Stores information about matches between lost and found pets';

COMMENT ON COLUMN match.id_match
IS 'Primary key, identifier for the match';

COMMENT ON COLUMN match.match_date
IS 'The date the match was made';

COMMENT ON COLUMN match.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN match.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN match.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN match.ModifiedAt
IS 'The date the table was modified';

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
    
COMMENT ON TABLE parameters
IS 'Stores information about the parameters';

COMMENT ON COLUMN parameters.id_parameter
IS 'Primary key, identifier for the parameter';

COMMENT ON COLUMN parameters."value"
IS 'The value of the parameter';

COMMENT ON COLUMN parameters.id_match
IS 'Foreign key, references the match';

COMMENT ON COLUMN parameters.id_value_type
IS 'Foreign key, references the value type';

COMMENT ON COLUMN parameters.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN parameters.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN parameters.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN parameters.ModifiedAt
IS 'The date the table was modified';
