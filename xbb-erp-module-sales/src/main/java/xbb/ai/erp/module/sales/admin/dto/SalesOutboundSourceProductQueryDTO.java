package xbb.ai.erp.module.sales.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class SalesOutboundSourceProductQueryDTO extends BaseDTO {
    private Long salesOrderId;
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Long id;
}
