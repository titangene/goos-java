#!/usr/bin/env bash
# Runs the unit tests inside the toolbox container.
set -euo pipefail

cd "$(dirname "$0")/.."

sudo docker compose up -d --build --no-deps toolbox

sudo docker compose exec toolbox bash /app/docker/scripts/run-unit-tests.sh
