package xbb.ai.erp.codegen.generator;

import xbb.ai.erp.codegen.spec.ModuleSpec;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public record DddGenerationContext(
    Path moduleRootDir,
    Path javaSourceRoot,
    Path resourceRoot,
    String basePackage,
    ModuleSpec moduleSpec,
    String templateProfile
) {

    public static DddGenerationContext create(Path moduleRootDir, ModuleSpec moduleSpec, String templateProfile) throws IOException {
        Path javaSourceRoot = moduleRootDir.resolve("src/main/java");
        Path resourceRoot = moduleRootDir.resolve("src/main/resources");
        if (!Files.isDirectory(javaSourceRoot)) {
            throw new IllegalArgumentException("module root 缺少 src/main/java: " + moduleRootDir.toAbsolutePath());
        }
        if (!Files.isDirectory(resourceRoot)) {
            throw new IllegalArgumentException("module root 缺少 src/main/resources: " + moduleRootDir.toAbsolutePath());
        }
        return new DddGenerationContext(moduleRootDir, javaSourceRoot, resourceRoot, moduleSpec.getPackageBase(), moduleSpec, templateProfile);
    }

    public String aggregateName() {
        return moduleSpec.getAggregate().getAggregateName();
    }

    public String moduleCodeLower() {
        return moduleSpec.getModuleCode().toLowerCase();
    }

    public Path mapperXmlDir() {
        return resourceRoot.resolve("mapper").resolve(moduleCodeLower());
    }
}
