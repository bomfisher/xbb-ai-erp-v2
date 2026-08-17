#!/usr/bin/env python3
"""由完整字段元数据生成表单字段、列表 Schema 与标准查询契约。"""

import argparse
import json
import re
from pathlib import Path
from typing import Any, Optional

from generate_list_meta_provider import property_value
from validate_field_metadata import load_metadata, validate


def java_string(value: str) -> str:
    return json.dumps(value, ensure_ascii=False)


def class_variable(aggregate: str) -> str:
    return aggregate[:1].lower() + aggregate[1:]


def scene_literals(scenes: list[str]) -> str:
    return ", ".join(f"SceneTypeEnum.{scene}" for scene in scenes)


def options_literal(options: Optional[str]) -> str:
    return "null" if not options else java_string(options)


def enum_constant(field: dict[str, Any]) -> str:
    normalized = re.sub(r"(?<!^)(?=[A-Z])", "_", field["name"]).replace("-", "_")
    return normalized.upper()


def sub_fields_literal(field: dict[str, Any]) -> str:
    return "List.of(" + ", ".join(
        f"new SceneFieldMeta({java_string(sub['attr'])}, {java_string(sub['attrName'])}, FieldTypeEnum.{sub['fieldType']}.getType(), {1 if sub['required'] else 0}, {1 if sub['editable'] else 0}, List.of(), {java_string(sub['businessCode']) if sub.get('businessCode') else 'null'}, List.of())"
        for sub in field.get("subFields", [])
    ) + ")"


def render_field_enum(metadata: dict[str, Any], package_base: str, aggregate: str) -> str:
    entries = []
    for field in metadata["fields"]:
        entries.append(
            f"    {enum_constant(field)}({java_string(field['attr'])}, {java_string(field['attrName'])}, FieldTypeEnum.{field['fieldType']}, {java_string(field['filterName']) if field['filterName'] else 'null'}, "
            f"{str(field['required']).lower()}, {str(field['editable']).lower()}, List.of({scene_literals(field['scenes'])}), "
            f"{options_literal(field.get('options'))}, {java_string(field['businessCode']) if field.get('businessCode') else 'null'}, {sub_fields_literal(field)})"
        )
    entries_text = ",\n".join(entries)
    return f'''package {package_base}.admin;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import xbb.ai.erp.base.common.filed.FieldItem;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Getter
public enum {aggregate}FieldEnum {{
{entries_text};

    private final String attr;
    private final String attrName;
    private final FieldTypeEnum fieldType;
    private final String filterName;
    private final boolean required;
    private final boolean editable;
    private final List<SceneTypeEnum> scenes;
    private final String options;
    private final String businessCode;
    private final List<SceneFieldMeta> subFields;

    {aggregate}FieldEnum(String attr, String attrName, FieldTypeEnum fieldType, String filterName, boolean required, boolean editable, List<SceneTypeEnum> scenes, String options, String businessCode, List<SceneFieldMeta> subFields) {{
        this.attr = attr; this.attrName = attrName; this.fieldType = fieldType; this.filterName = filterName;
        this.required = required; this.editable = editable; this.scenes = scenes; this.options = options;
        this.businessCode = businessCode; this.subFields = subFields;
    }}

    public boolean supports(SceneTypeEnum scene) {{
        return scenes.contains(scene) && (fieldType != FieldTypeEnum.SUB_ITEM || scene != SceneTypeEnum.LIST);
    }}

    public SceneFieldMeta toSceneFieldMeta() {{
        return new SceneFieldMeta(attr, attrName, fieldType.getType(), required ? 1 : 0, editable ? 1 : 0, itemList(), businessCode, subFields);
    }}

    public List<FieldItem> itemList() {{
        if (options == null || options.isBlank()) return List.of();
        return Arrays.stream(options.split(",")).map(String::trim).filter(option -> !option.isEmpty()).map(option -> {{
            String[] parts = option.split(":", 2); FieldItem item = new FieldItem(); item.setValue(parts[0].trim());
            item.setText(parts.length == 2 ? parts[1].trim() : parts[0].trim()); return item;
        }}).toList();
    }}

    public FieldEntity.BusinessSelectConfig businessSelectConfig() {{
        if (businessCode == null || businessCode.isBlank()) return null;
        FieldEntity.BusinessSelectConfig config = new FieldEntity.BusinessSelectConfig();
        config.setBusinessCode(businessCode);
        return config;
    }}
}}
'''


