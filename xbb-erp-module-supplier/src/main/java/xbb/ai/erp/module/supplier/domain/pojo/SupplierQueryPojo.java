package xbb.ai.erp.module.supplier.domain.pojo;

import lombok.Data;
import xbb.ai.erp.base.common.pojo.ListFilterCondition;

import java.util.List;

@Data
public class SupplierQueryPojo {
    private String corpid;
    private Long id;
    private String keyword;
    private String supplierCode;
    private String supplierName;
    private String supplierShortName;
    private String supplierCategory;
    private String mainBusinessCategory;
    private String ownerPurchaserId;
    private String bizStatus;
    private String refStatus;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private String groupByStr;
    private String orderByStr;
    private List<ListFilterCondition> conditions;
}
