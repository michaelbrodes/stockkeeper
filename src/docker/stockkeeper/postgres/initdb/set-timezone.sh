#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 \
     --username "$POSTGRES_USER" \
     --dbname stockkeeper \
     -c "SET TIME ZONE 'UTC'"
psql -v ON_ERROR_STOP=1 \
     --username "$POSTGRES_USER" \
     --dbname postgres \
     -c "SET TIME ZONE 'UTC'"