def render_field_factory(package_base: str, aggregate: str) -> str:
    return f'''package {package_base}.application.field;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import {package_base}.admin.{aggregate}FieldEnum;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Component
public class {aggregate}FieldFactory {{
    public List<SceneFieldMeta> getFields(SceneTypeEnum scene) {{
        return Arrays.stream({aggregate}FieldEnum.values())
            .filter(field -> field.supports(scene))
            .map({aggregate}FieldEnum::toSceneFieldMeta)
            .toList();
    }}
}}
'''


def render_form_section_factory(metadata: dict[str, Any], package_base: str, aggregate: str) -> str:
    sections = metadata.get("formSections", [])
    section_literals = ",\n            ".join(
        "section("
        f"{java_string(section['key'])}, {java_string(section['title'])}, {section['order']}, "
        f"{section.get('columns', 2)}, {str(section.get('collapsed', False)).lower()}, "
        f"List.of({', '.join(java_string(attr) for attr in section['fields'])})"
        ")"
        for section in sections
    )
    return f'''package {package_base}.application.field;

import java.util.List;
import xbb.ai.erp.base.common.filed.FormSectionEntity;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

public final class {aggregate}FormSectionFactory {{
    private {aggregate}FormSectionFactory() {{
    }}

    public static List<FormSectionEntity> getSections(SceneTypeEnum scene) {{
        if (scene != SceneTypeEnum.CREATE && scene != SceneTypeEnum.UPDATE) {{
            return List.of();
        }}
        return List.of(
            {section_literals}
        );
    }}

    private static FormSectionEntity section(String key, String title, int order, int columns, boolean collapsed, List<String> fields) {{
        FormSectionEntity section = new FormSectionEntity();
        section.setKey(key);
        section.setTitle(title);
        section.setOrder(order);
        section.setColumns(columns);
        section.setCollapsed(collapsed);
        section.setFields(fields);
        return section;
    }}
}}
'''


def render_schema(package_base: str, aggregate: str) -> str:
    return f'''package {package_base}.application.schema;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;
import {package_base}.application.provider.{aggregate}ListMetaProvider;

@Component
@RequiredArgsConstructor
public class {aggregate}ListSchemaProvider {{
    private final {aggregate}ListMetaProvider listMetaProvider;
    public Map<String, ListFilterMetaPojo> conditionMetaMap() {{ return listMetaProvider.buildFilterConditionMeta(new ListCommonQueryDTO()); }}
}}
'''


def render_query(package_base: str, aggregate: str, business_code: str, has_form_sections: bool) -> str:
    variable = class_variable(aggregate)
    form_section_import = f"import {package_base}.application.field.{aggregate}FormSectionFactory;\n" if has_form_sections else ""
    create_form_sections = f"        vo.setFormSections({aggregate}FormSectionFactory.getSections(SceneTypeEnum.CREATE));\n" if has_form_sections else ""
    update_form_sections = f"        vo.setFormSections({aggregate}FormSectionFactory.getSections(SceneTypeEnum.UPDATE));\n" if has_form_sections else ""
    return f'''package {package_base}.application.service.query;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.common.application.util.ListQueryMapUtil;
import xbb.ai.erp.module.common.application.render.ListValueRenderer;
import xbb.ai.erp.scene.meta.SceneFieldAssembler;
import xbb.ai.erp.scene.meta.SceneTypeEnum;
import {package_base}.admin.vo.{aggregate}DetailVO;
import {package_base}.admin.vo.{aggregate}ListItemVO;
import {package_base}.application.assembler.{aggregate}AdminAssembler;
import {package_base}.application.field.{aggregate}FieldFactory;
{form_section_import}import {package_base}.application.schema.{aggregate}ListSchemaProvider;
import {package_base}.domain.model.{aggregate};
import {package_base}.domain.repository.{aggregate}Repository;

@Service
public class {aggregate}QueryAppServiceImpl {{
    private final {aggregate}Repository {variable}Repository;
    private final {aggregate}FieldFactory fieldFactory;
    private final {aggregate}ListSchemaProvider schemaProvider;
    private final ListValueRenderer listValueRenderer;
    private final ListQueryMapUtil listQueryMapUtil = new ListQueryMapUtil();

    public {aggregate}QueryAppServiceImpl({aggregate}Repository {variable}Repository, {aggregate}FieldFactory fieldFactory, {aggregate}ListSchemaProvider schemaProvider, ListValueRenderer listValueRenderer) {{
        this.{variable}Repository = {variable}Repository; this.fieldFactory = fieldFactory; this.schemaProvider = schemaProvider; this.listValueRenderer = listValueRenderer;
    }}

    public ListBaseVO<{aggregate}ListItemVO> list(ListBaseDTO dto) {{
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = listQueryMapUtil.gen(dto, schemaProvider.conditionMetaMap());
        List<{aggregate}> list = {variable}Repository.findByCondition(conditionMap);
        Long total = {variable}Repository.count(conditionMap);
        ListBaseVO<{aggregate}ListItemVO> vo = new ListBaseVO<>();
        List<{aggregate}ListItemVO> items = list.stream().map({aggregate}AdminAssembler::toListItemVO).toList();
        vo.setList(listValueRenderer.render(dto.getCorpid(), BusinessCodeEnum.{business_code}.getCode(), items));
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }}

    public SaveItemVO<{package_base}.admin.vo.{aggregate}SaveItemVO> addItem(BaseDTO dto) {{
        SaveItemVO<{package_base}.admin.vo.{aggregate}SaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(SceneFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.CREATE)));
{create_form_sections}        vo.setData({aggregate}AdminAssembler.buildEmptySaveItemVO());
        vo.setData({aggregate}AdminAssembler.buildEmptySaveItemVO());
        return vo;
    }}

    public SaveItemVO<{package_base}.admin.vo.{aggregate}SaveItemVO> updateItem(IdBaseDTO dto) {{
        AdminParamValidator.validateIdQuery(dto);
        {aggregate} entity = {variable}Repository.findById(dto.getCorpid(), dto.getId());
        SaveItemVO<{package_base}.admin.vo.{aggregate}SaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(SceneFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.UPDATE)));
{update_form_sections}        vo.setData({aggregate}AdminAssembler.toSaveItemVO(entity));
        vo.setData({aggregate}AdminAssembler.toSaveItemVO(entity));
        return vo;
    }}

    public {aggregate}DetailVO detail(IdBaseDTO dto) {{
        AdminParamValidator.validateIdQuery(dto);
        {aggregate} entity = {variable}Repository.findById(dto.getCorpid(), dto.getId());
        return {aggregate}AdminAssembler.toDetailVO({aggregate}AdminAssembler.toSaveItemVO(entity));
    }}
}}
'''


