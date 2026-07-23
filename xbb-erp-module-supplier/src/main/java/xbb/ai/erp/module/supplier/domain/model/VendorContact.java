package xbb.ai.erp.module.supplier.domain.model;

import lombok.Data;

@Data
public class VendorContact {
    private Long id;
    private String corpid;
    private Long vendorId;
    private String contactName;
    private String mobile;
    private String phone;
    private String email;
    private String positionName;
    private Integer defaultFlag;
    private String bizStatus;
    private String creatorId;
    private String modifyId;
    private Integer deleted;
    private Long addTime;
    private Long updateTime;
    private Integer version;
}
