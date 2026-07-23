package xbb.ai.erp.module.supplier.domain.model;

import lombok.Data;

@Data
public class VendorAddress {
    private Long id;
    private String corpid;
    private Long vendorId;
    private String addressType;
    private String receiverName;
    private String receiverMobile;
    private String provinceCode;
    private String cityCode;
    private String districtCode;
    private String detailAddress;
    private String postalCode;
    private Integer defaultFlag;
    private String bizStatus;
    private String creatorId;
    private String modifyId;
    private Integer deleted;
    private Long addTime;
    private Long updateTime;
    private Integer version;
}
