#!/usr/bin/env python3
"""验证通用业务模块交付脚本的关键约束。"""

import subprocess
import tempfile
import unittest
import json
import importlib.util
from pathlib import Path


SCRIPTS = Path(__file__).parent
ROOT_DIRECTORIES_FOR_TEST = (
    "admin/dto", "admin/vo", "application/assembler", "application/field", "application/port",
    "application/provider", "application/schema", "application/service/impl", "application/service/query",
    "application/service/save", "application/service/draft", "application/validator", "domain/model",
    "domain/repository", "infrastructure/persistence/convertor", "infrastructure/persistence/mapper",
    "infrastructure/persistence/po", "infrastructure/persistence/repository",
)
RUN_CODEGEN_SPEC = importlib.util.spec_from_file_location("run_codegen", SCRIPTS / "run_codegen.py")
RUN_CODEGEN = importlib.util.module_from_spec(RUN_CODEGEN_SPEC)
RUN_CODEGEN_SPEC.loader.exec_module(RUN_CODEGEN)


def write_spec(path: Path, role: str, aggregate: str, table: str, admin: bool, application: bool) -> None:
    path.write_text(
        "\n".join((
            "moduleDir: sales-management",
            "moduleCode: sales",
            "moduleApiName: salesManagement",
            "businessName: salesOrder",
            "businessCode: SALES_ORDER",
            "packageBase: xbb.ai.erp.module.sales",
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
            "required": True,
            "editable": True,
            "defaultValue": None,
            "filterName": "order_no",
            "filterFieldType": "TEXT",
            "supportedSymbols": ["EQ", "NE", "CONTAINS", "NOT_CONTAINS", "IS_EMPTY", "IS_NOT_EMPTY"],
        }],
        "listActions": {
            "top": [{"actionCode": "ADD", "actionName": "新增"}],
            "bottom": [],
            "row": [{"actionCode": "EDIT", "actionName": "编辑"}],
        },
    }, ensure_ascii=False), encoding="utf-8")


