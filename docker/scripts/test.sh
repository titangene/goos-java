#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"

./test-unit-tests.sh
./test-e2e-tests.sh "$@"
