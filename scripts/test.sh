#!/usr/bin/env bash
# 在 toolbox container 內編譯 app 並跑 E2E 測試。
set -euo pipefail

cd "$(dirname "$0")/.."

docker compose --progress quiet up -d toolbox

docker compose exec toolbox bash -c '
  set -euo pipefail
  PROJ=/app
  BUILD=$PROJ/build-docker
  DEV_CP=$(ls "$PROJ"/lib/develop/*.jar | tr "\n" ":")

  compile() {
    local src=$1 out=$2 cp=$3
    rm -rf "$out"
    mkdir -p "$out"
    javac -d "$out" -cp "$cp" -sourcepath "$src" $(find "$src" -name "*.java")
  }

  echo "== compiling end-to-end tests =="
  compile "$PROJ/test/end-to-end" "$BUILD/e2e-test" "$DEV_CP"

  echo "== running end-to-end tests =="
  classes=$(cd "$BUILD/e2e-test" && find . -name "*.class" ! -name "*\$*" | sed "s|^\./||; s|\.class$||; s|/|.|g" | grep -E "Test$")

  java -cp "$BUILD/e2e-test:$DEV_CP" org.junit.runner.JUnitCore $classes
'
