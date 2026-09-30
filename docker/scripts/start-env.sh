#!/usr/bin/env bash
# 啟動 openfire + toolbox container，讓環境準備好可以跑 scripts/test.sh。
set -euo pipefail

cd "$(dirname "$0")/.."

echo "== starting openfire =="
docker compose up -d openfire

echo "== waiting for http://localhost:9090 =="
for i in $(seq 1 30); do
  curl -sf http://localhost:9090 > /dev/null && break
  sleep 1
done

echo "== building/starting toolbox =="
docker compose build toolbox
docker compose up -d toolbox

echo "== environment ready =="
