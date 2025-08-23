#!/usr/bin/env bash
set -euo pipefail
echo "Iniciando MySQL con Docker..."
docker compose up -d
echo "Esperando 10s a que MySQL esté listo..."; sleep 10
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=turnos
export DB_USER=turnos
export DB_PASS=secret
echo "Levantando Spring Boot..."
./mvnw spring-boot:run || mvn spring-boot:run
