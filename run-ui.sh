#!/usr/bin/env bash
set -euo pipefail

JAVAFX_LIB="/Users/nimasaeidi/Desktop/M/JavaFX/javafx-sdk-26.0.1/lib"

"$(dirname "$0")/build.sh"
java \
  --module-path "$JAVAFX_LIB" \
  --add-modules javafx.controls \
  -cp bin \
  ui.GameApplication
