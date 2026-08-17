#!/usr/bin/env python3
"""校验业务模块七接口和 DDD 目录职责。"""

import argparse
import re
from pathlib import Path

from validate_field_metadata import load_metadata, validate as validate_field_metadata


ROOT_DIRECTORIES = (
    "admin/dto", "admin/vo", "application/assembler", "application/field", "application/port",
    "application/provider", "application/schema", "application/service/impl", "application/service/query",
    "application/service/save", "application/service/draft", "application/validator", "domain/model",
    "domain/repository", "infrastructure/persistence/convertor", "infrastructure/persistence/mapper",
    "infrastructure/persistence/po", "infrastructure/persistence/repository",
)
CHILD_FORBIDDEN_FILE_SUFFIXES = ("AdminController.java", "AdminAppService.java")
ENDPOINTS = ("list", "addItem", "updateItem", "saveDraft", "saveAndSubmit", "draftList", "loadDraft")


def require_directories(source_root: Path, directories: tuple[str, ...]) -> list[str]:
    return [directory for directory in directories if not (source_root / directory).is_dir()]


def java_files(path: Path) -> list[Path]:
    return list(path.glob("**/*.java")) if path.is_dir() else []


def require_endpoint_methods(controller: Path) -> list[str]:
    content = controller.read_text(encoding="utf-8")
    missing = []
    for endpoint in ENDPOINTS:
        pattern = rf'@PostMapping\("/{re.escape(endpoint)}"\)\s+public\s+[^\n]+\s+{endpoint}\s*\('
        if not re.search(pattern, content):
            missing.append(endpoint)
    return missing


def require_service_methods(service: Path) -> list[str]:
    content = service.read_text(encoding="utf-8")
    return [endpoint for endpoint in ENDPOINTS if not re.search(rf'\b{endpoint}\s*\(', content)]


