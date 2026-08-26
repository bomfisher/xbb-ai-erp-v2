package xbb.ai.erp.module.sales.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceMainDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceLineDTO;
import java.util.List;

@Data
public class SalesInvoiceSaveItemVO {
    private SalesInvoiceMainDTO main;
    private List<SalesInvoiceLineDTO> lines;
}
