-- Creates the stockkeeper user and the stockkeeper database. By default the stockkeeper user has 
-- a bad password. That password isn't used in production.
CREATE DATABASE stockkeeper;
CREATE USER stockkeeper WITH ENCRYPTED PASSWORD 'change-me';
GRANT ALL PRIVILEGES ON DATABASE stockkeeper TO stockkeeper;