/* 
A single item currently within a pantry. A single item can have a "partial" remaining_quantity. 
 */
CREATE TABLE IF NOT EXISTS in_stock_pantry_item
(
  uuid               UUID PRIMARY KEY     DEFAULT uuid_generate_v4(),
  pantry_item_uuid   UUID        NOT NULL,
  expiration_time    TIMESTAMPTZ NULL,
  remaining_quantity NUMERIC     NOT NULL DEFAULT 1.0,
  CONSTRAINT in_stock_pantry_item_pantry_item_fk
    FOREIGN KEY (pantry_item_uuid)
      REFERENCES pantry_item (uuid),
  CONSTRAINT in_stock_pantry_item_remaining_quantity_ck
    CHECK (0 < remaining_quantity AND remaining_quantity <= 1.0)
);

CREATE INDEX expiration_time_idx ON in_stock_pantry_item (expiration_time);
