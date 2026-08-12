#!/usr/bin/env python3
"""由字段元数据生成共享 FieldEnum 消费的 ROOT 列表元数据 Provider。"""

import argparse
import json
from pathlib import Path
from typing import Any

from validate_field_metadata import load_metadata, validate


def property_value(spec_path: Path, name: str) -> str:
    for line in spec_path.read_text(encoding="utf-8").splitlines():
        normalized_line = line.lstrip()
        if normalized_line.startswith(f"{name}:"):
            return normalized_line.split(":", 1)[1].strip()
    raise ValueError(f"{spec_path} 缺少 {name}")


def java_string(value: str) -> str:
    return json.dumps(value, ensure_ascii=False)


def render(metadata: dict[str, Any], package_base: str, aggregate_name: str) -> str:
    actions = metadata["listActions"]
    business_code = metadata["businessCode"]
    action_lines: list[str] = []
    for method, property_name, action_type, group in (
        ("buildTopButtonMeta", "TopButtonList", "button", "top"),
        ("buildBottomButtonMeta", "BottomButtonList", "button", "bottom"),
        ("buildRowActionMeta", "RowActionList", "row", "row"),
    ):
        action_lines.extend(("    @Override", f"    public ListMetaBundlePojo {method}(ListCommonQueryDTO dto) {{", "        ListMetaBundlePojo bundle = new ListMetaBundlePojo();"))
        if action_type == "button":
            values = ", ".join(f"new ListButtonItemPojo({java_string(action['actionCode'])}, {java_string(action['actionName'])}, {(index + 1) * 10}, {java_string(action['actionCode'])})" for index, action in enumerate(actions[group]))
            action_lines.append(f"        bundle.set{property_name}(List.of({values}));")
        else:
            action_lines.extend(("        List<ListRowActionItemPojo> actions = new ArrayList<>();",))
            for index, action in enumerate(actions[group]):
                action_lines.extend(("        ListRowActionItemPojo action = new ListRowActionItemPojo();", f"        action.setActionCode({java_string(action['actionCode'])});", f"        action.setActionName({java_string(action['actionName'])});", f"        action.setSort({(index + 1) * 10});", "        action.setShowMode(\"PRIMARY\");", "        action.setConfirmType(\"NONE\");", "        actions.add(action);"))
            action_lines.append("        bundle.setRowActionList(actions);")
        action_lines.extend(("        return bundle;", "    }", ""))
    return f'''package {package_base}.application.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldItem;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.base.common.pojo.FilterField;
import xbb.ai.erp.base.common.pojo.ListButtonItemPojo;
import xbb.ai.erp.base.common.pojo.ListRowActionItemPojo;
import {package_base}.admin.{aggregate_name}FieldEnum;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.application.filter.ListFilterFieldTypeRule;
import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;
import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;
import xbb.ai.erp.module.common.application.provider.ListMetaProvider;
import xbb.ai.erp.scene.meta.SceneFieldAssembler;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Component
public class {aggregate_name}ListMetaProvider implements ListMetaProvider {{

    @Override
    public String businessCode() {{
        return BusinessCodeEnum.{business_code}.getCode();
    }}

    @Override
    public List<FilterField> buildFilterMeta(ListCommonQueryDTO dto) {{
        return Arrays.stream({aggregate_name}FieldEnum.values())
            .filter(field -> field.getFilterName() != null)
            .map(this::buildFilterField)
            .toList();
    }}

    @Override
    public Map<String, ListFilterMetaPojo> buildFilterConditionMeta(ListCommonQueryDTO dto) {{
        Map<String, ListFilterMetaPojo> metadata = new LinkedHashMap<>();
        Arrays.stream({aggregate_name}FieldEnum.values())
            .filter(field -> field.getFilterName() != null)
            .forEach(field -> metadata.put(field.getAttr(), new ListFilterMetaPojo(
                field.getAttr(), field.getFilterName(), filterRule(field).protocolFieldType(), filterRule(field).supportedSymbols())));
        return Map.copyOf(metadata);
    }}

    @Override
    public List<FieldEntity> buildHeaderMeta(ListCommonQueryDTO dto) {{
        return Arrays.stream({aggregate_name}FieldEnum.values())
            .filter(field -> field.supports(SceneTypeEnum.LIST))
            .map(field -> SceneFieldAssembler.build(field.toSceneFieldMeta()))
            .toList();
    }}

{chr(10).join(action_lines)}    private FilterField buildFilterField({aggregate_name}FieldEnum field) {{
        FilterField result = new FilterField();
        result.setAttr(field.getAttr());
        result.setAttrName(field.getAttrName());
        result.setSourceFieldType(field.getFieldType().getType());
        result.setFieldType(String.valueOf(field.getFieldType().getType()));
        result.setFilterFieldType(filterRule(field).protocolFieldType());
        result.setSupportedSymbols(filterRule(field).supportedSymbols());
        result.setItemList(field.itemList());
        result.setBusinessSelectConfig(field.businessSelectConfig());
        return result;
    }}

    private static ListFilterFieldTypeRule filterRule({aggregate_name}FieldEnum field) {{
        return ListFilterFieldTypeRule.find(field.getFieldType().getType())
            .orElseThrow(() -> new IllegalArgumentException("不支持的筛选字段类型: " + field.getFieldType()));
    }}
}}
'''


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("field_metadata", type=Path)
    parser.add_argument("spec", type=Path)
    parser.add_argument("module_root", type=Path)
    parser.add_argument("--apply", action="store_true")
    args = parser.parse_args()
    metadata = load_metadata(args.field_metadata)
    errors = validate(metadata)
    if errors:
        raise ValueError("字段元数据校验失败：\n- " + "\n- ".join(errors))
    package_base = property_value(args.spec, "packageBase")
    aggregate_name = property_value(args.spec, "aggregateName")
    target = args.module_root / "src/main/java" / Path(*package_base.split(".")) / "application/provider" / f"{aggregate_name}ListMetaProvider.java"
    if not args.apply:
        print(f"将生成列表元数据 Provider：{target}")
        return
    target.parent.mkdir(parents=True, exist_ok=True)
    target.write_text(render(metadata, package_base, aggregate_name), encoding="utf-8")
    print(f"列表元数据 Provider 已生成：{target}")


if __name__ == "__main__":
    main()