def upper_camel(value: str) -> str:
    return value[:1].upper() + value[1:]


STRING_LIST_FIELD_TYPES = {"DATE", "TIME", "COMB", "COMB_MULTI", "CHECKBOX", "CHECK_BOX", "RADIO_BTN"}
NUMERIC_JAVA_TYPES = {
    "byte", "short", "int", "long", "float", "double",
    "Byte", "Short", "Integer", "Long", "Float", "Double",
    "java.math.BigInteger", "java.math.BigDecimal",
}


def rewrite_list_string_contract(source_root: Path, aggregate: str, metadata: dict[str, Any]) -> None:
    list_fields = [
        field for field in metadata["fields"]
        if field["fieldType"] in STRING_LIST_FIELD_TYPES and "LIST" in field["scenes"]
    ]
    list_item_vo = source_root / "admin" / "vo" / f"{aggregate}ListItemVO.java"
    assembler = source_root / "application" / "assembler" / f"{aggregate}AdminAssembler.java"
    if not list_item_vo.is_file() or not assembler.is_file():
        if not list_fields:
            return
        missing = [str(path) for path in (list_item_vo, assembler) if not path.is_file()]
        raise ValueError("缺少列表转字符串所需骨架：" + "、".join(missing))

    list_item_content = list_item_vo.read_text(encoding="utf-8")
    assembler_content = assembler.read_text(encoding="utf-8")
    variable = class_variable(aggregate)
    list_fields_by_name = {field["name"]: field for field in list_fields}
    for declaration in re.finditer(r"private\s+(?P<type>[\w.]+)\s+(?P<name>\w+);", list_item_content):
        if declaration.group("type") in NUMERIC_JAVA_TYPES:
            list_fields_by_name.setdefault(declaration.group("name"), {"name": declaration.group("name")})

    for field in list_fields_by_name.values():
        name = field["name"]
        setter = upper_camel(name)
        declaration_pattern = rf"(private\s+)[^;\n]+(\s+{re.escape(name)};)"
        list_item_content, declarations = re.subn(
            declaration_pattern,
            r"\1String\2",
            list_item_content,
            count=1,
        )
        if declarations == 0:
            raise ValueError(f"未找到列表 VO 字段：{list_item_vo}#{name}")
        assignment = f"vo.set{setter}({variable}.get{setter}());"
        if field.get("fieldType") == "DATE":
            replacement = (
                f"vo.set{setter}(Objects.isNull({variable}.get{setter}()) ? \"\" : "
                f"java.time.Instant.ofEpochMilli({variable}.get{setter}()).atZone(java.time.ZoneId.systemDefault()).toLocalDate().toString());"
            )
        elif field.get("fieldType") == "TIME":
            replacement = (
                f"vo.set{setter}(Objects.isNull({variable}.get{setter}()) ? \"\" : "
                f"java.time.Instant.ofEpochMilli({variable}.get{setter}()).atZone(java.time.ZoneId.systemDefault()).toLocalDateTime().toString());"
            )
        else:
            replacement = (
                f"vo.set{setter}(Objects.isNull({variable}.get{setter}()) ? \"\" : "
                f"Objects.toString({variable}.get{setter}()));"
            )
        if assignment in assembler_content:
            assembler_content = assembler_content.replace(assignment, replacement, 1)
        elif f"vo.set{setter}(Objects.isNull({variable}.get{setter}())" not in assembler_content:
            raise ValueError(f"未找到列表装配赋值：{assembler}#{name}")

    list_item_vo.write_text(list_item_content, encoding="utf-8")
    assembler.write_text(assembler_content, encoding="utf-8")


