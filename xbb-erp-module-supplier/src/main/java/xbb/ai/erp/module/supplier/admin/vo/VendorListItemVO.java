package xbb.ai.erp.module.supplier.admin.vo;

import lombok.Data;

@Data
public class VendorListItemVO {
    private Long id;
    private String vendorCode;
    private String vendorName;
    private String vendorShortName;
    private String vendorCategory;
    private String mainBusinessCategory;
    private String ownerPurchaserNameSnapshot;
    private String bizStatus;
    private String refStatus;
    private Long addTime;
    private Long updateTime;
}
