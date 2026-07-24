package xbb.ai.erp.codegen.cli;

import xbb.ai.erp.codegen.generator.CodeGenerator;
import xbb.ai.erp.codegen.spec.ModuleSpec;
import xbb.ai.erp.codegen.spec.ModuleSpecLoader;
import xbb.ai.erp.codegen.spec.PathStrategyLoader;
import xbb.ai.erp.codegen.spec.PathStrategySpec;
import xbb.ai.erp.codegen.spec.SpecValidator;
import xbb.ai.erp.codegen.template.TemplateRenderer;

import java.nio.file.Path;
import java.util.Map;

public class CodegenCli {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            printUsage();
            return;
        }
        String command = args[0];
        Path specPath = Path.of(args[1]);
        ModuleSpec moduleSpec = new ModuleSpecLoader().load(specPath);
        new SpecValidator().validate(moduleSpec);
        PathStrategySpec pathStrategySpec = new PathStrategyLoader().loadPreset(moduleSpec.getPathStrategy());
        CodeGenerator codeGenerator = new CodeGenerator();
        if ("dry-run".equals(command)) {
            Map<String, String> pathMap = codeGenerator.dryRun(moduleSpec, pathStrategySpec);
            System.out.println(new TemplateRenderer().renderDryRun(pathMap));
            return;
        }
        if ("generate".equals(command)) {
            Path rootPath = args.length >= 3 ? Path.of(args[2]) : Path.of(".");
            codeGenerator.generate(rootPath, moduleSpec, pathStrategySpec);
            System.out.println("生成完成: " + rootPath.toAbsolutePath());
            return;
        }
        printUsage();
    }

    private static void printUsage() {
        System.out.println("用法:");
        System.out.println("  java -jar xbb-erp-codegen.jar dry-run <spec.yaml>");
        System.out.println("  java -jar xbb-erp-codegen.jar generate <spec.yaml> [chu]");
    }
}
