#!/usr/bin/env bash
# Runs the end-to-end tests inside the toolbox container.
set -euo pipefail

cd "$(dirname "$0")"

./test-e2e-tests.sh "$@"
