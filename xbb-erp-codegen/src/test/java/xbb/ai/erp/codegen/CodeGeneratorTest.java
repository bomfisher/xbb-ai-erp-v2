package xbb.ai.erp.codegen;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.codegen.cli.DbTableCodegenCli;
import xbb.ai.erp.codegen.generator.CodeGenerator;
import xbb.ai.erp.codegen.generator.DddFilePlan;
import xbb.ai.erp.codegen.generator.DddGenerationContext;
import xbb.ai.erp.codegen.generator.DddGenerationReport;
import xbb.ai.erp.codegen.generator.DddModuleLayoutPlanner;
import xbb.ai.erp.codegen.spec.ModuleSpec;
import xbb.ai.erp.codegen.spec.ModuleSpecLoader;
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
    void should_resolve_paths_with_module_root() throws Exception {
        ModuleSpec moduleSpec = new ModuleSpecLoader().load(Path.of("src/main/resources/examples/customer-module.yaml"));
        new SpecValidator().validate(moduleSpec);
        Path moduleRootDir = prepareModuleRoot("customer");
        DddGenerationContext context = DddGenerationContext.create(moduleRootDir, moduleSpec, "full");
        List<DddFilePlan> plans = new DddModuleLayoutPlanner().plan(context);
        Map<String, String> pathMap = new CodeGenerator().dryRun(plans);

        assertEquals(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/customer/admin/CustomerAdminController.java").toString(), pathMap.get("ADMIN_CONTROLLER"));
        assertEquals(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/customer/application/service/query/CustomerQueryAppServiceImpl.java").toString(), pathMap.get("APP_QUERY_SERVICE_IMPL"));
        assertEquals(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/customer/application/service/save/CustomerSaveAppServiceImpl.java").toString(), pathMap.get("APP_SAVE_SERVICE_IMPL"));
        assertEquals(moduleRootDir.resolve("src/main/resources/mapper/customer/CustomerMapper.xml").toString(), pathMap.get("MAPPER_XML"));
    }

    @Test
    void should_generate_core_files() throws Exception {
        ModuleSpec moduleSpec = new ModuleSpecLoader().load(Path.of("src/main/resources/examples/customer-module.yaml"));
        Path moduleRootDir = prepareModuleRoot("customer");
        DddGenerationContext context = DddGenerationContext.create(moduleRootDir, moduleSpec, "full");
        List<DddFilePlan> plans = new DddModuleLayoutPlanner().plan(context);
        new CodeGenerator().generate(context, plans);

        assertTrue(Files.exists(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/customer/admin/CustomerAdminController.java")));
        assertTrue(Files.exists(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/customer/application/service/query/CustomerQueryAppServiceImpl.java")));
        assertTrue(Files.exists(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/customer/application/service/save/CustomerSaveAppServiceImpl.java")));
        assertTrue(Files.exists(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/customer/application/service/draft/CustomerDraftAppService.java")));
        assertTrue(Files.exists(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/customer/application/provider/CustomerListMetaProvider.java")));
        assertTrue(Files.exists(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/customer/application/assembler/CustomerAdminAssembler.java")));
        assertTrue(Files.exists(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/customer/domain/model/Customer.java")));
        assertTrue(Files.exists(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/po/CustomerPO.java")));
        assertTrue(Files.exists(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/repository/CustomerRepositoryImpl.java")));
        assertTrue(Files.exists(moduleRootDir.resolve("src/main/resources/mapper/customer/CustomerMapper.xml")));
        String controllerContent = Files.readString(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/customer/admin/CustomerAdminController.java"));
        String mapperContent = Files.readString(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/mapper/CustomerMapper.java"));
        String xmlContent = Files.readString(moduleRootDir.resolve("src/main/resources/mapper/customer/CustomerMapper.xml"));
        String providerContent = Files.readString(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/customer/application/provider/CustomerListMetaProvider.java"));
        assertTrue(mapperContent.contains("import org.apache.ibatis.annotations.Mapper;"));
        assertTrue(mapperContent.contains("@Mapper\npublic interface CustomerMapper"));
        String repositoryContent = Files.readString(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/repository/CustomerRepositoryImpl.java"));
        assertTrue(repositoryContent.contains("@Repository(\"xbbAiErpModuleCustomerCustomerRepositoryImpl\")"));
        assertTrue(controllerContent.contains("@PostMapping(\"/saveDraft\")"));
        assertTrue(controllerContent.contains("@PostMapping(\"/saveAndSubmit\")"));
        assertTrue(controllerContent.contains("@PostMapping(\"/draftList\")"));
        assertTrue(controllerContent.contains("@PostMapping(\"/loadDraft\")"));
        assertTrue(controllerContent.contains("ResultVO.success"));
        assertFalse(controllerContent.contains("@PostMapping(\"/save\")"));
        assertTrue(xmlContent.contains("xbb.ai.erp.module.common.application.filter.CommonListFilterMapper.dynamicCondition"));
        assertTrue(providerContent.contains("implements ListMetaProvider"));
    }

    @Test
    void should_not_duplicate_base_entity_fields_in_po() throws Exception {
        ModuleSpec moduleSpec = new ModuleSpecLoader().load(Path.of("src/main/resources/examples/supplier-vendor.yaml"));
        Path moduleRootDir = prepareModuleRoot("supplier");
        DddGenerationContext context = DddGenerationContext.create(moduleRootDir, moduleSpec, "full");
        List<DddFilePlan> plans = new DddModuleLayoutPlanner().plan(context);
        new CodeGenerator().generate(context, plans);

        String poContent = Files.readString(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/supplier/infrastructure/persistence/po/VendorPO.java"));
        String xmlContent = Files.readString(moduleRootDir.resolve("src/main/resources/mapper/supplier/VendorMapper.xml"));
        assertTrue(poContent.contains("extends BaseEntity"));
        assertFalse(poContent.contains("private Long id;"));
        assertFalse(poContent.contains("private Integer deleted;"));
        assertFalse(poContent.contains("private Long addTime;"));
        assertFalse(poContent.contains("private Long updateTime;"));
        assertTrue(xmlContent.contains("and del = 0"));
    }

    @Test
    void should_resolve_purchase_paths_with_module_root() throws Exception {
        ModuleSpec moduleSpec = new ModuleSpecLoader().load(Path.of("src/main/resources/examples/purchase/purchase-request.yaml"));
        new SpecValidator().validate(moduleSpec);
        Path moduleRootDir = prepareModuleRoot("purchase");
        DddGenerationContext context = DddGenerationContext.create(moduleRootDir, moduleSpec, "full");
        List<DddFilePlan> plans = new DddModuleLayoutPlanner().plan(context);
        Map<String, String> pathMap = new CodeGenerator().dryRun(plans);

        assertEquals(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/purchase/admin/PurchaseRequestAdminController.java").toString(), pathMap.get("ADMIN_CONTROLLER"));
        assertEquals(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/purchase/application/service/query/PurchaseRequestQueryAppServiceImpl.java").toString(), pathMap.get("APP_QUERY_SERVICE_IMPL"));
        assertEquals(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/purchase/domain/model/PurchaseRequest.java").toString(), pathMap.get("DOMAIN_MODEL"));
        assertEquals(moduleRootDir.resolve("src/main/resources/mapper/purchase/PurchaseRequestMapper.xml").toString(), pathMap.get("MAPPER_XML"));
    }

    @Test
    void should_generate_purchase_request_core_files() throws Exception {
        ModuleSpec moduleSpec = new ModuleSpecLoader().load(Path.of("src/main/resources/examples/purchase/purchase-request.yaml"));
        Path moduleRootDir = prepareModuleRoot("purchase");
        DddGenerationContext context = DddGenerationContext.create(moduleRootDir, moduleSpec, "full");
        List<DddFilePlan> plans = new DddModuleLayoutPlanner().plan(context);
        new CodeGenerator().generate(context, plans);

        assertTrue(Files.exists(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/purchase/admin/PurchaseRequestAdminController.java")));
        assertTrue(Files.exists(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/purchase/application/service/PurchaseRequestAdminAppService.java")));
        assertTrue(Files.exists(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/purchase/domain/model/PurchaseRequest.java")));
        assertTrue(Files.exists(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/purchase/infrastructure/persistence/po/PurchaseRequestPO.java")));
        assertTrue(Files.exists(moduleRootDir.resolve("src/main/resources/mapper/purchase/PurchaseRequestMapper.xml")));
    }

    @Test
    void should_generate_child_aggregate_without_admin_files() throws Exception {
        ModuleSpec moduleSpec = new ModuleSpecLoader().load(Path.of("src/main/resources/examples/purchase/purchase-inbound-item.yaml"));
        Path moduleRootDir = prepareModuleRoot("purchase");
        DddGenerationContext context = DddGenerationContext.create(moduleRootDir, moduleSpec, "full");
        List<DddFilePlan> plans = new DddModuleLayoutPlanner().plan(context);
        new CodeGenerator().generate(context, plans);

        assertFalse(Files.exists(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/purchase/admin/PurchaseInboundItemAdminController.java")));
        assertFalse(Files.exists(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/purchase/application/service/PurchaseInboundItemAdminAppService.java")));
        assertTrue(Files.exists(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/purchase/domain/model/PurchaseInboundItem.java")));
        assertTrue(Files.exists(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/purchase/infrastructure/persistence/po/PurchaseInboundItemPO.java")));
        assertTrue(Files.exists(moduleRootDir.resolve("src/main/resources/mapper/purchase/PurchaseInboundItemMapper.xml")));
        String mapperContent = Files.readString(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/purchase/infrastructure/persistence/mapper/PurchaseInboundItemMapper.java"));
        assertTrue(mapperContent.contains("@Mapper\npublic interface PurchaseInboundItemMapper"));
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
    void should_skip_existing_files_and_record_report() throws Exception {
        ModuleSpec moduleSpec = new ModuleSpecLoader().load(Path.of("src/main/resources/examples/purchase/purchase-request.yaml"));
        Path moduleRootDir = prepareModuleRoot("purchase");
        DddGenerationContext context = DddGenerationContext.create(moduleRootDir, moduleSpec, "full");
        List<DddFilePlan> plans = new DddModuleLayoutPlanner().plan(context);
        Path existingFile = moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/purchase/admin/PurchaseRequestAdminController.java");
        Files.createDirectories(existingFile.getParent());
        Files.writeString(existingFile, "existing", StandardCharsets.UTF_8);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        DddGenerationReport report;
        try {
            System.setOut(new PrintStream(byteArrayOutputStream, true, StandardCharsets.UTF_8));
            report = new CodeGenerator().generate(context, plans);
        } finally {
            System.setOut(originalOut);
        }

        assertTrue(report.skippedFiles().contains(existingFile));
        assertTrue(report.generatedFiles().size() > 0);
        assertEquals("existing", Files.readString(existingFile));
    }

    @Test
    void should_generate_purchase_request_crud_details() throws Exception {
        ModuleSpec moduleSpec = new ModuleSpecLoader().load(Path.of("src/main/resources/examples/purchase/purchase-request.yaml"));
        Path moduleRootDir = prepareModuleRoot("purchase");
        DddGenerationContext context = DddGenerationContext.create(moduleRootDir, moduleSpec, "full");
        List<DddFilePlan> plans = new DddModuleLayoutPlanner().plan(context);
        new CodeGenerator().generate(context, plans);

        String appServiceImpl = Files.readString(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/purchase/application/service/impl/PurchaseRequestAdminAppServiceImpl.java"));
        String repositoryImpl = Files.readString(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/purchase/infrastructure/persistence/repository/PurchaseRequestRepositoryImpl.java"));
        String mapperXml = Files.readString(moduleRootDir.resolve("src/main/resources/mapper/purchase/PurchaseRequestMapper.xml"));
        String conditionMapHelper = Files.readString(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/purchase/infrastructure/persistence/repository/ConditionMapHelper.java"));

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
        assertTrue(conditionMapHelper.contains("throw new BizException(key + \" contains invalid characters\");"));
    }

    private Path prepareModuleRoot(String moduleCode) throws Exception {
        Path moduleRootDir = Files.createTempDirectory("xbb-codegen-" + moduleCode + "-");
        Files.createDirectories(moduleRootDir.resolve("src/main/java/xbb/ai/erp/module/" + moduleCode));
        Files.createDirectories(moduleRootDir.resolve("src/main/resources"));
        return moduleRootDir;
    }
}
