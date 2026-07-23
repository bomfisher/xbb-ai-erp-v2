package xbb.ai.erp.module.supplier.domain.model;

import lombok.Data;

@Data
public class VendorBankAccount {
    private Long id;
    private String corpid;
    private Long vendorId;
    private String accountName;
    private String bankName;
    private String bankAccountNo;
    private String accountUsage;
    private Integer defaultFlag;
    private String bizStatus;
    private String creatorId;
    private String modifyId;
    private Integer deleted;
    private Long addTime;
    private Long updateTime;
    private Integer version;
}
