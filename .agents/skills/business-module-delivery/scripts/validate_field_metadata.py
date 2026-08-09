#!/usr/bin/env python3
"""校验业务模块字段元数据是否来自明确输入。"""

import argparse
import json
import re
from pathlib import Path
from typing import Any, Dict


SCENES = {"LIST", "CREATE", "UPDATE"}
ACTION_GROUPS = ("top", "bottom", "row")
FILTERABLE_FIELD_TYPES = {"TEXT", "USER", "DEPT", "BUSINESS", "COMB", "NUM_INT", "NUM_DOUBLE", "AMOUNT", "DATE", "TIME"}
NON_FILTERABLE_FIELD_TYPES = {"FILE", "IMAGE", "ADDRESS", "SUB_ITEM", "PRODUCT"}


def load_metadata(path: Path) -> Dict[str, Any]:
    try:
        metadata = json.loads(path.read_text(encoding="utf-8"))
    except FileNotFoundError as error:
        raise ValueError(f"缺少字段元数据输入，请向开发者确认后提供：{path}") from error
    except json.JSONDecodeError as error:
        raise ValueError(f"字段元数据不是有效 JSON，请向开发者确认后修正：{path}") from error
    if not isinstance(metadata, dict):
        raise ValueError("字段元数据根节点必须是对象")
    return metadata


def require_string(value: Any, path: str, errors: list[str]) -> None:
    if not isinstance(value, str) or not value.strip():
        errors.append(f"缺少明确输入：{path}")


def validate(metadata: Dict[str, Any]) -> list[str]:
    errors: list[str] = []

    def validate_field(field: Any, prefix: str, child: bool = False) -> None:
        if not isinstance(field, dict):
            errors.append(f"{prefix} 必须是对象")
            return
        for key in ("name", "attr", "attrName", "fieldType"):
            require_string(field.get(key), f"{prefix}.{key}", errors)
        scenes = field.get("scenes")
        if not isinstance(scenes, list) or not scenes:
            errors.append(f"缺少明确输入：{prefix}.scenes")
        elif invalid_scenes := [scene for scene in scenes if scene not in SCENES]:
            errors.append(f"{prefix}.scenes 包含不支持场景：{'、'.join(invalid_scenes)}")
        if "filterName" not in field:
            errors.append(f"缺少明确输入：{prefix}.filterName（不可筛选请显式为 null）")
        elif field["filterName"] is not None:
            require_string(field["filterName"], f"{prefix}.filterName", errors)
            if child or field.get("fieldType") not in FILTERABLE_FIELD_TYPES:
                errors.append(f"{prefix}.fieldType 不支持筛选，filterName 必须为 null")
        if field.get("fieldType") in NON_FILTERABLE_FIELD_TYPES and field.get("filterName") is not None:
            errors.append(f"{prefix}.fieldType 不支持筛选，filterName 必须为 null")
        if field.get("fieldType") == "SUB_ITEM":
            sub_fields = field.get("subFields")
            if not isinstance(sub_fields, list):
                errors.append(f"缺少明确输入：{prefix}.subFields（可显式为 []）")
            else:
                for index, sub_field in enumerate(sub_fields):
                    validate_field(sub_field, f"{prefix}.subFields[{index}]", child=True)

    business_code = metadata.get("businessCode")
    require_string(business_code, "businessCode", errors)
    if isinstance(business_code, str) and not re.fullmatch(r"[A-Z][A-Z0-9_]*", business_code):
        errors.append("businessCode 必须是显式的大写枚举值")

    fields = metadata.get("fields")
    if not isinstance(fields, list):
        errors.append("缺少明确输入：fields（可显式为 []）")
    else:
        for index, field in enumerate(fields):
            validate_field(field, f"fields[{index}]")

    actions = metadata.get("listActions")
    if not isinstance(actions, dict):
        errors.append("缺少明确输入：listActions")
    else:
        for group in ACTION_GROUPS:
            action_items = actions.get(group)
            if not isinstance(action_items, list):
                errors.append(f"缺少明确输入：listActions.{group}（无动作请显式为 []）")
                continue
            for index, action in enumerate(action_items):
                if not isinstance(action, dict):
                    errors.append(f"listActions.{group}[{index}] 必须是对象")
                    continue
                require_string(action.get("actionCode"), f"listActions.{group}[{index}].actionCode", errors)
                require_string(action.get("actionName"), f"listActions.{group}[{index}].actionName", errors)
    return errors


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("field_metadata", type=Path)
    args = parser.parse_args()
    errors = validate(load_metadata(args.field_metadata))
    if errors:
        raise ValueError("字段元数据校验失败，请向开发者确认后补充：\n- " + "\n- ".join(errors))
    print(f"字段元数据校验通过：{args.field_metadata}")


if __name__ == "__main__":
    main()
