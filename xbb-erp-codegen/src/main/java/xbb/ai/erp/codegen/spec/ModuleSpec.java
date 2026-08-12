package xbb.ai.erp.codegen.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModuleSpec {
    private String moduleDir;
    private String moduleCode;
    private String moduleApiName;
    private String businessName;
    private String businessCode;
    private String moduleName;
    private String packageBase;
    private String pathStrategy = "ddd-mybatis-plus";
    private AggregateRoleEnum aggregateRole = AggregateRoleEnum.ROOT;
    private AggregateSpec aggregate = new AggregateSpec();
    private GenerateSpec generate = new GenerateSpec();
}
