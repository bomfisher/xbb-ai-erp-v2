package xbb.ai.erp.base.common.pojo;

import lombok.Data;

import java.util.List;

@Data
public class ListFilterCondition {
    private String attr;
    private String fieldType;
    private String symbol;
    private List<String> value;
}
