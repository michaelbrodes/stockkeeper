/*
The account table contains the username and password of users using stockkeeper. 
 */
CREATE TABLE IF NOT EXISTS account (
   uuid UUID PRIMARY KEY DEFAULT uuid_generate_v4(), 
   username TEXT NOT NULL
); 

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