def validate_field_delivery(module_root: Path, source_root: Path, aggregate: str, metadata: dict) -> list[str]:
    errors = []
    field_enum_content = ""
    field_enum = source_root / "admin" / f"{aggregate}FieldEnum.java"
    if not field_enum.is_file():
        errors.append(f"缺少共享字段事实源：{field_enum}")
    else:
        field_enum_content = field_enum.read_text(encoding="utf-8")
        for field in metadata["fields"]:
            required_fragments = (field["attr"], field["attrName"], f"FieldTypeEnum.{field['fieldType']}")
            missing = [fragment for fragment in required_fragments if fragment not in field_enum_content]
            if missing:
                errors.append(f"字段枚举未覆盖 {field['name']} 的明确元数据：{'、'.join(missing)}")

    query_services = java_files(source_root / "application/service/query")
    query_content = "\n".join(path.read_text(encoding="utf-8") for path in query_services)
    for scene in ("CREATE", "UPDATE"):
        if f"SceneTypeEnum.{scene}" not in query_content or "setHeadList" not in query_content:
            errors.append(f"addItem/updateItem 未使用 {scene} 场景生成 headList")

    business_enum = module_root.parent / "xbb-erp-base-common/src/main/java/xbb/ai/erp/base/common/module/BusinessCodeEnum.java"
    if not business_enum.is_file() or f'("{metadata["businessCode"]}")' not in business_enum.read_text(encoding="utf-8"):
        errors.append(f"BusinessCodeEnum 未登记明确业务编码：{metadata['businessCode']}")

    provider = source_root / "application/provider" / f"{aggregate}ListMetaProvider.java"
    if not provider.is_file():
        errors.append(f"缺少列表元数据 Provider：{provider}")
    else:
        provider_content = provider.read_text(encoding="utf-8")
        shared_enum_fragments = (
            f"{aggregate}FieldEnum",
            f"Arrays.stream({aggregate}FieldEnum.values())",
        )
        if any(fragment and fragment not in provider_content for fragment in shared_enum_fragments):
            errors.append("ListMetaProvider 必须从共享 FieldEnum 派生字段元数据，禁止直接硬编码字段")
        hard_coded_field_fragments = ('setAttr("', 'setAttrName("', 'FieldTypeEnum.')
        if any(fragment in provider_content for fragment in hard_coded_field_fragments):
            errors.append("ListMetaProvider 不得硬编码字段定义，应仅从共享 FieldEnum 投影字段元数据")
        empty_metadata_methods = (
            r"buildFilterMeta\s*\([^)]*\)\s*\{\s*return\s+(?:java\.util\.)?Collections\.emptyList\(\);",
            r"buildFilterConditionMeta\s*\([^)]*\)\s*\{\s*return\s+(?:java\.util\.)?Collections\.emptyMap\(\);",
            r"buildHeaderMeta\s*\([^)]*\)\s*\{\s*return\s+(?:java\.util\.)?Collections\.emptyList\(\);",
            r"buildFilterMeta\s*\([^)]*\)\s*\{\s*return\s+List\.of\(\);",
            r"buildFilterConditionMeta\s*\([^)]*\)\s*\{\s*return\s+Map\.of\(\);",
            r"buildHeaderMeta\s*\([^)]*\)\s*\{\s*return\s+List\.of\(\);",
        )
        if any(re.search(pattern, provider_content, re.DOTALL) for pattern in empty_metadata_methods):
            errors.append("ListMetaProvider 仍为空骨架，未生成明确字段元数据")
        for field in metadata["fields"]:
            required_fragments = [field["attr"], field["attrName"], f"FieldTypeEnum.{field['fieldType']}"]
            missing = [fragment for fragment in required_fragments if fragment not in field_enum_content]
            if missing:
                errors.append(f"共享 FieldEnum 未覆盖 {field['name']}：{'、'.join(missing)}")
        for action_group in metadata["listActions"].values():
            for action in action_group:
                if action["actionCode"] not in provider_content or action["actionName"] not in provider_content:
                    errors.append(f"ListMetaProvider 未覆盖列表动作：{action['actionCode']}")
    field_factory = source_root / "application/field" / f"{aggregate}FieldFactory.java"
    if not field_factory.is_file():
        errors.append(f"缺少字段工厂：{field_factory}")
    else:
        factory_content = field_factory.read_text(encoding="utf-8")
        if f"Arrays.stream({aggregate}FieldEnum.values())" not in factory_content or "private enum Field" in factory_content:
            errors.append("FieldFactory 必须只遍历共享 FieldEnum，禁止维护第二份字段枚举")
    return errors


def validate_list_contract(source_root: Path, aggregate: str) -> list[str]:
    errors = []
    controller = source_root / "admin" / f"{aggregate}AdminController.java"
    if controller.is_file() and not re.search(r"list\s*\(\s*@RequestBody\s+ListBaseDTO\s+\w+\s*\)", controller.read_text(encoding="utf-8")):
        errors.append("Controller 的 list 必须直接接收 ListBaseDTO")

    app_service = source_root / "application/service" / f"{aggregate}AdminAppService.java"
    if app_service.is_file() and not re.search(r"\blist\s*\(\s*ListBaseDTO\s+\w+\s*\)", app_service.read_text(encoding="utf-8")):
        errors.append("Application Service 的 list 必须直接接收 ListBaseDTO")

    query_services = java_files(source_root / "application/service/query")
    query_content = "\n".join(path.read_text(encoding="utf-8") for path in query_services)
    required_fragments = ("ListBaseDTO", "ListQueryMapUtil", "listQueryMapUtil.gen(", "conditionMetaMap()")
    missing = [fragment for fragment in required_fragments if fragment not in query_content]
    if missing:
        errors.append("Query AppService 未使用公共列表条件映射：" + "、".join(missing))
    return errors


STRING_LIST_FIELD_TYPES = {"DATE", "TIME", "COMB", "COMB_MULTI", "CHECKBOX", "CHECK_BOX", "RADIO_BTN"}


