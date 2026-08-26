package xbb.ai.erp.module.purchase.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseInvoiceSourceQueryDTO extends BaseDTO {
    private String sourceType;
    private Long supplierId;
    private Long sourceId;
    private String keyword;
}
