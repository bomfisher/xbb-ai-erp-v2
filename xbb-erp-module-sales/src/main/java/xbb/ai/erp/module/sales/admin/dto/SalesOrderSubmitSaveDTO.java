package xbb.ai.erp.module.sales.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SalesOrderSubmitSaveDTO extends SalesOrderSaveDTO {
    private SalesOrderDraftMetaDTO draftMeta = new SalesOrderDraftMetaDTO();
}
