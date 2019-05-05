/*
This is version one of the family table. The family table is just a grouping of accounts. 
 */
CREATE TABLE IF NOT EXISTS family
(
  uuid        UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  name        VARCHAR(50), 
  created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(), 
  updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  -- Represents when the family was "marked" for deletion. This is to enable easy "undo". 
  deleted_at  TIMESTAMPTZ NULL
);

CREATE TRIGGER family_auto_update 
BEFORE UPDATE ON family 
FOR EACH ROW 
EXECUTE PROCEDURE auto_update();