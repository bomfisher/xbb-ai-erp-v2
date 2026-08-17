package xbb.ai.erp.module.sales.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.pojo.ListFilterCondition;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SalesOutboundListDTO extends BaseDTO {
    private Long id;
    private String outboundNo;
    private Long salesOrderId;
    private Long customerId;
    private Long warehouseId;
    private Long outboundDate;
    private java.math.BigDecimal totalAmount;
    private String status;
    private Integer auditStatus;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private String groupByStr;
    private String orderByStr;
    private List<ListFilterCondition> conditions;
}