def validate_list_string_contract(source_root: Path, aggregate: str, metadata: dict) -> list[str]:
    errors = []
    list_fields = [
        field for field in metadata["fields"]
        if field["fieldType"] in STRING_LIST_FIELD_TYPES and "LIST" in field["scenes"]
    ]
    if not list_fields:
        return errors
    list_item_vo = source_root / "admin" / "vo" / f"{aggregate}ListItemVO.java"
    assembler = source_root / "application" / "assembler" / f"{aggregate}AdminAssembler.java"
    if not list_item_vo.is_file() or not assembler.is_file():
        return ["列表转字符串字段缺少 ListItemVO 或 AdminAssembler"]
    list_item_content = list_item_vo.read_text(encoding="utf-8")
    assembler_content = assembler.read_text(encoding="utf-8")
    variable = aggregate[:1].lower() + aggregate[1:]
    for field in list_fields:
        name = field["name"]
        setter = name[:1].upper() + name[1:]
        if not re.search(rf"private\s+String\s+{re.escape(name)};", list_item_content):
            errors.append(f"列表 {field['fieldType']} 字段 {name} 必须在 ListItemVO 中声明为 String")
        expected_assignment = (
            f"vo.set{setter}(Objects.isNull({variable}.get{setter}()) ? \"\" : "
            f"Objects.toString({variable}.get{setter}()));"
        )
        if expected_assignment not in assembler_content:
            errors.append(f"列表 {field['fieldType']} 字段 {name} 必须在 toListItemVO 中转换为 String")
    return errors


