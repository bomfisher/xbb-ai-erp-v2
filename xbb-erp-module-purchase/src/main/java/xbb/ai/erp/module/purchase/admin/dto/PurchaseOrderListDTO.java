package xbb.ai.erp.module.purchase.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.pojo.ListFilterCondition;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseOrderListDTO extends BaseDTO {
    private Long id;
    private String orderNo;
    private Long supplierId;
    private Long orderDate;
    private Long expectedDate;
    private java.math.BigDecimal totalAmount;
    private String status;
    private String remark;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private String groupByStr;
    private String orderByStr;
    private List<ListFilterCondition> conditions;
}
