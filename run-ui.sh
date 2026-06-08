#!/usr/bin/env bash
set -euo pipefail

JAVAFX_LIB="$(cd "$(dirname "$0")" && pwd)/lib"

"$(dirname "$0")/build.sh"
java \
  --enable-native-access=javafx.graphics \
  --module-path "$JAVAFX_LIB" \
  --add-modules javafx.controls \
  -cp bin \
  ui.GameApplication
