CREATE OR REPLACE FUNCTION auto_update()
RETURNS TRIGGER 
AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;