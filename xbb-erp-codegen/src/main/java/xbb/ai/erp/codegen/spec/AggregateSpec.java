package xbb.ai.erp.codegen.spec;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AggregateSpec {
    private String aggregateName;
    private String tableName;
    private List<FieldSpec> fields = new ArrayList<>();
}