class DeliveryScriptsTest(unittest.TestCase):
    def test_module_dir_controls_maven_module_path(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            spec = Path(temp_dir) / "master-data.yaml"
            spec.write_text("moduleDir: master-data\nmoduleCode: module_master_data\n", encoding="utf-8")
            self.assertEqual("module_master_data", RUN_CODEGEN.module_code(spec))
            self.assertEqual("master-data", RUN_CODEGEN.module_dir(spec))

    def test_module_dir_rejects_underscores_and_missing_values(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            underscore_spec = Path(temp_dir) / "invalid.yaml"
            missing_spec = Path(temp_dir) / "missing.yaml"
            underscore_spec.write_text("moduleDir: master_data\n", encoding="utf-8")
            missing_spec.write_text("moduleCode: master_data\n", encoding="utf-8")
            with self.assertRaisesRegex(ValueError, "短横线"):
                RUN_CODEGEN.module_dir(underscore_spec)
            with self.assertRaisesRegex(ValueError, "缺少 moduleDir"):
                RUN_CODEGEN.module_dir(missing_spec)

    def test_module_spec_validation_rejects_invalid_module_identity(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            spec = Path(temp_dir) / "invalid.yaml"
            write_spec(spec, "ROOT", "SalesOrder", "sales_order", True, True)
            spec.write_text(spec.read_text(encoding="utf-8").replace("moduleDir: sales-management", "moduleDir: sales_management"), encoding="utf-8")
            result = subprocess.run(
                ["python3", str(SCRIPTS / "validate_module_specs.py"), str(spec)],
                capture_output=True,
                text=True,
            )
            self.assertNotEqual(0, result.returncode)
            self.assertIn("短横线", result.stderr)

    def test_module_spec_validation_requires_root_route_identity(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            spec = Path(temp_dir) / "missing-route-identity.yaml"
            write_spec(spec, "ROOT", "SalesOrder", "sales_order", True, True)
            spec.write_text(
                spec.read_text(encoding="utf-8").replace("businessCode: SALES_ORDER\n", ""),
                encoding="utf-8",
            )
            result = subprocess.run(
                ["python3", str(SCRIPTS / "validate_module_specs.py"), str(spec)],
                capture_output=True,
                text=True,
            )
            self.assertNotEqual(0, result.returncode)
            self.assertIn("ROOT 规格缺少属性：businessCode", result.stderr)

    def test_list_meta_provider_generator_uses_complete_metadata(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            root = Path(temp_dir)
            metadata = root / "field-metadata.json"
            spec = root / "sales-order.yaml"
            module_root = root / "xbb-erp-module-sales-management"
            write_metadata(metadata)
            write_spec(spec, "ROOT", "SalesOrder", "sales_order", True, True)
            command = [
                "python3", str(SCRIPTS / "generate_list_meta_provider.py"),
                str(metadata), str(spec), str(module_root), "--apply",
            ]
            subprocess.run(command, check=True)
            provider = module_root / "src/main/java/xbb/ai/erp/module/sales/application/provider/SalesOrderListMetaProvider.java"
            content = provider.read_text(encoding="utf-8")
            self.assertIn("import xbb.ai.erp.module.sales.admin.SalesOrderFieldEnum;", content)
            self.assertIn("Arrays.stream(SalesOrderFieldEnum.values())", content)
            self.assertIn("field.getFilterName()", content)
            self.assertIn('new ListButtonItemPojo("ADD", "新增", 10, "ADD")', content)

    def test_query_form_contract_generator_uses_complete_metadata(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            root = Path(temp_dir)
            metadata = root / "field-metadata.json"
            spec = root / "sales-order.yaml"
            module_root = root / "xbb-erp-module-sales-management"
            write_metadata(metadata)
            write_spec(spec, "ROOT", "SalesOrder", "sales_order", True, True)
            source_root = module_root / "src/main/java/xbb/ai/erp/module/sales"
            for path in (source_root / "admin", source_root / "application/service", source_root / "application/service/impl"):
                path.mkdir(parents=True, exist_ok=True)
            (source_root / "admin/SalesOrderAdminController.java").write_text(
                "package xbb.ai.erp.module.sales.admin;\n\n"
                "import xbb.ai.erp.base.common.dto.IdBaseDTO;\n"
                "import xbb.ai.erp.module.sales.admin.dto.SalesOrderListDTO;\n\n"
                "class SalesOrderAdminController { void list(SalesOrderListDTO request) {} }\n",
                encoding="utf-8",
            )
            (source_root / "application/service/SalesOrderAdminAppService.java").write_text(
                "package xbb.ai.erp.module.sales.application.service;\n\n"
                "import xbb.ai.erp.base.common.dto.IdBaseDTO;\n"
                "import xbb.ai.erp.module.sales.admin.dto.SalesOrderListDTO;\n\n"
                "interface SalesOrderAdminAppService { void list(SalesOrderListDTO request); }\n",
                encoding="utf-8",
            )
            (source_root / "application/service/impl/SalesOrderAdminAppServiceImpl.java").write_text(
                "package xbb.ai.erp.module.sales.application.service.impl;\n\n"
                "import xbb.ai.erp.base.common.dto.IdBaseDTO;\n"
                "import xbb.ai.erp.module.sales.admin.dto.SalesOrderListDTO;\n\n"
                "class SalesOrderAdminAppServiceImpl { void list(SalesOrderListDTO request) {} }\n",
                encoding="utf-8",
            )
            subprocess.run([
                "python3", str(SCRIPTS / "generate_query_form_contract.py"), str(metadata), str(spec), str(module_root), "--apply",
            ], check=True)
            query = (source_root / "application/service/query/SalesOrderQueryAppServiceImpl.java").read_text(encoding="utf-8")
            factory = (source_root / "application/field/SalesOrderFieldFactory.java").read_text(encoding="utf-8")
            field_enum = (source_root / "admin/SalesOrderFieldEnum.java").read_text(encoding="utf-8")
            controller = (source_root / "admin/SalesOrderAdminController.java").read_text(encoding="utf-8")
            service = (source_root / "application/service/SalesOrderAdminAppService.java").read_text(encoding="utf-8")
            service_impl = (source_root / "application/service/impl/SalesOrderAdminAppServiceImpl.java").read_text(encoding="utf-8")
            self.assertIn("list(ListBaseDTO dto)", query)
            self.assertIn("listQueryMapUtil.gen(dto, schemaProvider.conditionMetaMap())", query)
            self.assertIn("setHeadList(SceneFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.CREATE)))", query)
            self.assertIn("Arrays.stream(SalesOrderFieldEnum.values())", factory)
            self.assertIn('ORDER_NO("main.orderNo", "订单编号", FieldTypeEnum.TEXT, "order_no"', field_enum)
            for content in (controller, service, service_impl):
                self.assertIn("list(ListBaseDTO request)", content)
                self.assertIn("import xbb.ai.erp.base.common.dto.ListBaseDTO;", content)
                self.assertNotIn("SalesOrderListDTO", content)

    def test_delivery_validator_requires_standard_list_and_form_contract(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            module_root = Path(temp_dir) / "xbb-erp-module-sales"
            source_root = module_root / "src/main/java/xbb/ai/erp/module/sales"
            metadata = Path(temp_dir) / "field-metadata.json"
            write_metadata(metadata)
            for directory in ROOT_DIRECTORIES_FOR_TEST:
                (source_root / directory).mkdir(parents=True, exist_ok=True)
            (module_root / "src/main/resources/mapper/sales").mkdir(parents=True, exist_ok=True)
            (module_root / "src/main/resources/mapper/sales/SalesOrderMapper.xml").write_text(
                '<mapper><insert id="insertBatch">insert into sales_order (id, corpid) values</insert></mapper>',
                encoding="utf-8",
            )
            (source_root / "admin/SalesOrderAdminController.java").write_text("class SalesOrderAdminController {}", encoding="utf-8")
            (source_root / "application/service/SalesOrderAdminAppService.java").write_text("interface SalesOrderAdminAppService {}", encoding="utf-8")
            (source_root / "application/service/query/SalesOrderQueryAppServiceImpl.java").write_text("class SalesOrderQueryAppServiceImpl {}", encoding="utf-8")
            verify = subprocess.run(
                ["python3", str(SCRIPTS / "verify_module_delivery.py"), str(module_root), "SalesOrder", "--field-metadata", str(metadata), "--skip-tests"],
                capture_output=True,
                text=True,
            )
            self.assertNotEqual(0, verify.returncode)
            self.assertIn("ListBaseDTO", verify.stderr)
            self.assertIn("headList", verify.stderr)

    def test_delivery_validator_requires_auto_increment_id_round_trip(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            root = Path(temp_dir)
            module_root = root / "xbb-erp-module-sales"
            source_root = module_root / "src/main/java/xbb/ai/erp/module/sales"
            metadata = root / "field-metadata.json"
            write_metadata(metadata)
            for directory in ROOT_DIRECTORIES_FOR_TEST:
                (source_root / directory).mkdir(parents=True, exist_ok=True)
            (module_root / "src/main/resources/mapper/sales").mkdir(parents=True, exist_ok=True)
            (module_root / "src/main/resources/mapper/sales/SalesOrderMapper.xml").write_text(
                '<mapper><insert id="insertBatch">insert into sales_order (id, corpid) values</insert></mapper>',
                encoding="utf-8",
            )
            (source_root / "admin/SalesOrderAdminController.java").write_text("class SalesOrderAdminController {}", encoding="utf-8")
            (source_root / "application/service/SalesOrderAdminAppService.java").write_text("interface SalesOrderAdminAppService {}", encoding="utf-8")
            (source_root / "application/service/query/SalesOrderQueryAppServiceImpl.java").write_text("class SalesOrderQueryAppServiceImpl {}", encoding="utf-8")
            repository = source_root / "infrastructure/persistence/repository/SalesOrderRepositoryImpl.java"
            repository.write_text(
                '@Repository("xbbAiErpModuleSalesSalesOrderRepositoryImpl")\n'
                "class SalesOrderRepositoryImpl {\n"
                "    public Long insert(SalesOrder entity) { mapper.insert(po); return null; }\n"
                "    public void insertBatch(List<SalesOrder> entityList) { mapper.insertBatch(poList); }\n"
                "    @Override void update() {}\n"
                "}\n",
                encoding="utf-8",
            )
            (source_root / "infrastructure/persistence/po/SalesOrderPO.java").write_text("class SalesOrderPO { Long id; }", encoding="utf-8")
            verify = subprocess.run(
                ["python3", str(SCRIPTS / "verify_module_delivery.py"), str(module_root), "SalesOrder", "--field-metadata", str(metadata), "--skip-tests"],
                capture_output=True,
                text=True,
            )
            self.assertNotEqual(0, verify.returncode)
            self.assertIn("AUTO_INCREMENT 插入前必须清空 PO.id", verify.stderr)
            self.assertIn("AUTO_INCREMENT 插入后必须将 PO.id 回写领域对象", verify.stderr)
            self.assertIn("AUTO_INCREMENT insert 必须返回数据库回填的 PO.id", verify.stderr)
            self.assertIn("AUTO_INCREMENT insertBatch 必须清空 PO.id 并将回填主键逐项写回领域数组", verify.stderr)
            self.assertIn("insertBatch 必须配置 useGeneratedKeys", verify.stderr)
            self.assertIn("AUTO_INCREMENT insertBatch 不得插入 id 列", verify.stderr)
            self.assertIn("BaseEntity", verify.stderr)

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

    def test_business_code_sync_requires_explicit_apply_and_is_idempotent(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            metadata = Path(temp_dir) / "field-metadata.json"
            enum_path = Path(temp_dir) / "BusinessCodeEnum.java"
            write_metadata(metadata)
            enum_path.write_text('enum BusinessCodeEnum { DEMO("DEMO"),\n    ;\n}\n', encoding="utf-8")
            command = ["python3", str(SCRIPTS / "sync_business_code.py"), str(metadata), "--enum-path", str(enum_path)]
            missing = subprocess.run(command, capture_output=True, text=True)
            self.assertNotEqual(0, missing.returncode)
            self.assertIn("缺少业务编码：SALES_ORDER", missing.stderr)
            subprocess.run([*command, "--apply"], check=True)
            self.assertIn('SALES_ORDER("SALES_ORDER")', enum_path.read_text(encoding="utf-8"))
            subprocess.run(command, check=True)

    def test_field_design_generator_derives_filter_rules_from_field_type(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            design = Path(temp_dir) / "field-design.yaml"
            metadata = Path(temp_dir) / "field-metadata.json"
            design.write_text(
                """businessCode: SALES_ORDER
fields:
  - name: orderNo
    attr: main.orderNo
    attrName: 订单编号
    fieldType: TEXT
    scenes: [LIST, CREATE, UPDATE]
    required: true
    editable: true
    defaultValue: null
    filterName: order_no
  - name: file
    attr: main.file
    attrName: 附件
    fieldType: FILE
    scenes: [CREATE, UPDATE]
    required: false
    editable: true
    defaultValue: null
    filterName: null
  - name: tags
    attr: main.tags
    attrName: 标签
    fieldType: COMB_MULTI
    scenes: [LIST, CREATE, UPDATE]
    required: false
    editable: true
    defaultValue: null
    filterName: tags
    options: "A:甲, B:乙"
listActions:
  top: []
  bottom: []
  row: []
""",
                encoding="utf-8",
            )
            subprocess.run(["ruby", str(SCRIPTS / "generate_field_metadata.rb"), str(design), str(metadata)], check=True)
            generated = json.loads(metadata.read_text(encoding="utf-8"))
            self.assertEqual("order_no", generated["fields"][0]["filterName"])
            self.assertEqual("TEXT", generated["fields"][0]["filterFieldType"])
            self.assertEqual(["EQ", "NE", "CONTAINS", "NOT_CONTAINS", "IS_EMPTY", "IS_NOT_EMPTY"], generated["fields"][0]["supportedSymbols"])
            self.assertIsNone(generated["fields"][1]["filterName"])
            self.assertEqual("tags", generated["fields"][2]["filterName"])
            self.assertEqual("ENUM_MULTI", generated["fields"][2]["filterFieldType"])
            self.assertEqual(["CONTAINS", "NOT_CONTAINS", "CONTAINS_ALL", "NOT_CONTAINS_ALL", "IS_EMPTY", "IS_NOT_EMPTY"], generated["fields"][2]["supportedSymbols"])
            subprocess.run(["python3", str(SCRIPTS / "validate_field_metadata.py"), str(metadata)], check=True)

    def test_field_design_generator_preserves_type_specific_configuration(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            design = Path(temp_dir) / "field-design.yaml"
            metadata = Path(temp_dir) / "field-metadata.json"
            design.write_text(
                """businessCode: SALES_ORDER
fields:
  - name: status
    attr: main.status
    attrName: 状态
    fieldType: COMB
    scenes: [CREATE, UPDATE]
    required: false
    editable: true
    defaultValue: null
    filterName: status
    options: "0:禁用, 1:启用"
  - name: productDataId
    attr: main.productDataId
    attrName: 产品
    fieldType: BUSINESS
    scenes: [CREATE, UPDATE]
    required: false
    editable: true
    defaultValue: null
    filterName: data_id
    businessCode: PRODUCT
  - name: items
    attr: items
    attrName: 明细
    fieldType: SUB_ITEM
    scenes: [CREATE, UPDATE]
    required: false
    editable: true
    defaultValue: null
    filterName: null
    subFields:
      - name: skuId
        attr: skuId
        attrName: 产品
        fieldType: PRODUCT
        scenes: [CREATE, UPDATE]
        required: true
        editable: true
        defaultValue: null
        filterName: null
listActions:
  top: []
  bottom: []
  row: []
""",
                encoding="utf-8",
            )
            subprocess.run(["ruby", str(SCRIPTS / "generate_field_metadata.rb"), str(design), str(metadata)], check=True)
            generated = json.loads(metadata.read_text(encoding="utf-8"))
            self.assertEqual("0:禁用, 1:启用", generated["fields"][0]["options"])
            self.assertEqual(["CONTAINS", "NOT_CONTAINS", "IS_EMPTY", "IS_NOT_EMPTY"], generated["fields"][0]["supportedSymbols"])
            self.assertEqual("PRODUCT", generated["fields"][1]["businessCode"])
            self.assertEqual("data_id", generated["fields"][1]["filterName"])
            self.assertEqual("skuId", generated["fields"][2]["subFields"][0]["name"])
            subprocess.run(["python3", str(SCRIPTS / "validate_field_metadata.py"), str(metadata)], check=True)

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
                for endpoint in ("addItem", "updateItem", "saveDraft", "saveAndSubmit", "draftList", "loadDraft")
            )
            (source_root / "admin/SalesOrderAdminController.java").write_text(
                "class SalesOrderAdminController {\n"
                "    @PostMapping(\"/list\")\n    public ResultVO<Void> list(@RequestBody ListBaseDTO dto) { return null; }\n"
                + endpoint_methods + "\n}\n", encoding="utf-8"
            )
            (source_root / "application/service/SalesOrderAdminAppService.java").write_text(
                "interface SalesOrderAdminAppService {\n    void list(ListBaseDTO dto);\n"
                + "\n".join(f"    void {endpoint}();" for endpoint in ("addItem", "updateItem", "saveDraft", "saveAndSubmit", "draftList", "loadDraft"))
                + "\n}\n", encoding="utf-8"
            )
            (source_root / "admin/SalesOrderFieldEnum.java").write_text(
                'enum SalesOrderFieldEnum { ORDER_NO("main.orderNo", "订单编号", FieldTypeEnum.TEXT, "order_no"), TAGS("main.tags", "标签", FieldTypeEnum.COMB_MULTI, "tags") }\n', encoding="utf-8"
            )
            (source_root / "application/field/SalesOrderFieldFactory.java").write_text(
                'class SalesOrderFieldFactory { Object data = Arrays.stream(SalesOrderFieldEnum.values()); }\n', encoding="utf-8"
            )
            (source_root / "application/service/query/SalesOrderQueryAppService.java").write_text(
                "class SalesOrderQueryAppService { void list(ListBaseDTO dto) { listQueryMapUtil.gen(dto, conditionMetaMap()); } "
                "ListQueryMapUtil listQueryMapUtil; Object conditionMetaMap() { return null; } void addItem() { setHeadList(SceneTypeEnum.CREATE); } "
                "void updateItem() { setHeadList(SceneTypeEnum.UPDATE); } void setHeadList(Object value) {} }\n",
                encoding="utf-8",
            )
            (source_root / "application/provider/SalesOrderListMetaProvider.java").write_text(
                'class SalesOrderListMetaProvider { Object data = Arrays.stream(SalesOrderFieldEnum.values()); String actions = "ADD 新增 EDIT 编辑"; }\n',
                encoding="utf-8",
            )
            (source_root / "infrastructure/persistence/repository/SalesOrderRepositoryImpl.java").write_text(
                '@Repository("xbbAiErpModuleSalesSalesOrderRepositoryImpl") class SalesOrderRepositoryImpl {}\n',
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
