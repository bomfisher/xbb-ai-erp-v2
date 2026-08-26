package xbb.ai.erp.module.sales.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class SalesInvoiceSourceQueryDTO extends BaseDTO {
    private String sourceType;
    private Long customerId;
    private Long sourceId;
    private String keyword;
}
