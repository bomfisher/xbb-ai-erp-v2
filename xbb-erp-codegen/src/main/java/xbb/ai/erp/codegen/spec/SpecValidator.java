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
        if (moduleSpec.getAggregateRole() == AggregateRoleEnum.ROOT) {
            requireRootRouteIdentity(moduleSpec, errors);
        }
        if (!isBlank(moduleSpec.getModuleApiName()) && !moduleSpec.getModuleApiName().matches("[a-z][A-Za-z0-9]*")) {
            errors.add("moduleApiName 必须是小驼峰");
        }
        if (!isBlank(moduleSpec.getBusinessName()) && !moduleSpec.getBusinessName().matches("[a-z][A-Za-z0-9]*")) {
            errors.add("businessName 必须是小驼峰");
        }
        if (!isBlank(moduleSpec.getBusinessCode()) && !moduleSpec.getBusinessCode().matches("[A-Z][A-Z0-9_]*")) {
            errors.add("businessCode 必须是显式的大写枚举值");
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

    private void requireRootRouteIdentity(ModuleSpec moduleSpec, List<String> errors) {
        if (isBlank(moduleSpec.getModuleApiName())) {
            errors.add("ROOT 规格的 moduleApiName 不能为空");
        }
        if (isBlank(moduleSpec.getBusinessName())) {
            errors.add("ROOT 规格的 businessName 不能为空");
        }
        if (isBlank(moduleSpec.getBusinessCode())) {
            errors.add("ROOT 规格的 businessCode 不能为空");
        }
    }
}