def validate_auto_increment_insert_contract(module_root: Path, source_root: Path, aggregate: str) -> list[str]:
    errors = []
    repository = source_root / "infrastructure/persistence/repository" / f"{aggregate}RepositoryImpl.java"
    po = source_root / "infrastructure/persistence/po" / f"{aggregate}PO.java"
    if not repository.is_file() or not po.is_file():
        return errors

    repository_content = repository.read_text(encoding="utf-8")
    po_content = po.read_text(encoding="utf-8")
    mapper_files = list((source_root / "infrastructure/persistence/mapper").glob(f"{aggregate}Mapper.java"))
    mapper_xml_files = list((module_root / "src/main/resources/mapper").glob(f"**/{aggregate}Mapper.xml"))
    if len(mapper_files) == 1:
        mapper_content = mapper_files[0].read_text(encoding="utf-8")
        if "BaseMapper" in mapper_content:
            errors.append("Mapper 禁止继承 MyBatis-Plus BaseMapper，所有持久化语句必须显式声明")
        if not re.search(r"\bint\s+insert\s*\(" + re.escape(aggregate) + r"PO\s+po\s*\)", mapper_content):
            errors.append("Mapper 必须声明自定义单条 insert，禁止 Repository 使用 BaseMapper 默认 insert")
    else:
        errors.append("无法唯一定位 Mapper 以校验自定义单条 insert")
    insert_match = re.search(
        r"public\s+Long\s+insert\s*\([^)]*\)\s*\{(?P<body>.*?)\}",
        repository_content,
        re.DOTALL,
    )
    if not insert_match:
        errors.append("RepositoryImpl 的 insert 必须返回数据库生成的 Long 主键")
        return errors

    insert_body = insert_match.group("body")
    if "initializeForInsert(po);" not in insert_body:
        errors.append("insert 必须调用 initializeForInsert 初始化 BaseEntity 字段")
    if not re.search(r"\w+\.setId\(po\.getId\(\)\);", insert_body):
        errors.append("AUTO_INCREMENT 插入后必须将 PO.id 回写领域对象")
    if "return po.getId();" not in insert_body:
        errors.append("AUTO_INCREMENT insert 必须返回数据库回填的 PO.id")
    batch_match = re.search(
        r"public\s+void\s+insertBatch\s*\([^)]*\)\s*\{(?P<body>.*?)\}",
        repository_content,
        re.DOTALL,
    )
    if not batch_match:
        errors.append("RepositoryImpl 缺少可校验的 insertBatch 方法")
    else:
        batch_body = batch_match.group("body")
        required_batch_fragments = (
            "poList.forEach(this::initializeForInsert);",
            "Mapper.insertBatch(poList);",
            ".setId(poList.get(index).getId());",
        )
        missing = [fragment for fragment in required_batch_fragments if fragment not in batch_body]
        if missing:
            errors.append("insertBatch 必须调用 initializeForInsert 并将回填主键逐项写回领域数组：" + "、".join(missing))
    required_initializer_fragments = (
        "private void initializeForInsert(BaseEntity po)",
        "po.setId(null);",
        "po.setDel(0);",
        "po.setAddTime(now);",
        "po.setUpdateTime(now);",
    )
    missing_initializer = [fragment for fragment in required_initializer_fragments if fragment not in repository_content]
    if missing_initializer:
        errors.append("RepositoryImpl 必须提供完整的 initializeForInsert(BaseEntity po)：" + "、".join(missing_initializer))
    if "IdWorker" in repository_content or "xbb.ai.erp.base.idgen" in repository_content or "Snowflake" in repository_content:
        errors.append("AUTO_INCREMENT 插入不得依赖雪花 ID 生成器")
    if "extends BaseEntity" not in po_content and ("@TableId" not in po_content or "IdType.AUTO" not in po_content):
        errors.append("PO 必须继承带有 @TableId(type = IdType.AUTO) 的 BaseEntity，禁止默认雪花 ID 策略")
    mapper_xml_files = list((module_root / "src/main/resources/mapper").glob(f"**/{aggregate}Mapper.xml"))
    if len(mapper_xml_files) != 1:
        errors.append("无法唯一定位 Mapper XML 以校验 insertBatch 主键回填")
    else:
        mapper_xml = mapper_xml_files[0].read_text(encoding="utf-8")
        single_insert_match = re.search(r"<insert\s+id=\"insert\"(?P<attributes>[^>]*)>(?P<body>.*?)</insert>", mapper_xml, re.DOTALL)
        if not single_insert_match:
            errors.append("Mapper XML 必须生成单条 insert，禁止依赖 MyBatis-Plus BaseMapper 默认实现")
        elif 'useGeneratedKeys="true"' not in single_insert_match.group("attributes") or 'keyProperty="id"' not in single_insert_match.group("attributes"):
            errors.append("单条 insert 必须配置 useGeneratedKeys=\"true\" 和 keyProperty=\"id\"")
        batch_insert_match = re.search(r"<insert\s+id=\"insertBatch\"(?P<attributes>[^>]*)>(?P<body>.*?)</insert>", mapper_xml, re.DOTALL)
        if not batch_insert_match:
            errors.append("Mapper XML 缺少 insertBatch")
        else:
            attributes = batch_insert_match.group("attributes")
            column_match = re.search(r"insert\s+into\s+\w+\s*\((?P<columns>.*?)\)\s*values", batch_insert_match.group("body"), re.DOTALL | re.IGNORECASE)
            if 'useGeneratedKeys="true"' not in attributes or 'keyProperty="id"' not in attributes:
                errors.append("insertBatch 必须配置 useGeneratedKeys=\"true\" 和 keyProperty=\"id\"")
            if column_match and re.search(r"\bid\b", column_match.group("columns")):
                errors.append("AUTO_INCREMENT insertBatch 不得插入 id 列")
    return errors


def validate_save_assembler_audit_contract(source_root: Path, aggregate: str) -> list[str]:
    assembler = source_root / "application/assembler" / f"{aggregate}AdminAssembler.java"
    if not assembler.is_file():
        return []
    content = assembler.read_text(encoding="utf-8")
    required_fragments = (
        "import java.util.Objects;",
        "Objects.isNull(main.getId())",
        ".setCreatorId(dto.getUserId());",
        ".setModifyId(dto.getUserId());",
    )
    missing = [fragment for fragment in required_fragments if fragment not in content]
    if missing:
        return ["AdminAssembler 保存装配必须维护 creatorId/modifyId：" + "、".join(missing)]
    return []


