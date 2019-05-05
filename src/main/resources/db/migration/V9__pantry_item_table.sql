/*
A product within a pantry. This relation enables users to override the "name" of a product for 
their particular pantry.
 */
CREATE TABLE IF NOT EXISTS pantry_item
(
  uuid                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  pantry_item_category_uuid UUID    NOT NULL,
  product_uuid          UUID    NOT NULL,
  -- The pantry level override of the name stored in the product table.
  name_override         TEXT    NULL,
  decay_rate            NUMERIC NOT NULL DEFAULT 1.0,
  -- TODO right now we just store seller on the pantry_item itself. Later we will need some extra metadata about sellers, so it may be worth adding a new relation.
  seller                TEXT    NULL,

  CONSTRAINT pantry_item_pantry_item_category_fk
    FOREIGN KEY (pantry_item_category_uuid)
      REFERENCES pantry_item_category (uuid)
      ON DELETE CASCADE,
  CONSTRAINT pantry_item_product_fk
    FOREIGN KEY (product_uuid)
      REFERENCES product (uuid)
      ON DELETE CASCADE
);