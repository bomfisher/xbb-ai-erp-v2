package xbb.ai.erp.module.customer;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ModuleStructureTest {

    @Test
    void should_expose_customer_module_packages() {
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/customer/admin")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/customer/admin/dto")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/customer/admin/vo")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/customer/application/service")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/customer/application/assembler")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/customer/domain/model")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/customer/infrastructure/persistence/repository")));
    }
}