def rewrite_list_contract(path: Path, package_base: str, aggregate: str) -> None:
    content = path.read_text(encoding="utf-8")
    if "import xbb.ai.erp.base.common.dto.ListBaseDTO;" not in content:
        id_base_import = "import xbb.ai.erp.base.common.dto.IdBaseDTO;"
        if id_base_import in content:
            content = content.replace(id_base_import, f"{id_base_import}\nimport xbb.ai.erp.base.common.dto.ListBaseDTO;", 1)
        else:
            package_declaration = re.search(r"^package [^;]+;\n", content, re.MULTILINE)
            if package_declaration:
                content = (
                    content[:package_declaration.end()]
                    + "\nimport xbb.ai.erp.base.common.dto.ListBaseDTO;\n"
                    + content[package_declaration.end():]
                )
            else:
                content = "import xbb.ai.erp.base.common.dto.ListBaseDTO;\n" + content

    list_parameter = re.compile(
        r"(?P<prefix>\blist\s*\(\s*(?:@[\w.]+(?:\([^)]*\))?\s*)*)"
        + re.escape(aggregate)
        + r"ListDTO\s+(?P<name>\w+)"
    )
    content, replacements = list_parameter.subn(
        lambda match: f"{match.group('prefix')}ListBaseDTO {match.group('name')}", content
    )
    if replacements == 0 and not re.search(r"\blist\s*\(\s*(?:@[\w.]+(?:\([^)]*\))?\s*)*ListBaseDTO\s+\w+", content):
        raise ValueError(f"未找到 {aggregate}ListDTO 列表签名：{path}")

    content = re.sub(
        rf"^import\s+{re.escape(package_base)}\.admin\.dto\.{re.escape(aggregate)}ListDTO;\n",
        "",
        content,
        flags=re.MULTILINE,
    )
    path.write_text(content, encoding="utf-8")


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
    aggregate = property_value(args.spec, "aggregateName")
    business_code = property_value(args.spec, "businessCode")
    source_root = args.module_root / "src/main/java" / Path(*package_base.split("."))
    has_form_sections = bool(metadata.get("formSections"))
    targets = (source_root / "admin" / f"{aggregate}FieldEnum.java", source_root / "application/field" / f"{aggregate}FieldFactory.java", source_root / "application/schema" / f"{aggregate}ListSchemaProvider.java", source_root / "application/service/query" / f"{aggregate}QueryAppServiceImpl.java")
    if has_form_sections:
        targets += (source_root / "application/field" / f"{aggregate}FormSectionFactory.java",)
    if not args.apply:
        print("将生成标准表单/查询契约：" + "、".join(str(target) for target in targets))
        return
    for target in targets:
        target.parent.mkdir(parents=True, exist_ok=True)
    targets[0].write_text(render_field_enum(metadata, package_base, aggregate), encoding="utf-8")
    targets[1].write_text(render_field_factory(package_base, aggregate), encoding="utf-8")
    targets[2].write_text(render_schema(package_base, aggregate), encoding="utf-8")
    targets[3].write_text(render_query(package_base, aggregate, business_code, has_form_sections), encoding="utf-8")
    if has_form_sections:
        targets[4].write_text(render_form_section_factory(metadata, package_base, aggregate), encoding="utf-8")
    rewrite_list_string_contract(source_root, aggregate, metadata)
    controller = source_root / "admin" / f"{aggregate}AdminController.java"
    service = source_root / "application/service" / f"{aggregate}AdminAppService.java"
    service_impl = source_root / "application/service/impl" / f"{aggregate}AdminAppServiceImpl.java"
    for path in (controller, service, service_impl):
        rewrite_list_contract(path, package_base, aggregate)
    print(f"标准表单/查询契约已生成：{aggregate}")


if __name__ == "__main__":
    main()
