#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 \
     --username "$POSTGRES_USER" \
     --dbname stockkeeper \
     -c 'CREATE EXTENSION IF NOT EXISTS "uuid-ossp";'
psql -v ON_ERROR_STOP=1 \
     --username "$POSTGRES_USER" \
     --dbname postgres \
     -c 'CREATE EXTENSION IF NOT EXISTS "uuid-ossp";'
