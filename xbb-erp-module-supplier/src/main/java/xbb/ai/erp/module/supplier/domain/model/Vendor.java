package xbb.ai.erp.module.supplier.domain.model;

import lombok.Data;

@Data
public class Vendor {
    private Long id;
    private String corpid;
    private String vendorCode;
    private String vendorName;
    private String vendorShortName;
    private String vendorCategory;
    private String mainBusinessCategory;
    private String ownerPurchaserId;
    private String ownerPurchaserNameSnapshot;
    private String bizStatus;
    private String refStatus;
    private Long defaultContactId;
    private Long defaultAddressId;
    private Long defaultBankAccountId;
    private Long defaultInvoiceProfileId;
    private String remark;
    private String creatorId;
    private String modifyId;
    private Integer version;
    private Integer deleted;
    private Long addTime;
    private Long updateTime;
}
