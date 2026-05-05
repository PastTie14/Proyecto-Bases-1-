-- ============================================================
-- FILE 04 - FINANCIAL
-- Tables: donation, donation_x_user
-- Depends on: currency (01), user (02)
-- ============================================================

-- ============================================
-- DONATION
-- ============================================
CREATE TABLE donation
(
    id_donation    NUMBER(8),
    amount         NUMBER(12, 2),
    id_association NUMBER(8),
    id_crib_house  NUMBER(8),
    id_currency    NUMBER(4)
)
TABLESPACE TS_DATA;

ALTER TABLE donation
    MODIFY id_donation CONSTRAINT donation_idDonation_nn NOT NULL;

ALTER TABLE donation
    MODIFY amount CONSTRAINT donation_amount_nn NOT NULL;

ALTER TABLE donation
    MODIFY id_association CONSTRAINT donation_idAssociation_nn NOT NULL;

ALTER TABLE donation
    MODIFY id_crib_house CONSTRAINT donation_idCribHouse_nn NOT NULL;

ALTER TABLE donation
    MODIFY id_currency CONSTRAINT donation_idCurrency_nn NOT NULL;

ALTER TABLE donation
    ADD CONSTRAINT pk_donation PRIMARY KEY (id_donation)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE donation
    ADD CONSTRAINT fk_donation_association
    FOREIGN KEY (id_association) REFERENCES association (id_user);
    
ALTER TABLE donation
    ADD CONSTRAINT fk_donation_crib_house
    FOREIGN KEY (id_crib_house) REFERENCES crib_house (id_user);

ALTER TABLE donation
    ADD CONSTRAINT fk_donation_currency
    FOREIGN KEY (id_currency) REFERENCES currency (id_currency);

-- ============================================
-- DONATION X USER
-- ============================================
CREATE TABLE donation_x_user
(
    id_user     NUMBER(8),
    id_donation NUMBER(8)
)
TABLESPACE TS_DATA;

ALTER TABLE donation_x_user
    MODIFY id_user CONSTRAINT donationXUser_idUser_nn NOT NULL;

ALTER TABLE donation_x_user
    MODIFY id_donation CONSTRAINT donationXUser_idDonation_nn NOT NULL;

ALTER TABLE donation_x_user
    ADD CONSTRAINT pk_donation_x_user PRIMARY KEY (id_user, id_donation)
    USING INDEX TABLESPACE TS_INDEX;

ALTER TABLE donation_x_user
    ADD CONSTRAINT fk_dxu_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user);

ALTER TABLE donation_x_user
    ADD CONSTRAINT fk_dxu_donation
    FOREIGN KEY (id_donation) REFERENCES donation (id_donation);