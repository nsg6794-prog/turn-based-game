#!/usr/bin/env bash
set -euo pipefail

JAVAFX_LIB="$(cd "$(dirname "$0")" && pwd)/lib"

mkdir -p bin
javac \
  --module-path "$JAVAFX_LIB" \
  --add-modules javafx.controls \
  -d bin \
  src/game/*.java src/combat/*.java src/items/*.java src/ui/*.java
