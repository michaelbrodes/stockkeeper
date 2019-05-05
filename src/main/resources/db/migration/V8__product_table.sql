/*
A mapping of "barcode" to the actual name of a tracked product. 
 */
CREATE TABLE IF NOT EXISTS product
(
  uuid    UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  -- when a product is first created we cannot guarantee its name. Therefore we allow null. Ideally
  -- we shouldn't have any nulls stored in the database.
  name    TEXT NULL,
  -- may not actually be a bar code if we support QR codes. Terminology in this domain isn't great.
  barcode TEXT NOT NULL,
  CONSTRAINT product_uk UNIQUE (name, barcode)
);

CREATE INDEX product_name_idx ON product(name);