package xbb.ai.erp.module.supplier.admin.vo;

import lombok.Data;

@Data
public class SupplierListItemVO {
    private Long id;
    private String supplierCode;
    private String supplierName;
    private String supplierShortName;
    private String supplierCategory;
    private String mainBusinessCategory;
    private String ownerPurchaserNameSnapshot;
    private String bizStatus;
    private String refStatus;
    private Long addTime;
    private Long updateTime;
}
