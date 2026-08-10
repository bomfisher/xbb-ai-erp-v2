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
    field_enum = source_root / "admin" / f"{aggregate}FieldEnum.java"
    if not field_enum.is_file():
        errors.append(f"缺少字段枚举：{field_enum}")
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
        provider_content = provider.read_text(encoding="utf-8") + "\n" + field_enum_content
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
            if "LIST" in field["scenes"] or field["filterName"] is not None:
                required_fragments = [field["attr"].split(".")[-1], field["attrName"]]
                if field["filterName"] is not None:
                    required_fragments.append(field["filterName"])
                    required_fragments.append("setFilterFieldType")
                missing = [fragment for fragment in required_fragments if fragment not in provider_content]
                if missing:
                    errors.append(f"ListMetaProvider 未覆盖 {field['name']}：{'、'.join(missing)}")
        for action_group in metadata["listActions"].values():
            for action in action_group:
                if action["actionCode"] not in provider_content or action["actionName"] not in provider_content:
                    errors.append(f"ListMetaProvider 未覆盖列表动作：{action['actionCode']}")
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
