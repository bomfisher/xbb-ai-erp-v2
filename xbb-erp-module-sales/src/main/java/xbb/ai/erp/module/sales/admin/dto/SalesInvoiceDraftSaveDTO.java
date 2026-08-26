package xbb.ai.erp.module.sales.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SalesInvoiceDraftSaveDTO extends SalesInvoiceSaveDTO {
    private SalesInvoiceDraftMetaDTO draftMeta = new SalesInvoiceDraftMetaDTO();
}
