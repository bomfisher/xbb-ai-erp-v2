#!/usr/bin/env bash
set -euo pipefail

root_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
module_name=""

usage() {
  echo "用法: scripts/harness-verify.sh [--module xbb-erp-module-<name>]"
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --module)
      module_name="${2:-}"
      shift 2
      ;;
    -h|--help)
      usage
      exit 0
      ;;
    *)
      usage
      exit 2
      ;;
  esac
done

fail() {
  echo "Harness 校验失败: $1" >&2
  exit 1
}

require_file() {
  [[ -f "$root_dir/$1" ]] || fail "缺少 $1"
}

verify_harness_docs() {
  require_file "docs/harness/README.md"
  require_file "docs/harness/工程规则唯一事实源.md"
  require_file "docs/harness/功能交付输入模板.md"
  require_file "docs/harness/数据库迁移规范.md"
  require_file "docs/harness/Flyway基线记录.md"
  require_file "docs/harness/前后端契约规范.md"
  require_file "docs/harness/CI门禁规范.md"
  require_file "docs/harness/验证矩阵.md"
  require_file "docs/harness/发布检查清单.md"
  require_file "docs/harness/后端文档规范.md"
  require_file "docs/harness/文档地图.md"
  require_file "docs/guide/业务模块目录规范.md"
  require_file "docs/base/项目业务module导航.md"
  require_file "docs/kn/总目录.md"
  [[ -x "$root_dir/scripts/harness-verify.sh" ]] || fail "Harness 校验脚本不可执行"
  [[ -x "$root_dir/scripts/harness-ci.sh" ]] || fail "CI 校验脚本不可执行"
  [[ -x "$root_dir/scripts/feature-worktree.sh" ]] || fail "工作树脚本不可执行"
  [[ -x "$root_dir/scripts/export-flyway-baseline.sh" ]] || fail "Flyway 基线导出脚本不可执行"
}

verify_skills() {
  while IFS= read -r skill_file; do
    head -n 1 "$skill_file" | grep -qx -- '---' || fail "SKILL frontmatter 缺失: ${skill_file#$root_dir/}"
    sed -n '2,20p' "$skill_file" | grep -q '^name: [a-z0-9-]\+$' || fail "SKILL 名称无效: ${skill_file#$root_dir/}"
    sed -n '2,20p' "$skill_file" | grep -q '^description: .\+$' || fail "SKILL 描述缺失: ${skill_file#$root_dir/}"
    ! grep -q '\[TODO\]' "$skill_file" || fail "SKILL 包含 TODO: ${skill_file#$root_dir/}"
  done < <(find "$root_dir/.agents/skills" -name SKILL.md -type f | sort)
}

verify_rule_drift() {
  ! grep -q '后缀必须是 `Entity`' "$root_dir/.claude/commands/init-module/SKILL.md" || fail "模块初始化 SKILL 仍使用 Entity 规则"
  ! grep -q '项目顶部和底部module导航' "$root_dir/.claude/commands/init-module/SKILL.md" || fail "模块初始化 SKILL 仍引用失效导航"
}

verify_migrations() {
  local migration_dir="$root_dir/xbb-erp-app-admin/src/main/resources/db/migration"
  [[ -d "$migration_dir" ]] || fail "缺少 Flyway 迁移目录"
  local versions_file
  versions_file="$(mktemp)"
  while IFS= read -r migration_file; do
    local file_name version
    file_name="$(basename "$migration_file")"
    [[ "$file_name" =~ ^V[0-9]+__[a-z0-9_]+\.sql$ ]] || fail "Flyway 文件命名无效: $file_name"
    version="${file_name%%__*}"
    echo "$version" >> "$versions_file"
  done < <(find "$migration_dir" -maxdepth 1 -type f -name '*.sql' | sort)
  [[ "$(sort "$versions_file" | uniq -d | wc -l | tr -d ' ')" == "0" ]] || fail "Flyway 迁移版本重复"
  local baseline_file="$migration_dir/V1__baseline.sql"
  if [[ -f "$baseline_file" ]]; then
    ! rg -q '^DROP TABLE|^INSERT INTO|^/\\*![0-9]+' "$baseline_file" || fail "Flyway 基线包含破坏性或数据语句"
  fi
  rm -f "$versions_file"
}

verify_module() {
  [[ -n "$module_name" ]] || return 0
  [[ "$module_name" =~ ^xbb-erp-module-[a-z0-9_]+$ ]] || fail "模块名必须为 xbb-erp-module-<name>"
  local module_dir="$root_dir/$module_name"
  local java_dir="$module_dir/src/main/java"
  [[ -d "$java_dir" ]] || fail "模块源码目录不存在: $module_name"
  local required_dir
  for required_dir in admin application domain infrastructure/persistence; do
    find "$java_dir" -type d -path "*/$required_dir" -print -quit | grep -q . || fail "$module_name 缺少 $required_dir 分层"
  done
  local po_dir
  while IFS= read -r po_dir; do
    while IFS= read -r po_file; do
      [[ "$(basename "$po_file")" == *PO.java ]] || fail "数据库对象必须使用 PO 后缀: ${po_file#$root_dir/}"
    done < <(find "$po_dir" -maxdepth 1 -type f -name '*.java' | sort)
    ! grep -R --include='*PO.java' -q '\bBoolean\b' "$po_dir" || fail "PO 禁止使用 Boolean: ${po_dir#$root_dir/}"
  done < <(find "$java_dir" -type d -path '*/infrastructure/persistence/po' | sort)
}

verify_harness_docs
verify_skills
verify_rule_drift
verify_migrations
verify_module
echo "Harness 校验通过"
