#!/usr/bin/env bash
# Runs the unit tests inside the toolbox container.
# --no-deps 讓這裡不會因為 docker-compose.yml 的 openfire 區塊有變動就重建
# openfire container（會清掉已經建好的測試帳號）。
set -euo pipefail

cd "$(dirname "$0")/.."

docker compose up -d --build --no-deps toolbox

docker compose exec toolbox bash /app/docker/scripts/run-unit-tests.sh
