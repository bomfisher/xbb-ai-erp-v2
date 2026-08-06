package xbb.ai.erp.codegen.spec;

import lombok.Data;

@Data
public class ModuleSpec {
    private String moduleCode;
    private String moduleName;
    private String packageBase;
    private String pathStrategy = "ddd-mybatis-plus";
    private AggregateRoleEnum aggregateRole = AggregateRoleEnum.ROOT;
    private AggregateSpec aggregate = new AggregateSpec();
    private GenerateSpec generate = new GenerateSpec();
}
