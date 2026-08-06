#!/usr/bin/env python3
"""生成业务模块交付范围，保留主从表规格边界。"""

import argparse
import json
from pathlib import Path

from validate_field_metadata import load_metadata, validate


def property_value(path: Path, property_name: str) -> str:
    prefix = f"{property_name}:"
    for line in path.read_text(encoding="utf-8").splitlines():
        normalized_line = line.lstrip()
        if normalized_line.startswith(prefix):
            return normalized_line.split(":", 1)[1].strip()
    raise ValueError(f"{path} 缺少属性：{property_name}")


def spec_summary(path: Path) -> dict[str, str]:
    return {
        "spec": str(path),
        "aggregate": property_value(path, "aggregateName"),
        "table": property_value(path, "tableName"),
        "role": property_value(path, "aggregateRole"),
    }


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--module-code", required=True)
    parser.add_argument("--root-spec", type=Path, required=True)
    parser.add_argument("--child-spec", type=Path, action="append", default=[])
    parser.add_argument("--field-metadata", type=Path, required=True)
    parser.add_argument("--output", type=Path, required=True)
    args = parser.parse_args()

    root = spec_summary(args.root_spec)
    children = [spec_summary(path) for path in args.child_spec]
    metadata = load_metadata(args.field_metadata)
    metadata_errors = validate(metadata)
    if metadata_errors:
        raise ValueError("字段元数据校验失败，请向开发者确认后补充：\n- " + "\n- ".join(metadata_errors))
    if root["role"] != "ROOT":
        raise ValueError("主表规格的 aggregateRole 必须为 ROOT")
    if any(child["role"] != "CHILD" for child in children):
        raise ValueError("从表规格的 aggregateRole 必须为 CHILD")

    scope = {
        "moduleCode": args.module_code,
        "root": root,
        "children": children,
        "fieldMetadata": metadata,
        "rootEndpoints": ["list", "addItem", "updateItem", "saveDraft", "saveAndSubmit", "draftList", "loadDraft"],
    }
    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_text(json.dumps(scope, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")


if __name__ == "__main__":
    main()
