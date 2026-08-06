#!/usr/bin/env python3
"""验证通用业务模块交付脚本的关键约束。"""

import subprocess
import tempfile
import unittest
import json
from pathlib import Path


SCRIPTS = Path(__file__).parent


def write_spec(path: Path, role: str, aggregate: str, table: str, admin: bool, application: bool) -> None:
    path.write_text(
        "\n".join((
            "moduleCode: sales",
            f"aggregateRole: {role}",
            "aggregate:",
            f"  aggregateName: {aggregate}",
            f"  tableName: {table}",
            "generate:",
            f"  admin: {str(admin).lower()}",
            f"  application: {str(application).lower()}",
            "  domain: true",
            "  persistence: true",
            "  xml: true",
            "",
        )),
        encoding="utf-8",
    )


def write_metadata(path: Path) -> None:
    path.write_text(json.dumps({
        "businessCode": "SALES_ORDER",
        "fields": [{
            "name": "orderNo",
            "attr": "main.orderNo",
            "attrName": "订单编号",
            "fieldType": "TEXT",
            "scenes": ["LIST", "CREATE", "UPDATE"],
            "filter": {
                "column": "order_no",
                "fieldType": "TEXT",
                "symbols": ["EQ", "CONTAINS"],
            },
        }],
        "listActions": {
            "top": [{"actionCode": "ADD", "actionName": "新增"}],
            "bottom": [],
            "row": [{"actionCode": "EDIT", "actionName": "编辑"}],
        },
    }, ensure_ascii=False), encoding="utf-8")


class DeliveryScriptsTest(unittest.TestCase):
    def test_module_specs_and_scope_support_root_and_child(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            root = Path(temp_dir) / "order.yaml"
            child = Path(temp_dir) / "order-item.yaml"
            output = Path(temp_dir) / "scope.json"
            metadata = Path(temp_dir) / "field-metadata.json"
            write_spec(root, "ROOT", "SalesOrder", "sales_order", True, True)
            write_spec(child, "CHILD", "SalesOrderItem", "sales_order_item", False, False)
            write_metadata(metadata)
            subprocess.run(["python3", str(SCRIPTS / "validate_module_specs.py"), str(root), str(child)], check=True)
            subprocess.run([
                "python3", str(SCRIPTS / "build_delivery_scope.py"), "--module-code", "sales",
                "--root-spec", str(root), "--child-spec", str(child), "--field-metadata", str(metadata), "--output", str(output),
            ], check=True)
            self.assertIn('"SalesOrderItem"', output.read_text(encoding="utf-8"))

    def test_field_metadata_rejects_unspecified_business_values(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            metadata = Path(temp_dir) / "field-metadata.json"
            metadata.write_text('{"businessCode":"sales"}', encoding="utf-8")
            result = subprocess.run(
                ["python3", str(SCRIPTS / "validate_field_metadata.py"), str(metadata)],
                capture_output=True,
                text=True,
            )
            self.assertNotEqual(0, result.returncode)
            self.assertIn("请向开发者确认后补充", result.stderr)

    def test_delivery_validator_accepts_complete_root_and_child(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            module_root = Path(temp_dir) / "xbb-erp-module-sales"
            source_root = module_root / "src/main/java/xbb/ai/erp/module/sales"
            metadata = Path(temp_dir) / "field-metadata.json"
            write_metadata(metadata)
            directories = (
                "admin/dto", "admin/vo", "application/assembler", "application/field", "application/port",
                "application/provider", "application/schema", "application/service/impl", "application/service/query",
                "application/service/save", "application/service/draft", "application/validator", "domain/model",
                "domain/repository", "infrastructure/persistence/convertor", "infrastructure/persistence/mapper",
                "infrastructure/persistence/po", "infrastructure/persistence/repository",
            )
            for directory in directories:
                (source_root / directory).mkdir(parents=True, exist_ok=True)
            mapper_root = module_root / "src/main/resources/mapper/sales"
            mapper_root.mkdir(parents=True)
            (mapper_root / "SalesOrderMapper.xml").write_text("<mapper/>", encoding="utf-8")
            (module_root / "src/test/java").mkdir(parents=True)
            business_enum = module_root.parent / "xbb-erp-base-common/src/main/java/xbb/ai/erp/base/common/module/BusinessCodeEnum.java"
            business_enum.parent.mkdir(parents=True)
            business_enum.write_text('enum BusinessCodeEnum { SALES_ORDER("SALES_ORDER") }\n', encoding="utf-8")
            endpoint_methods = "\n".join(
                f'    @PostMapping("/{endpoint}")\n    public ResultVO<Void> {endpoint}() {{ return null; }}'
                for endpoint in ("list", "addItem", "updateItem", "saveDraft", "saveAndSubmit", "draftList", "loadDraft")
            )
            (source_root / "admin/SalesOrderAdminController.java").write_text(
                "class SalesOrderAdminController {\n" + endpoint_methods + "\n}\n", encoding="utf-8"
            )
            (source_root / "application/service/SalesOrderAdminAppService.java").write_text(
                "interface SalesOrderAdminAppService {\n"
                + "\n".join(f"    void {endpoint}();" for endpoint in ("list", "addItem", "updateItem", "saveDraft", "saveAndSubmit", "draftList", "loadDraft"))
                + "\n}\n", encoding="utf-8"
            )
            (source_root / "admin/SalesOrderFieldEnum.java").write_text(
                'enum SalesOrderFieldEnum { ORDER_NO("main.orderNo", "订单编号", FieldTypeEnum.TEXT) }\n', encoding="utf-8"
            )
            (source_root / "application/service/query/SalesOrderQueryAppService.java").write_text(
                "class SalesOrderQueryAppService { void addItem() { setHeadList(SceneTypeEnum.CREATE); } "
                "void updateItem() { setHeadList(SceneTypeEnum.UPDATE); } void setHeadList(Object value) {} }\n",
                encoding="utf-8",
            )
            (source_root / "application/provider/SalesOrderListMetaProvider.java").write_text(
                'class SalesOrderListMetaProvider { String data = "main.orderNo 订单编号 order_no TEXT ADD 新增 EDIT 编辑"; }\n',
                encoding="utf-8",
            )
            for relative_path in (
                "domain/model/SalesOrderItem.java", "domain/repository/SalesOrderItemRepository.java",
                "infrastructure/persistence/po/SalesOrderItemPO.java", "infrastructure/persistence/mapper/SalesOrderItemMapper.java",
                "infrastructure/persistence/repository/SalesOrderItemRepositoryImpl.java",
            ):
                target = source_root / relative_path
                target.write_text("class Placeholder {}\n", encoding="utf-8")
            subprocess.run([
                "python3", str(SCRIPTS / "verify_module_delivery.py"), str(module_root), "SalesOrder",
                "--field-metadata", str(metadata), "--child", "SalesOrderItem",
            ], check=True)


if __name__ == "__main__":
    unittest.main()
