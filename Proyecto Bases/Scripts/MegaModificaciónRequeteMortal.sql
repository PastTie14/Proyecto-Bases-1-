ALTER TABLE canton DROP CONSTRAINT fk_canton_province;

ALTER TABLE canton
    ADD CONSTRAINT fk_canton_province
    FOREIGN KEY (id_province) REFERENCES province (id_province)
    ON DELETE CASCADE;


ALTER TABLE district DROP CONSTRAINT fk_district_canton;

ALTER TABLE district
    ADD CONSTRAINT fk_district_canton
    FOREIGN KEY (id_canton) REFERENCES canton (id_canton)
    ON DELETE CASCADE;


ALTER TABLE race DROP CONSTRAINT fk_race_pet_type;

ALTER TABLE race
    ADD CONSTRAINT fk_race_pet_type
    FOREIGN KEY (id_pet_type) REFERENCES pet_type (id_pet_type)
    ON DELETE CASCADE;


ALTER TABLE association DROP CONSTRAINT fk_association_user;

ALTER TABLE association
ADD CONSTRAINT fk_association_user
FOREIGN KEY (id_user) REFERENCES "user" (id_user)
ON DELETE CASCADE;

ALTER TABLE adopter DROP CONSTRAINT fk_adopter_user;

ALTER TABLE adopter
    ADD CONSTRAINT fk_adopter_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user)
    ON DELETE CASCADE;
    
ALTER TABLE rescuer DROP CONSTRAINT fk_rescuer_user;

ALTER TABLE rescuer
    ADD CONSTRAINT fk_rescuer_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user)
    ON DELETE CASCADE;
    
ALTER TABLE crib_house DROP CONSTRAINT fk_crib_house_user;

ALTER TABLE crib_house
    ADD CONSTRAINT fk_crib_house_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user)
    ON DELETE CASCADE;
    
    
    
    
ALTER TABLE user_x_black_list DROP CONSTRAINT fk_uxbl_user;

ALTER TABLE user_x_black_list
    ADD CONSTRAINT fk_uxbl_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user)
    ON DELETE CASCADE;

ALTER TABLE user_x_black_list DROP CONSTRAINT fk_uxbl_report;

ALTER TABLE user_x_black_list
    ADD CONSTRAINT fk_uxbl_report
    FOREIGN KEY (id_report) REFERENCES black_list (id_report)
    ON DELETE CASCADE;
    

ALTER TABLE identification_chip DROP CONSTRAINT fk_chip_pet;

ALTER TABLE identification_chip
    ADD CONSTRAINT fk_chip_pet
    FOREIGN KEY (id_pet) REFERENCES pet (id_pet)
    ON DELETE CASCADE;
   
 
ALTER TABLE pet_x_color DROP CONSTRAINT fk_pxc_pet;
    
ALTER TABLE pet_x_color
    ADD CONSTRAINT fk_pxc_pet
    FOREIGN KEY (id_pet) REFERENCES pet (id_pet)
    ON DELETE CASCADE;

ALTER TABLE pet_x_color DROP CONSTRAINT fk_pxc_color;

ALTER TABLE pet_x_color
    ADD CONSTRAINT fk_pxc_color
    FOREIGN KEY (id_color) REFERENCES color (id_color)
    ON DELETE CASCADE;

ALTER TABLE pet_x_district DROP CONSTRAINT fk_pxd_pet;
    
ALTER TABLE pet_x_district
    ADD CONSTRAINT fk_pxd_pet
    FOREIGN KEY (id_pet) REFERENCES pet (id_pet)
    ON DELETE CASCADE;

ALTER TABLE pet_x_district DROP CONSTRAINT fk_pxd_district;

ALTER TABLE pet_x_district
    ADD CONSTRAINT fk_pxd_district
    FOREIGN KEY (id_district) REFERENCES district (id_district)
    ON DELETE CASCADE;
    
ALTER TABLE pet_type_x_crib_house DROP CONSTRAINT fk_ptxch_pet_type;

ALTER TABLE pet_type_x_crib_house
    ADD CONSTRAINT fk_ptxch_pet_type
    FOREIGN KEY (id_pet_type) REFERENCES pet_type (id_pet_type)
    ON DELETE CASCADE;

ALTER TABLE pet_type_x_crib_house DROP CONSTRAINT fk_ptxch_crib_house;

ALTER TABLE pet_type_x_crib_house
    ADD CONSTRAINT fk_ptxch_crib_house
    FOREIGN KEY (id_crib_house) REFERENCES crib_house (id_user)
    ON DELETE CASCADE;
    
    

ALTER TABLE phone_number DROP CONSTRAINT fk_phone_user;
    
