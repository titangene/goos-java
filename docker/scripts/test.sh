#!/usr/bin/env bash
# Runs the end-to-end tests inside the toolbox container.
set -euo pipefail

cd "$(dirname "$0")/.."

sudo docker compose up -d --build toolbox
sudo docker compose exec toolbox bash /app/docker/scripts/run-e2e-tests.sh
