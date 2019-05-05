/*
This is version one of the family_permission table. The family_permission table is the relational
version of the FamilyPermission enum. Technically postgres does have enum types, but I was told that 
this the recommended approach.
 */
CREATE TABLE IF NOT EXISTS family_permission
(
  uid        BIGINT PRIMARY KEY,
  permission VARCHAR(20) NOT NULL UNIQUE
);

-- TODO maybe move this query into a context initialization listener?
INSERT INTO family_permission (uid, permission)
VALUES (1, 'OWNER'),
       (2, 'EDITOR'),
       (3, 'VIEWER');