# Creates the stockkeeper user and the stockkeeper database. By default the 
# stockkeeper user inherits the password of the postgres user.

psql -v ON_ERROR_STOP=1 \
     --username "$POSTGRES_USER" \
     --dbname postgres \
     -c 'CREATE DATABASE stockkeeper;'
psql -v ON_ERROR_STOP=1 \
     --username "$POSTGRES_USER" \
     --dbname postgres \
     -c "CREATE USER stockkeeper WITH ENCRYPTED PASSWORD '$POSTGRES_PASSWORD';"
psql -v ON_ERROR_STOP=1 \
     --username "$POSTGRES_USER" \
     --dbname postgres \
     -c "GRANT ALL PRIVILEGES ON DATABASE stockkeeper TO stockkeeper;"
psql -v ON_ERROR_STOP=1 \
     --username "$POSTGRES_USER" \
     --dbname postgres \
     -c "GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO stockkeeper;"
