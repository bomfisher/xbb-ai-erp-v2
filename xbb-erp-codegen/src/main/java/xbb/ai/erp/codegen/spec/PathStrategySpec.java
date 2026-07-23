package xbb.ai.erp.codegen.spec;

import lombok.Data;

import java.util.EnumMap;
import java.util.Map;

@Data
public class PathStrategySpec {
    private String strategyName;
    private String moduleRootPattern;
    private String basePackagePattern;
    private Map<FileSlotEnum, String> slots = new EnumMap<>(FileSlotEnum.class);
}
