package xbb.ai.erp.codegen.generator;

import xbb.ai.erp.codegen.template.TemplateRenderer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CodeGenerator {

    private final TemplateRenderer templateRenderer = new TemplateRenderer();

    public Map<String, String> dryRun(List<DddFilePlan> plans) {
        Map<String, String> pathMap = new LinkedHashMap<>();
        for (DddFilePlan plan : plans) {
            pathMap.put(plan.templateType().name(), plan.targetPath().toString());
        }
        return pathMap;
    }

    public DddGenerationReport generate(DddGenerationContext context, List<DddFilePlan> plans) throws IOException {
        DddGenerationReport report = new DddGenerationReport();
        for (DddFilePlan plan : plans) {
            write(plan, context, report);
        }
        return report;
    }

    private void write(DddFilePlan plan, DddGenerationContext context, DddGenerationReport report) throws IOException {
        Path parent = plan.targetPath().getParent();
        if (parent != null && Files.notExists(parent)) {
            Files.createDirectories(parent);
            report.addCreatedDirectory(parent);
        }
        if (plan.skipIfExists() && Files.exists(plan.targetPath())) {
            report.addSkippedFile(plan.targetPath());
            return;
        }
        String content = templateRenderer.render(plan.templateType(), context);
        Files.writeString(plan.targetPath(), content, StandardCharsets.UTF_8);
        report.addGeneratedFile(plan.targetPath());
    }
}
