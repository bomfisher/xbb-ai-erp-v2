package xbb.ai.erp.codegen.spec;

import java.util.ArrayList;
import java.util.List;

public class SpecValidator {

    public void validate(ModuleSpec moduleSpec) {
        List<String> errors = new ArrayList<>();
        if (isBlank(moduleSpec.getModuleCode())) {
            errors.add("moduleCode 不能为空");
        }
        if (isBlank(moduleSpec.getPackageBase())) {
            errors.add("packageBase 不能为空");
        }
        if (moduleSpec.getAggregate() == null) {
            errors.add("aggregate 不能为空");
        } else {
            if (isBlank(moduleSpec.getAggregate().getAggregateName())) {
                errors.add("aggregate.aggregateName 不能为空");
            }
            if (isBlank(moduleSpec.getAggregate().getTableName())) {
                errors.add("aggregate.tableName 不能为空");
            }
        }
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("；", errors));
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
