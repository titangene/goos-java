#!/usr/bin/env bash
# Compiles and runs the end-to-end tests inside the toolbox container.
set -euo pipefail

PROJ=/app
BUILD=$PROJ/build-docker

APP_CP=$(ls "$PROJ"/lib/deploy/*.jar | tr '\n' ':')
DEV_CP=$(ls "$PROJ"/lib/develop/*.jar | tr '\n' ':')

compile() {
  local src=$1 out=$2 cp=$3
  rm -rf "$out"
  mkdir -p "$out"
  javac -d "$out" -cp "$cp" -sourcepath "$src" $(find "$src" -name '*.java')
}

echo "== compiling app =="
compile "$PROJ/src" "$BUILD/app" "$APP_CP"

echo "== compiling end-to-end tests =="
compile "$PROJ/test/end-to-end" "$BUILD/e2e-test" "$BUILD/app:$APP_CP$DEV_CP"

echo "== waiting for Openfire (localhost:9090) =="
for i in $(seq 1 30); do
  curl -sf http://localhost:9090 > /dev/null && break
  sleep 1
done

echo "== running end-to-end tests =="
classes=$(cd "$BUILD/e2e-test" && find . -name '*.class' ! -name '*\$*' | sed 's|^\./||; s|\.class$||; s|/|.|g' | grep -E 'Test$')

xvfb-run -a java -cp "$BUILD/e2e-test:$BUILD/app:$APP_CP$DEV_CP" org.junit.runner.JUnitCore $classes
