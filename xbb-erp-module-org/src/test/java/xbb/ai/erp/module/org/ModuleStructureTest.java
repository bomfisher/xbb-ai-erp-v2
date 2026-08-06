package xbb.ai.erp.module.org;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ModuleStructureTest {

    @Test
    void should_expose_org_module_packages() {
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/org/admin")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/org/admin/dto")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/org/admin/vo")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/org/application/assembler")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/org/application/pojo")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/org/application/service")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/org/admin/dto")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/org/admin/vo")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/org/domain/enums")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/org/domain/model")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/org/domain/repository")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/convertor")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/mapper")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/po")));
        assertTrue(Files.exists(Path.of("src/main/java/xbb/ai/erp/module/org/infrastructure/persistence/repository")));
    }
}
