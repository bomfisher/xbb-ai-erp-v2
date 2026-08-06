#!/usr/bin/env bash
set -euo pipefail

root_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
module_name="${1:-}"

cd "$root_dir"
scripts/harness-verify.sh

if [[ -n "$module_name" ]]; then
  scripts/harness-verify.sh --module "$module_name"
  mvn -B -pl "$module_name" -am test
else
  mvn -B test
fi
