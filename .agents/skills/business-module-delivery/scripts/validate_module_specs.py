#!/usr/bin/env python3
"""校验通用业务模块 ROOT/CHILD 代码生成规格。"""

import argparse
import re
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


def optional_property_value(content: str, property_name: str) -> str:
    try:
        return property_value(content, property_name)
    except ValueError:
        return ""


def boolean_value(content: str, property_name: str) -> bool:
    value = property_value(content, property_name)
    if value not in ("true", "false"):
        raise ValueError(f"属性 {property_name} 必须为 true 或 false")
    return value == "true"


def spec_values(path: Path) -> Dict[str, SpecValue]:
    content = path.read_text(encoding="utf-8")
    return {
        "moduleDir": property_value(content, "moduleDir"),
        "moduleCode": property_value(content, "moduleCode"),
        "moduleApiName": optional_property_value(content, "moduleApiName"),
        "businessName": optional_property_value(content, "businessName"),
        "businessCode": optional_property_value(content, "businessCode"),
        "aggregateName": property_value(content, "aggregateName"),
        "tableName": property_value(content, "tableName"),
        "aggregateRole": property_value(content, "aggregateRole"),
        "admin": boolean_value(content, "admin"),
        "application": boolean_value(content, "application"),
        "domain": boolean_value(content, "domain"),
        "persistence": boolean_value(content, "persistence"),
        "xml": boolean_value(content, "xml"),
    }


def validate_module_identity(path: Path, values: Dict[str, SpecValue]) -> None:
    module_dir = str(values["moduleDir"])
    module_code = str(values["moduleCode"])
    if not re.fullmatch(r"[a-z0-9]+(?:-[a-z0-9]+)*", module_dir):
        raise ValueError(f"{path} 的 moduleDir 必须只使用小写字母、数字和短横线：{module_dir}")
    if not re.fullmatch(r"[a-z0-9]+(?:_[a-z0-9]+)*", module_code):
        raise ValueError(f"{path} 的 moduleCode 必须只使用小写字母、数字和下划线：{module_code}")


def validate_root(path: Path, values: Dict[str, SpecValue]) -> None:
    if values["aggregateRole"] != "ROOT":
        raise ValueError(f"{path} 必须使用 ROOT 角色")
    required_generation = ("admin", "application", "domain", "persistence", "xml")
    missing = [key for key in required_generation if not values[key]]
    if missing:
        raise ValueError(f"{path} 的 ROOT 规格必须生成：{'、'.join(missing)}")
    module_api_name = str(values["moduleApiName"])
    business_name = str(values["businessName"])
    business_code = str(values["businessCode"])
    if not module_api_name:
        raise ValueError(f"{path} 的 ROOT 规格缺少属性：moduleApiName")
    if not business_name:
        raise ValueError(f"{path} 的 ROOT 规格缺少属性：businessName")
    if not business_code:
        raise ValueError(f"{path} 的 ROOT 规格缺少属性：businessCode")
    if not re.fullmatch(r"[a-z][A-Za-z0-9]*", module_api_name):
        raise ValueError(f"{path} 的 moduleApiName 必须是小驼峰：{module_api_name}")
    if not re.fullmatch(r"[a-z][A-Za-z0-9]*", business_name):
        raise ValueError(f"{path} 的 businessName 必须是小驼峰：{business_name}")
    if not re.fullmatch(r"[A-Z][A-Z0-9_]*", business_code):
        raise ValueError(f"{path} 的 businessCode 必须是大写枚举值：{business_code}")


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
    validate_module_identity(args.root_spec, root)
    validate_root(args.root_spec, root)
    for child_path in args.child_specs:
        child = spec_values(child_path)
        validate_module_identity(child_path, child)
        validate_child(child_path, root, child)
    print(f"业务模块规格校验通过：{args.root_spec}，从表数量 {len(args.child_specs)}")


if __name__ == "__main__":
    main()
