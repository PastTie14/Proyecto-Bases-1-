CREATE OR REPLACE TRIGGER beforeUpdateAdoptionForm
BEFORE INSERT OR UPDATE
ON adoption_form
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Adoption_Form', 'id_adoption', 'empty', :new.id_adoption);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Adoption_Form', 'notes', 'empty', :new.notes);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Adoption_Form', 'adoption_date', 'empty', :new.adoption_date);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Adoption_Form', 'reference', 'empty', :new."reference");

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Adoption_Form', 'id_adopter', 'empty', :new.id_adopter);

        INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
        VALUES (s_log.nextval, SYSDATE, USER, 'Adoption_Form', 'id_pet', 'empty', :new.id_pet);

    ELSE
        IF :old.notes <> :new.notes THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Adoption_Form', 'notes', :old.notes, :new.notes);
        END IF;

        IF :old.adoption_date <> :new.adoption_date THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Adoption_Form', 'adoption_date', :old.adoption_date, :new.adoption_date);
        END IF;

        IF :old."reference" <> :new."reference" THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Adoption_Form', 'reference', :old."reference", :new."reference");
        END IF;

        IF :old.id_adopter <> :new.id_adopter THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Adoption_Form', 'id_adopter', :old.id_adopter, :new.id_adopter);
        END IF;

        IF :old.id_pet <> :new.id_pet THEN
            INSERT INTO "log"(id_log, changeDate, changeBy, tableName, fieldName, previousValue, currentValue)
            VALUES (s_log.nextval, SYSDATE, USER, 'Adoption_Form', 'id_pet', :old.id_pet, :new.id_pet);
        END IF;
    END IF;
END beforeUpdateAdoptionForm;