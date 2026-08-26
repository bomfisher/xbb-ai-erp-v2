package xbb.ai.erp.module.purchase.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseInvoiceBusinessSelectQueryDTO extends BaseDTO {
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Long id;
    private Long supplierId;
}