ALTER TABLE phone_number
    ADD CONSTRAINT fk_phone_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user)
    ON DELETE CASCADE;

ALTER TABLE phone_number DROP CONSTRAINT fk_phone_pet;

ALTER TABLE phone_number
    ADD CONSTRAINT fk_phone_pet
    FOREIGN KEY (id_pet) REFERENCES pet (id_pet)
    ON DELETE CASCADE;

ALTER TABLE phone_number DROP CONSTRAINT fk_phone_veterinarian;

ALTER TABLE phone_number
    ADD CONSTRAINT fk_phone_veterinarian
    FOREIGN KEY (id_veterinarian) REFERENCES veterinarian (id_veterinarian)
    ON DELETE CASCADE;
    
    
ALTER TABLE photo DROP CONSTRAINT fk_photo_user;
    
ALTER TABLE photo
    ADD CONSTRAINT fk_photo_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user)
    ON DELETE CASCADE;
    

ALTER TABLE adoption_form DROP CONSTRAINT fk_af_adopter;
    
ALTER TABLE adoption_form
    ADD CONSTRAINT fk_af_adopter
    FOREIGN KEY (id_adopter) REFERENCES adopter (id_user)
    ON DELETE CASCADE;

ALTER TABLE adoption_form DROP CONSTRAINT fk_af_pet;

ALTER TABLE adoption_form
    ADD CONSTRAINT fk_af_pet
    FOREIGN KEY (id_pet) REFERENCES pet (id_pet)
    ON DELETE CASCADE;    

ALTER TABLE rating DROP CONSTRAINT fk_rating_user;
    
ALTER TABLE rating
    ADD CONSTRAINT fk_rating_user
    FOREIGN KEY (id_user) REFERENCES "user" (id_user)
    ON DELETE CASCADE;

ALTER TABLE rating DROP CONSTRAINT fk_rating_adopter;

ALTER TABLE rating
    ADD CONSTRAINT fk_rating_adopter
    FOREIGN KEY (id_adopter) REFERENCES adopter (id_user)
    ON DELETE CASCADE;
    
ALTER TABLE pet_extra_info DROP CONSTRAINT fk_pei_pet;

ALTER TABLE pet_extra_info
    ADD CONSTRAINT fk_pei_pet
    FOREIGN KEY (id_pet) REFERENCES pet (id_pet)
    ON DELETE CASCADE;
    

ALTER TABLE bounty DROP CONSTRAINT fk_bounty_pet_extra_info;
    
ALTER TABLE bounty
    ADD CONSTRAINT fk_bounty_pet_extra_info
    FOREIGN KEY (id_pet_extra_info) REFERENCES pet_extra_info (id_pet_extra_info)
    ON DELETE CASCADE;

ALTER TABLE bounty DROP CONSTRAINT fk_bounty_currency;

ALTER TABLE bounty
    ADD CONSTRAINT fk_bounty_currency
    FOREIGN KEY (id_currency) REFERENCES currency (id_currency)
    ON DELETE CASCADE;
    

ALTER TABLE medic_sheet DROP CONSTRAINT fk_ms_pet_extra_info;
    
ALTER TABLE medic_sheet
    ADD CONSTRAINT fk_ms_pet_extra_info
    FOREIGN KEY (id_pet_extra_info) REFERENCES pet_extra_info (id_pet_extra_info)
    ON DELETE CASCADE;
    
    
ALTER TABLE disease_x_medic_sheet DROP CONSTRAINT fk_dxms_disease;
    
ALTER TABLE disease_x_medic_sheet
    ADD CONSTRAINT fk_dxms_disease
    FOREIGN KEY (id_disease) REFERENCES disease (id_disease)
    ON DELETE CASCADE;

ALTER TABLE disease_x_medic_sheet DROP CONSTRAINT fk_dxms_medic_sheet;

ALTER TABLE disease_x_medic_sheet
    ADD CONSTRAINT fk_dxms_medic_sheet
    FOREIGN KEY (id_medic_sheet) REFERENCES medic_sheet (id_medic_sheet)
    ON DELETE CASCADE;
    
ALTER TABLE treatment_x_disease DROP CONSTRAINT fk_txd_treatment;

ALTER TABLE treatment_x_disease
    ADD CONSTRAINT fk_txd_treatment
    FOREIGN KEY (id_treatment) REFERENCES treatment (id_treatment)
    ON DELETE CASCADE;

ALTER TABLE treatment_x_disease DROP CONSTRAINT fk_txd_disease;

ALTER TABLE treatment_x_disease
    ADD CONSTRAINT fk_txd_disease
    FOREIGN KEY (id_disease) REFERENCES disease (id_disease)
    ON DELETE CASCADE;