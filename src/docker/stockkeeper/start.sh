#!/bin/bash

# `docker-compose` doesn't have enough context to know when a service is actually 
# initialized and can accept connections. This script is to add that functionality to 
# docker-compose. It will `docker-compose start` a service and then wait until it can
# respond before it starts dependent services.

# inherit environment variables
set -e
# inherit developer specific overrides.
source .env.sh

# This function will repeatedly try to connect to a postgres instance with psql. It 
# will only return when the postgres instance is up.
function wait_on_db() 
{
    host=$1
    port=$2
    until PGPASSWORD=$DB_PASS psql -h "$host" -U "postgres" -c '\q'; do
        >&2 echo "Postgres $host:$port is unavailable - sleeping"
        sleep 3
    done
}

# This function will repeatedly query for health against a springboot 
# application. It will only return when the app returns a 200.
function wait_on_app() 
{
    host=$1
    port=$2
    
    until curl --fail -X GET "http://$host:$port/health/"; do 
        >&2 echo "App $host:$port is unavailable - sleeping"
        sleep 3
    done
}

docker-compose up --build --no-start

echo "Starting database and waiting until it is up!"
echo "================================================================================"
docker-compose start db
wait_on_db localhost 5432
echo "Database is up!"

echo "Starting server and waiting until it is up!"
echo "================================================================================"
docker-compose start server
wait_on_app localhost 8200
echo ""
echo "Server is up!"

echo "Starting client!"
echo "================================================================================"
docker-compose start client

# follow the server logs since we are in the server project.
docker logs -f stockkeeper_server_1