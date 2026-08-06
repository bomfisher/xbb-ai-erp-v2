package xbb.ai.erp.codegen.cli;

import xbb.ai.erp.codegen.generator.CodeGenerator;
import xbb.ai.erp.codegen.generator.DddFilePlan;
import xbb.ai.erp.codegen.generator.DddGenerationContext;
import xbb.ai.erp.codegen.generator.DddGenerationReport;
import xbb.ai.erp.codegen.generator.DddModuleLayoutPlanner;
import xbb.ai.erp.codegen.spec.ModuleSpec;
import xbb.ai.erp.codegen.spec.ModuleSpecLoader;
import xbb.ai.erp.codegen.spec.SpecValidator;
import xbb.ai.erp.codegen.template.TemplateRenderer;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class CodegenCli {

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            printUsage();
            return;
        }
        String command = args[0];
        Path specPath = Path.of(args[1]);
        Path moduleRootDir = Path.of(args[2]);
        ModuleSpec moduleSpec = new ModuleSpecLoader().load(specPath);
        new SpecValidator().validate(moduleSpec);
        DddGenerationContext context = DddGenerationContext.create(moduleRootDir, moduleSpec, "full");
        List<DddFilePlan> plans = new DddModuleLayoutPlanner().plan(context);
        CodeGenerator codeGenerator = new CodeGenerator();
        if ("dry-run".equals(command)) {
            Map<String, String> pathMap = codeGenerator.dryRun(plans);
            System.out.println(new TemplateRenderer().renderDryRun(pathMap));
            return;
        }
        if ("generate".equals(command)) {
            DddGenerationReport report = codeGenerator.generate(context, plans);
            printReport(context, report);
            return;
        }
        printUsage();
    }

    private static void printReport(DddGenerationContext context, DddGenerationReport report) {
        System.out.println("moduleRootDir=" + context.moduleRootDir().toAbsolutePath());
        System.out.println("basePackage=" + context.basePackage());
        System.out.println("aggregateName=" + context.aggregateName());
        System.out.println("Created directories");
        report.createdDirectories().forEach(path -> System.out.println("  + " + path.toAbsolutePath()));
        System.out.println("Generated files");
        report.generatedFiles().forEach(path -> System.out.println("  + " + path.toAbsolutePath()));
        System.out.println("Skipped existing files");
        report.skippedFiles().forEach(path -> System.out.println("  - " + path.toAbsolutePath()));
        System.out.println("Failed files");
        report.failedFiles().forEach(path -> System.out.println("  ! " + path.toAbsolutePath()));
    }

    private static void printUsage() {
        System.out.println("用法:");
        System.out.println("  java -jar xbb-erp-codegen.jar dry-run <spec.yaml> <moduleRootDir>");
        System.out.println("  java -jar xbb-erp-codegen.jar generate <spec.yaml> <moduleRootDir>");
    }
}
