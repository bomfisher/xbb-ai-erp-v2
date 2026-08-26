package xbb.ai.erp.module.sales.admin.vo;

import java.util.List;
import lombok.Data;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceLineDTO;

@Data
public class SalesInvoiceSourcePreviewVO {
    private Long customerId;
    private String sourceType;
    private Long sourceId;
    private List<SalesInvoiceLineDTO> lines;
}
