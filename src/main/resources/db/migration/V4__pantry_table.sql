CREATE TABLE IF NOT EXISTS pantry
(
  uuid        UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  -- There is a one-to-one relationship between pantry and family.
  family_uuid UUID NOT NULL UNIQUE,

  CONSTRAINT pantry_family_fk
    FOREIGN KEY (family_uuid)
      REFERENCES family (uuid)
);
