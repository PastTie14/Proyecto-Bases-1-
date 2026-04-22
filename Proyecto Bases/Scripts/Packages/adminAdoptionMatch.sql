CREATE OR REPLACE PACKAGE adminAdoptionMatch IS

PROCEDURE insertAdoptionForm(id_adoption IN NUMBER, notes VARCHAR2,
                                        adoption_date IN DATE, "reference" IN VARCHAR2,
                                        id_adopter IN NUMBER, id_pet IN NUMBER);

PROCEDURE insertPhoto(id_photo IN NUMBER, "date" IN DATE, 
                                    photo_dir IN VARCHAR2, id_adopter IN NUMBER);

PROCEDURE insertRating(id_rating IN NUMBER, score IN NUMBER, 
                                        id_user IN NUMBER, id_adopter IN NUMBER);

PROCEDURE insertMatch(id_match IN NUMBER, match_date IN DATE,
                                        similarity_percentage IN NUMBER);

PROCEDURE insertParameters(id_parameter IN NUMBER, "value" IN VARCHAR2,
                                        id_match IN NUMBER, id_value_type IN NUMBER);

END adminAdoptionMatch;