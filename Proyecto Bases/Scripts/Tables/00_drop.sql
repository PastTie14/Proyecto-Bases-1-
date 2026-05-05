-- ============================================================
-- DROP ALL TABLES - ASOCIACIÓN QUIERO UN PELUDO
-- Execute this script to remove all tables from the schema
-- Order: most dependent tables first
-- ============================================================

-- MEDICAL (most dependent)
DROP TABLE treatment_x_disease;
DROP TABLE disease_x_medic_sheet;
DROP TABLE medic_sheet;

-- PET EXTRA INFO AND RELATED
DROP TABLE training_ease;
DROP TABLE energy_level;
DROP TABLE current_status;
DROP TABLE bounty;
DROP TABLE pet_extra_info;

-- MATCH
DROP TABLE parameters;
DROP TABLE match;
DROP TABLE value_type;

-- ADOPTION / SOCIAL
DROP TABLE rating;
DROP TABLE photo;
DROP TABLE adoption_form;

-- PHONE NUMBER (depends on user, pet, veterinarian)
DROP TABLE phone_number;

-- PET RELATIONSHIPS
DROP TABLE pet_x_district;
DROP TABLE pet_x_color;
DROP TABLE pet_type_x_crib_house;
DROP TABLE identification_chip;

-- PET
DROP TABLE pet;

-- VETERINARIAN
DROP TABLE veterinarian;

-- CATALOGS (pet)
DROP TABLE color;
DROP TABLE status;
DROP TABLE race;
DROP TABLE pet_type;

-- FINANCIAL
DROP TABLE donation_x_user;
DROP TABLE donation;

-- USER BLACKLIST
DROP TABLE user_x_black_list;
DROP TABLE black_list;

-- USER SUBTYPES AND LOG
DROP TABLE "log";
DROP TABLE crib_house;
DROP TABLE rescuer;
DROP TABLE adopter;
DROP TABLE association;

-- BASE USER
DROP TABLE "user";

-- DISEASE AND TREATMENT (no dependencies after cross tables are dropped)
DROP TABLE treatment;
DROP TABLE disease;

-- LOCATION
DROP TABLE district;
DROP TABLE canton;
DROP TABLE province;

-- FINANCIAL CATALOG
DROP TABLE currency;

DROP TABLE CRIB_HOUSE;
DROP TABLE "size";

DROP TABLE "user";
