package xbb.ai.erp.module.sales.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class SalesInvoiceSaveDTO extends BaseDTO {
    private SalesInvoiceMainDTO main;
    private List<SalesInvoiceLineDTO> lines;
}
