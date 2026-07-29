package xbb.ai.erp.module.common.admin.pojo;

import lombok.Data;

import java.util.List;

@Data
public class ListFilterCondition {
    private String attr;
    private String fieldType;
    private String symbol;
    private List<String> value;
}
