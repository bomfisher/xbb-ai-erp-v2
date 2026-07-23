package xbb.ai.erp.module.customer.domain.model;

import lombok.Data;

@Data
public class CustomerBankAccount {
    private Long id;
    private String corpid;
    private Long customerId;
    private String accountName;
    private String bankName;
    private String accountNo;
    private String accountUsage;
    private Integer defaultFlag;
    private String bizStatus;
    private String remark;
    private String creatorId;
    private String modifyId;
    private Integer version;
    private Integer del;
    private Long addTime;
    private Long updateTime;
}
