package xbb.ai.erp.codegen.strategy;

import xbb.ai.erp.codegen.spec.FileSlotEnum;
import xbb.ai.erp.codegen.spec.ModuleSpec;
import xbb.ai.erp.codegen.spec.PathStrategySpec;

import java.util.Locale;

public class PathResolver {

    public ResolvedPath resolve(ModuleSpec moduleSpec, PathStrategySpec pathStrategySpec, FileSlotEnum slot, String fileName) {
        String moduleRoot = applyCommonTokens(pathStrategySpec.getModuleRootPattern(), moduleSpec);
        String slotPattern = pathStrategySpec.getSlots().get(slot);
        if (slotPattern == null || slotPattern.isBlank()) {
            throw new IllegalArgumentException("未配置路径槽位: " + slot);
        }
        String relativeDir = applyCommonTokens(slotPattern, moduleSpec);
        String packageName = toPackageName(relativeDir);
        return new ResolvedPath(packageName, moduleRoot + "/" + relativeDir + "/" + fileName);
    }

    public ResolvedPath resolveMapperXml(ModuleSpec moduleSpec, PathStrategySpec pathStrategySpec, String fileName) {
        String moduleRoot = applyCommonTokens(pathStrategySpec.getModuleRootPattern(), moduleSpec);
        String slotPattern = pathStrategySpec.getSlots().get(FileSlotEnum.MAPPER_XML);
        String relativeDir = applyCommonTokens(slotPattern, moduleSpec);
        return new ResolvedPath("", moduleRoot + "/" + relativeDir + "/" + fileName);
    }

    private String applyCommonTokens(String pattern, ModuleSpec moduleSpec) {
        String aggregateName = moduleSpec.getAggregate().getAggregateName();
        String aggregateVariable = lowerCamel(aggregateName);
        return pattern
            .replace("{moduleCode}", moduleSpec.getModuleCode())
            .replace("{packageBasePath}", moduleSpec.getPackageBase().replace('.', '/'))
            .replace("{aggregateName}", aggregateName)
            .replace("{aggregateVariable}", aggregateVariable)
            .replace("{moduleCodeLower}", moduleSpec.getModuleCode().toLowerCase(Locale.ROOT));
    }

    private String toPackageName(String relativeDir) {
        String javaPrefix = "src/main/java/";
        if (!relativeDir.startsWith(javaPrefix)) {
            return "";
        }
        return relativeDir.substring(javaPrefix.length()).replace('/', '.');
    }

    private String lowerCamel(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }
        return Character.toLowerCase(value.charAt(0)) + value.substring(1);
    }
}
