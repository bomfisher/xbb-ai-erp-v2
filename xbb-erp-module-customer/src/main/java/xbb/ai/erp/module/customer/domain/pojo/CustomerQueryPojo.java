package xbb.ai.erp.module.customer.domain.pojo;

import lombok.Data;
import xbb.ai.erp.base.common.pojo.ListFilterCondition;

import java.util.List;

@Data
public class CustomerQueryPojo {
    private String corpid;
    private Long id;
    private String keyword;
    private String customerCode;
    private String customerName;
    private String customerCategory;
    private String bizStatus;
    private String refStatus;
    private String ownerSalesId;
    private Long excludeId;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private String groupByStr;
    private String orderByStr;
    private List<ListFilterCondition> conditions;
}
