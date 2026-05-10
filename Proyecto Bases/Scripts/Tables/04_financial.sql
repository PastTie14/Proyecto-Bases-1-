-- ============================================================
-- FILE 04 - FINANCIAL
-- Tables: donation
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
    id_currency    NUMBER(4),
    id_donnor      NUMBER(8),
    createdBy  VARCHAR2(20),
    createdAt  DATE,
    modifiedBy VARCHAR2(20),
    modifiedAt DATE
)
TABLESPACE TS_DATA;

ALTER TABLE donation
    MODIFY id_donation CONSTRAINT donation_idDonation_nn NOT NULL;

ALTER TABLE donation
    MODIFY amount CONSTRAINT donation_amount_nn NOT NULL;

ALTER TABLE donation 
    ADD CONSTRAINT chk_donation_receiver CHECK (
        (id_association IS NOT NULL AND id_crib_house IS NULL) OR
        (id_association IS NULL AND id_crib_house IS NOT NULL)
    );

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
    
ALTER TABLE donation
    ADD CONSTRAINT fk_donation_donnor
    FOREIGN KEY (id_donnor) REFERENCES "user" (id_user);


COMMENT ON TABLE donation
IS 'Stores the donation information';

COMMENT ON COLUMN donation.id_donation
IS 'Primary key, identifier for the donation id';

COMMENT ON COLUMN donation.amount
IS 'Amount of the donation';

COMMENT ON COLUMN donation.id_association
IS 'Foreign key, references the receiver association';

COMMENT ON COLUMN donation.id_crib_house
IS 'Foreign key, references the receiver crib house';

COMMENT ON COLUMN donation.id_currency
IS 'Foreign key, references the currency';

COMMENT ON COLUMN donation.id_donnor
IS 'Foreign key, references the donnor';

COMMENT ON COLUMN donation.CreatedBy
IS 'The user who created the table';

COMMENT ON COLUMN donation.CreatedAt
IS 'The date the table was created';

COMMENT ON COLUMN donation.ModifiedBy
IS 'The user who modified the table';

COMMENT ON COLUMN donation.ModifiedAt
IS 'The date the table was modified';

