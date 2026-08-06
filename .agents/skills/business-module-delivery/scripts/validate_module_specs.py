#!/usr/bin/env python3
"""校验通用业务模块 ROOT/CHILD 代码生成规格。"""

import argparse
from pathlib import Path
from typing import Dict, Union


SpecValue = Union[str, bool]


def property_value(content: str, property_name: str) -> str:
    prefix = f"{property_name}:"
    for line in content.splitlines():
        normalized_line = line.lstrip()
        if normalized_line.startswith(prefix):
            return normalized_line.split(":", 1)[1].strip()
    raise ValueError(f"缺少属性：{property_name}")


def boolean_value(content: str, property_name: str) -> bool:
    value = property_value(content, property_name)
    if value not in ("true", "false"):
        raise ValueError(f"属性 {property_name} 必须为 true 或 false")
    return value == "true"


def spec_values(path: Path) -> Dict[str, SpecValue]:
    content = path.read_text(encoding="utf-8")
    return {
        "moduleCode": property_value(content, "moduleCode"),
        "aggregateName": property_value(content, "aggregateName"),
        "tableName": property_value(content, "tableName"),
        "aggregateRole": property_value(content, "aggregateRole"),
        "admin": boolean_value(content, "admin"),
        "application": boolean_value(content, "application"),
        "domain": boolean_value(content, "domain"),
        "persistence": boolean_value(content, "persistence"),
        "xml": boolean_value(content, "xml"),
    }


def validate_root(path: Path, values: Dict[str, SpecValue]) -> None:
    if values["aggregateRole"] != "ROOT":
        raise ValueError(f"{path} 必须使用 ROOT 角色")
    required_generation = ("admin", "application", "domain", "persistence", "xml")
    missing = [key for key in required_generation if not values[key]]
    if missing:
        raise ValueError(f"{path} 的 ROOT 规格必须生成：{'、'.join(missing)}")


def validate_child(path: Path, root: Dict[str, SpecValue], child: Dict[str, SpecValue]) -> None:
    if child["moduleCode"] != root["moduleCode"]:
        raise ValueError(f"{path} 与主表规格的 moduleCode 不一致")
    if child["aggregateRole"] != "CHILD":
        raise ValueError(f"{path} 必须使用 CHILD 角色")
    if child["aggregateName"] == root["aggregateName"] or child["tableName"] == root["tableName"]:
        raise ValueError(f"{path} 的聚合名和表名必须与主表不同")
    if child["admin"] or child["application"]:
        raise ValueError(f"{path} 的 CHILD 规格不得生成 admin 或 application")
    required_generation = ("domain", "persistence", "xml")
    missing = [key for key in required_generation if not child[key]]
    if missing:
        raise ValueError(f"{path} 的 CHILD 规格必须生成：{'、'.join(missing)}")


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("root_spec", type=Path)
    parser.add_argument("child_specs", nargs="*", type=Path)
    args = parser.parse_args()

    root = spec_values(args.root_spec)
    validate_root(args.root_spec, root)
    for child_path in args.child_specs:
        validate_child(child_path, root, spec_values(child_path))
    print(f"业务模块规格校验通过：{args.root_spec}，从表数量 {len(args.child_specs)}")


if __name__ == "__main__":
    main()
