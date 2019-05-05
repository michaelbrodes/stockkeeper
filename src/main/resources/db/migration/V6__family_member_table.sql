/*
A person (account) that belongs to a family.
 */
CREATE TABLE IF NOT EXISTS family_member
(
  uuid                  UUID   PRIMARY KEY DEFAULT uuid_generate_v4(),
  account_uuid          UUID   NOT NULL,
  family_uuid           UUID   NOT NULL,
  family_permission_uid BIGINT NOT NULL,
  CONSTRAINT family_member_account_fk
    FOREIGN KEY (account_uuid)
      REFERENCES account (uuid)
      ON DELETE CASCADE,
  CONSTRAINT family_member_family_fk
    FOREIGN KEY (family_uuid)
      REFERENCES family (uuid)
      ON DELETE CASCADE,
  CONSTRAINT family_member_family_permission_fk
    FOREIGN KEY (family_permission_uid)
      REFERENCES family_permission (uid)
      ON DELETE SET NULL,
  CONSTRAINT family_member_uk
    UNIQUE (account_uuid, family_uuid, family_permission_uid)
);