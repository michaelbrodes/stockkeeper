-- TODO maybe group products by category to give the user some defaults?
/*
A type of item within a pantry. This is groups pantry_items that are related to one another,
but aren't necessarily the same item. An example would be "Aunt Jemima's Maple Syrup"
 */
CREATE TABLE IF NOT EXISTS pantry_item_category
(
  uuid        UUID PRIMARY KEY     DEFAULT uuid_generate_v4(),
  pantry_uuid UUID        NOT NULL,
  name        TEXT        NOT NULL,
  created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT pantry_item_category_pantry_fk
    FOREIGN KEY (pantry_uuid)
      REFERENCES pantry (uuid)
      ON DELETE CASCADE
);

CREATE TRIGGER pantry_item_category_auto_update
  BEFORE UPDATE
  ON pantry_item_category
  FOR EACH ROW
EXECUTE PROCEDURE auto_update();