def java_fields(source_file: Path) -> dict[str, str]:
    if not source_file.is_file():
        return {}
    return {
        name: field_type
        for field_type, name in re.findall(
            r"\bprivate\s+([\w.<>, ?]+?)\s+(\w+)(?:\s*=\s*[^;]+)?\s*;",
            source_file.read_text(encoding="utf-8"),
        )
    }


def validate_form_attr_contract(source_root: Path, aggregate: str, metadata: dict) -> list[str]:
    save_dto = source_root / "admin/dto" / f"{aggregate}SaveDTO.java"
    save_item_vo = source_root / "admin/vo" / f"{aggregate}SaveItemVO.java"
    if not save_dto.is_file():
        return []
    save_fields = java_fields(save_dto)
    save_item_fields = java_fields(save_item_vo)
    errors = []
    main_type = save_fields.get("main")
    main_fields = java_fields(source_root / "admin/dto" / f"{main_type}.java") if main_type else {}
    for field in metadata["fields"]:
        attr = field["attr"]
        if attr.startswith("main."):
            path = attr.split(".")
            if len(path) != 2 or path[0] not in save_fields or path[1] not in main_fields:
                errors.append(f"字段 {field['name']} 的 attr={attr} 无法映射到 {aggregate}SaveDTO.main")
            if path[0] not in save_item_fields:
                errors.append(f"字段 {field['name']} 的 attr={attr} 未在 {aggregate}SaveItemVO 中声明主档路径")
            continue
        if "." in attr:
            errors.append(f"字段 {field['name']} 的 attr={attr} 不是可校验的保存 DTO 路径")
            continue
        if attr not in save_fields:
            errors.append(f"字段 {field['name']} 的 attr={attr} 未在 {aggregate}SaveDTO 中声明")
            continue
        if attr not in save_item_fields:
            errors.append(f"字段 {field['name']} 的 attr={attr} 未在 {aggregate}SaveItemVO 中声明")
            continue
        if field["fieldType"] != "SUB_ITEM":
            continue
        list_type = save_fields[attr]
        child_match = re.search(r"List\s*<\s*([\w.]+)\s*>", list_type)
        if not child_match:
            errors.append(f"子表字段 {field['name']} 的 DTO 属性 {attr} 必须是 List<T>")
            continue
        child_type = child_match.group(1).split(".")[-1]
        child_fields = java_fields(source_root / "admin/dto" / f"{child_type}.java")
        for sub_field in field.get("subFields", []):
            if sub_field["attr"] not in child_fields:
                errors.append(
                    f"子表字段 {field['name']} 的 subField attr={sub_field['attr']} 未在 {child_type} 中声明"
                )
    return errors


