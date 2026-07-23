package xbb.ai.erp.codegen.spec;

import lombok.Data;

@Data
public class FieldSpec {
    private String name;
    private String column;
    private String javaType;
    private Boolean primaryKey;
    private String comment;
    private Boolean queryable;
    private Boolean visibleInList;
    private Boolean visibleInDetail;
}
