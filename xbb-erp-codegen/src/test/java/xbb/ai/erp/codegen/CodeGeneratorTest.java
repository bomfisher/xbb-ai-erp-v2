package xbb.ai.erp.codegen;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.codegen.generator.CodeGenerator;
import xbb.ai.erp.codegen.spec.ModuleSpec;
import xbb.ai.erp.codegen.spec.ModuleSpecLoader;
import xbb.ai.erp.codegen.spec.PathStrategyLoader;
import xbb.ai.erp.codegen.spec.PathStrategySpec;
import xbb.ai.erp.codegen.spec.SpecValidator;

import java.nio.file.Files;
import java.nio.file.Path;
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
}
