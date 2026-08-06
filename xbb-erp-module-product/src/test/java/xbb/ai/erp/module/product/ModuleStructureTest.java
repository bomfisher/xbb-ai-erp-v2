package xbb.ai.erp.module.product;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ModuleStructureTest {

    @Test
    void should_expose_product_module_packages() {
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/product/admin")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/product/admin/dto")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/product/admin/vo")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/product/application/service")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/product/application/assembler")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/product/application/validator")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/product/application/port")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/product/domain/model")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/repository")));
    }
}
