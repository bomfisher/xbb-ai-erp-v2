package xbb.ai.erp.module.supplier.admin.vo;

import lombok.Data;

@Data
public class SupplierDraftListItemVO {
    private String draftCode;
    private String draftTitle;
    private String supplierName;
    private String supplierCode;
    private Long updatedTime;
}
