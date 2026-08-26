package xbb.ai.erp.module.sales.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class SalesInvoiceBusinessSelectQueryDTO extends BaseDTO {
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Long id;
    private Long customerId;
}
