package xbb.ai.erp.codegen.generator;

import xbb.ai.erp.codegen.template.TemplateType;

import java.nio.file.Path;

public record DddFilePlan(
    Path targetPath,
    String packageName,
    String className,
    TemplateType templateType,
    boolean skipIfExists
) {
}
