#!/usr/bin/env bash
set -euo pipefail

root_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

usage() {
  echo "用法: scripts/feature-worktree.sh create <feature-name> [base-ref] | list | remove <path> --confirm"
}

case "${1:-}" in
  create)
    feature_name="${2:-}"
    base_ref="${3:-HEAD}"
    [[ "$feature_name" =~ ^[a-z0-9][a-z0-9-]*$ ]] || { usage; exit 2; }
    target_dir="$(dirname "$root_dir")/$(basename "$root_dir")-$feature_name"
    branch_name="agent/$feature_name"
    [[ ! -e "$target_dir" ]] || { echo "目标目录已存在: $target_dir" >&2; exit 1; }
    git -C "$root_dir" show-ref --verify --quiet "refs/heads/$branch_name" && { echo "分支已存在: $branch_name" >&2; exit 1; }
    git -C "$root_dir" worktree add -b "$branch_name" "$target_dir" "$base_ref"
    ;;
  list)
    git -C "$root_dir" worktree list
    ;;
  remove)
    target_dir="${2:-}"
    confirmation="${3:-}"
    [[ -n "$target_dir" && "$confirmation" == "--confirm" ]] || { usage; exit 2; }
    git -C "$root_dir" worktree remove "$target_dir"
    ;;
  *)
    usage
    exit 2
    ;;
esac
