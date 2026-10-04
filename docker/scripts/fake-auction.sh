#!/usr/bin/env bash
# 互動式假拍賣工具 -- 手動送出 PRICE/CLOSE 事件，驅動已加入拍賣的 sniper。
# 可用指令見 docker/tools/FakeAuction.java 裡的說明（等它印出 ">>> " 提示字元
# 後就可以輸入），完整操作步驟見 docs/fake-auction.md。
#
# Usage:
#   docker/scripts/fake-auction.sh <itemId>
#   e.g. docker/scripts/fake-auction.sh item-54321
set -euo pipefail

cd "$(dirname "$0")/.."

if [ $# -lt 1 ]; then
  echo "usage: $0 <itemId>" >&2
  exit 1
fi

ITEM_ID=$1

docker compose up -d --build --no-deps toolbox
docker compose exec -e ITEM_ID="$ITEM_ID" toolbox bash -c '
  set -euo pipefail
  PROJ=/app
  BUILD=$PROJ/build-docker
  APP_CP=$(ls "$PROJ"/lib/deploy/*.jar | tr "\n" ":")

  mkdir -p "$BUILD/tools"
  javac -d "$BUILD/tools" -cp "$APP_CP" "$PROJ/docker/tools/FakeAuction.java"
  java -cp "$BUILD/tools:$APP_CP" FakeAuction "$ITEM_ID"
'
