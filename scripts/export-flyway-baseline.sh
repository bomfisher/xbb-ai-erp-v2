#!/usr/bin/env bash
set -euo pipefail

root_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
config_file="$root_dir/xbb-erp-app-admin/src/main/resources/application.yml"
output_file="$root_dir/xbb-erp-app-admin/src/main/resources/db/migration/V1__baseline.sql"

[[ "${1:-}" == "--confirm-baseline" ]] || {
  echo "用法: scripts/export-flyway-baseline.sh --confirm-baseline" >&2
  exit 2
}
[[ ! -e "$output_file" ]] || {
  echo "基线迁移已存在，拒绝覆盖: $output_file" >&2
  exit 1
}

connection=$(sed -nE 's#^[[:space:]]*url:[[:space:]]*jdbc:mysql://([^?]+).*#\1#p' "$config_file" | head -n 1)
host_port=${connection%%/*}
database_name=${connection#*/}
database_host=${host_port%:*}
database_port=${host_port##*:}
database_user=$(sed -nE 's/^[[:space:]]*username:[[:space:]]*(.*)$/\1/p' "$config_file" | head -n 1)
database_password=$(sed -nE 's/^[[:space:]]*password:[[:space:]]*(.*)$/\1/p' "$config_file" | head -n 1)

[[ -n "$database_host" && -n "$database_port" && -n "$database_name" && -n "$database_user" && -n "$database_password" ]] || {
  echo "应用配置缺少数据库连接信息" >&2
  exit 1
}

temporary_dump="$(mktemp)"
trap 'rm -f "$temporary_dump"' EXIT
export MYSQL_PWD="$database_password"

/usr/local/mysql/bin/mysqldump \
  --protocol=TCP \
  --ssl-mode=DISABLED \
  --column-statistics=0 \
  --host="$database_host" \
  --port="$database_port" \
  --user="$database_user" \
  --no-data \
  --skip-comments \
  --single-transaction \
  --set-gtid-purged=OFF \
  "$database_name" > "$temporary_dump"

{
  echo "-- Flyway schema baseline generated from an authorized read-only export."
  echo "-- The export contains table definitions only and no business rows."
  sed -E \
    -e '/^\/\*![0-9]+/d' \
    -e '/^DROP TABLE IF EXISTS /d' \
    -e 's/ AUTO_INCREMENT=[0-9]+//g' \
    "$temporary_dump"
} > "$output_file"

echo "Flyway 基线已生成: ${output_file#$root_dir/}"
echo "表数量: $(rg -c '^CREATE TABLE' "$output_file")"
