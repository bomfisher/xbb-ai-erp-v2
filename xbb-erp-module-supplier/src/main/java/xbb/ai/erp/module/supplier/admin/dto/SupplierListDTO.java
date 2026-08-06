package xbb.ai.erp.module.supplier.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.module.common.admin.pojo.ListFilterCondition;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SupplierListDTO extends BaseDTO {
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
