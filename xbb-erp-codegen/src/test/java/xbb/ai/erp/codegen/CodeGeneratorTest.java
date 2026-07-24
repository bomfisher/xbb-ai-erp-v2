package xbb.ai.erp.codegen;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.codegen.cli.DbTableCodegenCli;
import xbb.ai.erp.codegen.generator.CodeGenerator;
import xbb.ai.erp.codegen.spec.ModuleSpec;
import xbb.ai.erp.codegen.spec.ModuleSpecLoader;
import xbb.ai.erp.codegen.spec.PathStrategyLoader;
import xbb.ai.erp.codegen.spec.PathStrategySpec;
import xbb.ai.erp.codegen.spec.SpecValidator;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CodeGeneratorTest {

    @Test
    void should_resolve_paths_without_hard_coding_customer_directory() throws Exception {
        ModuleSpec moduleSpec = new ModuleSpecLoader().load(Path.of("src/main/resources/examples/customer-module.yaml"));
        new SpecValidator().validate(moduleSpec);
        PathStrategySpec pathStrategySpec = new PathStrategyLoader().loadPreset(moduleSpec.getPathStrategy());
        Map<String, String> pathMap = new CodeGenerator().dryRun(moduleSpec, pathStrategySpec);
        assertEquals("xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/CustomerAdminController.java", pathMap.get("ADMIN_CONTROLLER"));
        assertEquals("xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/CustomerAdminAppService.java", pathMap.get("APP_SERVICE"));
        assertEquals("xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/model/Customer.java", pathMap.get("DOMAIN_MODEL"));
        assertEquals("xbb-erp-module-customer/src/main/resources/mapper/customer/CustomerMapper.xml", pathMap.get("MAPPER_XML"));
    }

    @Test
    void should_generate_core_files() throws Exception {
        ModuleSpec moduleSpec = new ModuleSpecLoader().load(Path.of("src/main/resources/examples/customer-module.yaml"));
        PathStrategySpec pathStrategySpec = new PathStrategyLoader().loadPreset(moduleSpec.getPathStrategy());
        Path outputRoot = Files.createTempDirectory("xbb-codegen-");
        new CodeGenerator().generate(outputRoot, moduleSpec, pathStrategySpec);
        assertTrue(Files.exists(outputRoot.resolve("xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/admin/CustomerAdminController.java")));
        assertTrue(Files.exists(outputRoot.resolve("xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/CustomerAdminAppService.java")));
        assertTrue(Files.exists(outputRoot.resolve("xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/service/impl/CustomerAdminAppServiceImpl.java")));
        assertTrue(Files.exists(outputRoot.resolve("xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/application/assembler/CustomerAdminAssembler.java")));
        assertTrue(Files.exists(outputRoot.resolve("xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/domain/model/Customer.java")));
        assertTrue(Files.exists(outputRoot.resolve("xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/po/CustomerPO.java")));
        assertTrue(Files.exists(outputRoot.resolve("xbb-erp-module-customer/src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/repository/CustomerRepositoryImpl.java")));
        assertTrue(Files.exists(outputRoot.resolve("xbb-erp-module-customer/src/main/resources/mapper/customer/CustomerMapper.xml")));
    }

    @Test
    void should_not_duplicate_base_entity_fields_in_po() throws Exception {
        ModuleSpec moduleSpec = new ModuleSpecLoader().load(Path.of("src/main/resources/examples/supplier-vendor.yaml"));
        PathStrategySpec pathStrategySpec = new PathStrategyLoader().loadPreset(moduleSpec.getPathStrategy());
        Path outputRoot = Files.createTempDirectory("xbb-codegen-supplier-");
        new CodeGenerator().generate(outputRoot, moduleSpec, pathStrategySpec);
        String poContent = Files.readString(outputRoot.resolve("xbb-erp-module-supplier/src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/po/VendorPO.java"));
        String xmlContent = Files.readString(outputRoot.resolve("xbb-erp-module-supplier/src/main/resources/mapper/supplier/VendorMapper.xml"));
        assertFalse(poContent.contains("extends BaseEntity"));
        assertTrue(poContent.contains("private Long updateTime;"));
        assertTrue(poContent.contains("private Long id;"));
        assertTrue(poContent.contains("private Integer deleted;"));
        assertTrue(poContent.contains("private Long addTime;"));
        assertTrue(poContent.contains("private Long updateTime;"));
        assertTrue(xmlContent.contains("and del = 0"));
    }

    @Test
    void should_resolve_purchase_paths_without_hard_coding_supplier_directory() throws Exception {
        ModuleSpec moduleSpec = new ModuleSpecLoader().load(Path.of("src/main/resources/examples/purchase/purchase-request.yaml"));
        new SpecValidator().validate(moduleSpec);
        PathStrategySpec pathStrategySpec = new PathStrategyLoader().loadPreset(moduleSpec.getPathStrategy());
        Map<String, String> pathMap = new CodeGenerator().dryRun(moduleSpec, pathStrategySpec);
        assertEquals("xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/admin/PurchaseRequestAdminController.java", pathMap.get("ADMIN_CONTROLLER"));
        assertEquals("xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/PurchaseRequestAdminAppService.java", pathMap.get("APP_SERVICE"));
        assertEquals("xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/domain/model/PurchaseRequest.java", pathMap.get("DOMAIN_MODEL"));
        assertEquals("xbb-erp-module-purchase/src/main/resources/mapper/purchase/PurchaseRequestMapper.xml", pathMap.get("MAPPER_XML"));
    }

    @Test
    void should_generate_purchase_request_core_files() throws Exception {
        ModuleSpec moduleSpec = new ModuleSpecLoader().load(Path.of("src/main/resources/examples/purchase/purchase-request.yaml"));
        PathStrategySpec pathStrategySpec = new PathStrategyLoader().loadPreset(moduleSpec.getPathStrategy());
        Path outputRoot = Files.createTempDirectory("xbb-codegen-purchase-");
        new CodeGenerator().generate(outputRoot, moduleSpec, pathStrategySpec);
        assertTrue(Files.exists(outputRoot.resolve("xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/admin/PurchaseRequestAdminController.java")));
        assertTrue(Files.exists(outputRoot.resolve("xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/PurchaseRequestAdminAppService.java")));
        assertTrue(Files.exists(outputRoot.resolve("xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/domain/model/PurchaseRequest.java")));
        assertTrue(Files.exists(outputRoot.resolve("xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/infrastructure/persistence/po/PurchaseRequestPO.java")));
        assertTrue(Files.exists(outputRoot.resolve("xbb-erp-module-purchase/src/main/resources/mapper/purchase/PurchaseRequestMapper.xml")));
    }

    @Test
    void should_generate_yaml_from_table_meta() throws Exception {
        Path yamlOutputDir = Files.createTempDirectory("xbb-codegen-yaml-");
        DbTableCodegenCli.TableMeta tableMeta = new DbTableCodegenCli.TableMeta(
            "purchase_request",
            List.of(
                new DbTableCodegenCli.ColumnMeta("id", "bigint", "主键", false, true),
                new DbTableCodegenCli.ColumnMeta("corpid", "varchar", "公司ID", false, false),
                new DbTableCodegenCli.ColumnMeta("request_no", "varchar", "申请单号", false, false),
                new DbTableCodegenCli.ColumnMeta("deleted", "tinyint", "删除标记", false, false),
                new DbTableCodegenCli.ColumnMeta("created_time", "datetime", "创建时间", true, false)
            )
        );

        Path yamlPath = DbTableCodegenCli.writeYaml(
            tableMeta,
            "purchase",
            "采购",
            "xbb.ai.erp.module.purchase",
            yamlOutputDir
        );

        ModuleSpec moduleSpec = new ModuleSpecLoader().load(yamlPath);
        assertEquals("purchase", moduleSpec.getModuleCode());
        assertEquals("xbb.ai.erp.module.purchase", moduleSpec.getPackageBase());
        assertEquals("PurchaseRequest", moduleSpec.getAggregate().getAggregateName());
        assertEquals("purchase_request", moduleSpec.getAggregate().getTableName());
        assertEquals("Integer", moduleSpec.getAggregate().getFields().stream()
            .filter(field -> "deleted".equals(field.getName()))
            .findFirst()
            .orElseThrow()
            .getJavaType());
        assertEquals("purchase_request.yaml", yamlPath.getFileName().toString());
    }

    @Test
    void should_skip_codegen_when_mapper_xml_exists() throws Exception {
        ModuleSpec moduleSpec = new ModuleSpecLoader().load(Path.of("src/main/resources/examples/purchase/purchase-request.yaml"));
        PathStrategySpec pathStrategySpec = new PathStrategyLoader().loadPreset(moduleSpec.getPathStrategy());
        Path outputRoot = Files.createTempDirectory("xbb-codegen-skip-");
        Path mapperXmlPath = outputRoot.resolve("xbb-erp-module-purchase/src/main/resources/mapper/purchase/PurchaseRequestMapper.xml");
        Files.createDirectories(mapperXmlPath.getParent());
        Files.writeString(mapperXmlPath, "existing", StandardCharsets.UTF_8);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        try {
            System.setOut(new PrintStream(byteArrayOutputStream, true, StandardCharsets.UTF_8));
            boolean generated = DbTableCodegenCli.generateIfMapperXmlMissing(outputRoot, moduleSpec, pathStrategySpec, new CodeGenerator());
            assertFalse(generated);
        } finally {
            System.setOut(originalOut);
        }

        String console = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        assertTrue(console.contains("skip codegen, mapper xml exists"));
        assertFalse(Files.exists(outputRoot.resolve("xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/admin/PurchaseRequestAdminController.java")));
        assertEquals("existing", Files.readString(mapperXmlPath));
    }

    @Test
    void should_generate_purchase_request_crud_details() throws Exception {
        ModuleSpec moduleSpec = new ModuleSpecLoader().load(Path.of("src/main/resources/examples/purchase/purchase-request.yaml"));
        PathStrategySpec pathStrategySpec = new PathStrategyLoader().loadPreset(moduleSpec.getPathStrategy());
        Path outputRoot = Files.createTempDirectory("xbb-codegen-purchase-crud-");
        new CodeGenerator().generate(outputRoot, moduleSpec, pathStrategySpec);

        String appServiceImpl = Files.readString(outputRoot.resolve("xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/application/service/impl/PurchaseRequestAdminAppServiceImpl.java"));
        String repositoryImpl = Files.readString(outputRoot.resolve("xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/infrastructure/persistence/repository/PurchaseRequestRepositoryImpl.java"));
        String mapperXml = Files.readString(outputRoot.resolve("xbb-erp-module-purchase/src/main/resources/mapper/purchase/PurchaseRequestMapper.xml"));
        String conditionMapHelper = Files.readString(outputRoot.resolve("xbb-erp-module-purchase/src/main/java/xbb/ai/erp/module/purchase/infrastructure/persistence/repository/ConditionMapHelper.java"));

        assertTrue(appServiceImpl.contains("conditionMap.put(\"pageNum\", dto.getPageNum());"));
        assertTrue(appServiceImpl.contains("purchaseRequestRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());"));
        assertFalse(appServiceImpl.contains("dto.getIdList().forEach(id -> purchaseRequestRepository.removeById(dto.getCorpid(), id));"));

        assertTrue(repositoryImpl.contains("Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);"));
        assertTrue(repositoryImpl.contains("return purchaseRequestMapper.findByCondition(preparedConditionMap).stream().map(PurchaseRequestConvertor::toDomain).toList();"));
        assertTrue(repositoryImpl.contains("return purchaseRequestMapper.count(preparedConditionMap);"));

        assertTrue(mapperXml.contains("<sql id=\"BaseCondition\">"));
        assertTrue(mapperXml.contains("group by ${conditionMap.groupByStr}"));
        assertTrue(mapperXml.contains("order by ${conditionMap.orderByStr}"));

        assertTrue(conditionMapHelper.contains("static Map<String, Object> prepare(Map<String, Object> source)"));
        assertTrue(conditionMapHelper.contains("conditionMap.put(\"offset\", (pageNum - 1) * pageSize);"));
        assertTrue(conditionMapHelper.contains("throw new IllegalArgumentException(key + \" contains invalid characters\");"));
    }
}
