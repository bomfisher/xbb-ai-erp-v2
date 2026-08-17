#!/usr/bin/env python3
"""校验业务模块字段元数据是否来自明确输入。"""

import argparse
import json
import re
from pathlib import Path
from typing import Any, Dict


SCENES = {"LIST", "CREATE", "UPDATE"}
ACTION_GROUPS = ("top", "bottom", "row")
FILTERABLE_FIELD_TYPES = {
    "TEXT", "USER", "DEPT", "BUSINESS", "COMB", "COMB_MULTI", "CHECKBOX", "RADIO_BTN", "SWITCH",
    "NUM_INT", "NUM_DOUBLE", "AMOUNT", "STOCK", "DATE", "TIME",
}
NON_FILTERABLE_FIELD_TYPES = {"FILE", "IMAGE", "ADDRESS", "SUB_ITEM", "PRODUCT"}
FIXED_BUSINESS_CODES = {"USER": "ORG_MEMBER", "DEPT": "ORG_DEPARTMENT"}
EXPLICIT_BUSINESS_CODE_FIELD_TYPES = {"BUSINESS", "PRODUCT"}
FILTER_PROTOCOL_TYPES = {
    "TEXT": "TEXT", "USER": "ID", "DEPT": "ID", "BUSINESS": "BUSINESS",
    "COMB": "ENUM", "RADIO_BTN": "ENUM", "SWITCH": "ENUM",
    "COMB_MULTI": "ENUM_MULTI", "CHECKBOX": "ENUM_MULTI",
    "NUM_INT": "NUM_INT", "NUM_DOUBLE": "NUM_DOUBLE", "AMOUNT": "AMOUNT", "STOCK": "STOCK",
    "DATE": "DATE", "TIME": "TIME",
}
FILTER_SUPPORTED_SYMBOLS = {
    "TEXT": ["EQ", "NE", "CONTAINS", "NOT_CONTAINS", "IS_EMPTY", "IS_NOT_EMPTY"],
    "USER": ["EQ", "NE", "IN", "IS_EMPTY", "IS_NOT_EMPTY"],
    "DEPT": ["EQ", "NE", "IN", "IS_EMPTY", "IS_NOT_EMPTY"],
    "BUSINESS": ["EQ", "NE", "IN", "IS_EMPTY", "IS_NOT_EMPTY"],
    "COMB": ["CONTAINS", "NOT_CONTAINS", "IS_EMPTY", "IS_NOT_EMPTY"],
    "RADIO_BTN": ["CONTAINS", "NOT_CONTAINS", "IS_EMPTY", "IS_NOT_EMPTY"],
    "SWITCH": ["CONTAINS", "NOT_CONTAINS", "IS_EMPTY", "IS_NOT_EMPTY"],
    "COMB_MULTI": ["CONTAINS", "NOT_CONTAINS", "CONTAINS_ALL", "NOT_CONTAINS_ALL", "IS_EMPTY", "IS_NOT_EMPTY"],
    "CHECKBOX": ["CONTAINS", "NOT_CONTAINS", "CONTAINS_ALL", "NOT_CONTAINS_ALL", "IS_EMPTY", "IS_NOT_EMPTY"],
    "NUM_INT": ["EQ", "NE", "GE", "LE", "BETWEEN", "IS_EMPTY", "IS_NOT_EMPTY"],
    "NUM_DOUBLE": ["EQ", "NE", "GE", "LE", "BETWEEN", "IS_EMPTY", "IS_NOT_EMPTY"],
    "AMOUNT": ["EQ", "NE", "GE", "LE", "BETWEEN", "IS_EMPTY", "IS_NOT_EMPTY"],
    "STOCK": ["EQ", "NE", "GE", "LE", "BETWEEN", "IS_EMPTY", "IS_NOT_EMPTY"],
    "DATE": ["EQ", "GE", "LE", "BETWEEN", "IS_EMPTY", "IS_NOT_EMPTY"],
    "TIME": ["GE", "LE", "BETWEEN", "IS_EMPTY", "IS_NOT_EMPTY"],
}


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
            expected_protocol = FILTER_PROTOCOL_TYPES.get(field.get("fieldType"))
            if field.get("filterFieldType") != expected_protocol:
                errors.append(f"{prefix}.filterFieldType 必须为 {expected_protocol}")
            expected_symbols = FILTER_SUPPORTED_SYMBOLS.get(field.get("fieldType"))
            if field.get("supportedSymbols") != expected_symbols:
                errors.append(f"{prefix}.supportedSymbols 必须为 {expected_symbols}")
        if field.get("fieldType") in NON_FILTERABLE_FIELD_TYPES and field.get("filterName") is not None:
            errors.append(f"{prefix}.fieldType 不支持筛选，filterName 必须为 null")
        expected_business_code = FIXED_BUSINESS_CODES.get(field.get("fieldType"))
        if expected_business_code and field.get("businessCode") != expected_business_code:
            errors.append(f"{prefix}.businessCode 必须为 {expected_business_code}")
        if field.get("fieldType") in EXPLICIT_BUSINESS_CODE_FIELD_TYPES:
            business_select_code = field.get("businessCode")
            if not isinstance(business_select_code, str) or not re.fullmatch(r"[A-Z][A-Z0-9_]*", business_select_code):
                errors.append(f"{prefix}.businessCode 必须是显式的大写枚举值")
        if field.get("fieldType") == "SUB_ITEM":
            sub_fields = field.get("subFields")
            if not isinstance(sub_fields, list):
                errors.append(f"缺少明确输入：{prefix}.subFields（可显式为 []）")
            else:
                for index, sub_field in enumerate(sub_fields):
                    validate_field(sub_field, f"{prefix}.subFields[{index}]", child=True)
        if "selectionFill" in field:
            if field.get("fieldType") != "BUSINESS":
                errors.append(f"{prefix}.selectionFill 只能用于 BUSINESS 字段")
            elif not isinstance(field.get("selectionFill"), bool):
                errors.append(f"{prefix}.selectionFill 必须为布尔值")

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

    form_sections = metadata.get("formSections")
    if form_sections is not None:
        if not isinstance(form_sections, list):
            errors.append("formSections 必须是数组")
        else:
            form_attrs = {
                field.get("attr") for field in fields if isinstance(field, dict)
                and isinstance(field.get("attr"), str)
                and any(scene in {"CREATE", "UPDATE"} for scene in field.get("scenes", []))
            }
            keys: list[str] = []
            orders: list[int] = []
            assigned_attrs: list[str] = []
            for index, section in enumerate(form_sections):
                prefix = f"formSections[{index}]"
                if not isinstance(section, dict):
                    errors.append(f"{prefix} 必须是对象")
                    continue
                for key in ("key", "title"):
                    require_string(section.get(key), f"{prefix}.{key}", errors)
                if not isinstance(section.get("order"), int):
                    errors.append(f"{prefix}.order 必须是整数")
                if "columns" in section and (not isinstance(section["columns"], int) or section["columns"] <= 0):
                    errors.append(f"{prefix}.columns 必须是正整数")
                if "collapsed" in section and not isinstance(section["collapsed"], bool):
                    errors.append(f"{prefix}.collapsed 必须是布尔值")
                if not isinstance(section.get("fields"), list) or not section["fields"]:
                    errors.append(f"{prefix}.fields 必须是非空数组")
                    continue
                invalid_attrs = [attr for attr in section["fields"] if not isinstance(attr, str) or attr not in form_attrs]
                if invalid_attrs:
                    errors.append(f"{prefix}.fields 只能引用 CREATE/UPDATE 字段 attr：{'、'.join(map(str, invalid_attrs))}")
                keys.append(section.get("key"))
                orders.append(section.get("order"))
                assigned_attrs.extend(section["fields"])
            if len(keys) != len(set(keys)):
                errors.append("formSections.key 不能重复")
            if len(orders) != len(set(orders)):
                errors.append("formSections.order 不能重复")
            if len(assigned_attrs) != len(set(assigned_attrs)):
                errors.append("formSections.fields 不能重复引用同一字段")

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
