package xbb.ai.erp.module.sales.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.pojo.ListFilterCondition;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SalesOrderListDTO extends BaseDTO {
    private Long id;
    private String orderNo;
    private Long customerId;
    private Long orderDate;
    private Long deliveryDate;
    private java.math.BigDecimal totalAmount;
    private Integer status;
    private Integer auditStatus;
    private Integer outboundStatus;
    private Integer receiptStatus;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private String groupByStr;
    private String orderByStr;
    private List<ListFilterCondition> conditions;
}
