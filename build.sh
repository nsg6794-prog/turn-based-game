#!/usr/bin/env bash
set -euo pipefail

JAVAFX_LIB="$(cd "$(dirname "$0")" && pwd)/lib"

mkdir -p bin

compile_to() {
  local output_dir="$1"

  mkdir -p "$output_dir"
  javac \
    --module-path "$JAVAFX_LIB" \
    --add-modules javafx.controls \
    -d "$output_dir" \
    src/game/*.java src/combat/*.java src/items/*.java src/ui/*.java

  if [ -d assets ]; then
    mkdir -p "$output_dir/assets"
    cp assets/* "$output_dir/assets/"
  fi
}

compile_to bin
compile_to out