def validate_root(module_root: Path, aggregate: str, skip_tests: bool, metadata: dict) -> list[str]:
    package_root = module_root / "src/main/java"
    source_roots = list(package_root.glob("**/module/**"))
    matching_roots = [path for path in source_roots if (path / "admin").is_dir()]
    errors = []
    if len(matching_roots) != 1:
        return [f"无法唯一定位模块 Java 根目录：{package_root}"]
    source_root = matching_roots[0]
    missing_directories = require_directories(source_root, ROOT_DIRECTORIES)
    if missing_directories:
        errors.append(f"ROOT 缺少职责目录：{'、'.join(missing_directories)}")
    mapper_root = module_root / "src/main/resources/mapper"
    if not mapper_root.is_dir() or not list(mapper_root.glob("**/*Mapper.xml")):
        errors.append("ROOT 缺少 Mapper XML 资源目录或映射文件")
    controller = source_root / "admin" / f"{aggregate}AdminController.java"
    service = source_root / "application/service" / f"{aggregate}AdminAppService.java"
    if not controller.is_file():
        errors.append(f"缺少主表 Controller：{controller}")
    else:
        missing_endpoints = require_endpoint_methods(controller)
        if missing_endpoints:
            errors.append(f"Controller 缺少七接口：{'、'.join(missing_endpoints)}")
    if not service.is_file():
        errors.append(f"缺少主表 Application Service：{service}")
    else:
        missing_methods = require_service_methods(service)
        if missing_methods:
            errors.append(f"Application Service 缺少七接口：{'、'.join(missing_methods)}")
    if not skip_tests and not (module_root / "src/test/java").is_dir():
        errors.append("缺少 src/test/java，未建立七接口相关测试目录")
    repository_impl = source_root / "infrastructure/persistence/repository" / f"{aggregate}RepositoryImpl.java"
    if not repository_impl.is_file() or "@Repository(\"" not in repository_impl.read_text(encoding="utf-8"):
        errors.append("RepositoryImpl 必须声明模块级显式 Spring Bean 名，避免跨模块同名聚合冲突")
    errors.extend(validate_field_delivery(module_root, source_root, aggregate, metadata))
    errors.extend(validate_list_contract(source_root, aggregate))
    errors.extend(validate_list_string_contract(source_root, aggregate, metadata))
    errors.extend(validate_auto_increment_insert_contract(module_root, source_root, aggregate))
    errors.extend(validate_save_assembler_audit_contract(source_root, aggregate))
    errors.extend(validate_form_attr_contract(source_root, aggregate, metadata))
    return errors


def validate_children(module_root: Path, children: list[str]) -> list[str]:
    package_root = module_root / "src/main/java"
    source_roots = list(package_root.glob("**/module/**"))
    matching_roots = [path for path in source_roots if (path / "domain").is_dir()]
    if len(matching_roots) != 1:
        return [f"无法唯一定位从表模块 Java 根目录：{package_root}"]
    source_root = matching_roots[0]
    errors = []
    for child in children:
        forbidden = [
            path for path in java_files(source_root)
            if path.name.startswith(child) and path.name.endswith(CHILD_FORBIDDEN_FILE_SUFFIXES)
        ]
        if forbidden:
            errors.append(f"CHILD {child} 不得拥有独立 Controller 或总入口 Application Service：{'、'.join(map(str, forbidden))}")
        required_files = (
            source_root / "domain/model" / f"{child}.java",
            source_root / "domain/repository" / f"{child}Repository.java",
            source_root / "infrastructure/persistence/po" / f"{child}PO.java",
            source_root / "infrastructure/persistence/mapper" / f"{child}Mapper.java",
            source_root / "infrastructure/persistence/repository" / f"{child}RepositoryImpl.java",
        )
        missing = [str(path) for path in required_files if not path.is_file()]
        if missing:
            errors.append(f"CHILD {child} 缺少领域或持久化文件：{'、'.join(missing)}")
    return errors


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("module_root", type=Path)
    parser.add_argument("root_aggregate")
    parser.add_argument("--field-metadata", type=Path, required=True)
    parser.add_argument("--child", action="append", default=[])
    parser.add_argument("--skip-tests", action="store_true")
    args = parser.parse_args()

    metadata = load_metadata(args.field_metadata)
    metadata_errors = validate_field_metadata(metadata)
    if metadata_errors:
        raise ValueError("字段元数据校验失败，请向开发者确认后补充：\n- " + "\n- ".join(metadata_errors))
    errors = validate_root(args.module_root, args.root_aggregate, args.skip_tests, metadata)
    errors.extend(validate_children(args.module_root, args.child))
    if errors:
        raise ValueError("业务模块交付校验失败：\n- " + "\n- ".join(errors))
    print(f"业务模块交付校验通过：{args.module_root}")


if __name__ == "__main__":
    main()
