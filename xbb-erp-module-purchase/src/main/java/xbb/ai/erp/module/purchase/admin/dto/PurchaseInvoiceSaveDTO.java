package xbb.ai.erp.module.purchase.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseInvoiceSaveDTO extends BaseDTO {
    private PurchaseInvoiceMainDTO main;
    private PurchaseInvoiceSourceSelectionDTO sourceSelection;
    private List<PurchaseInvoiceLineDTO> lines;
}
