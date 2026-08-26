package xbb.ai.erp.module.masterdata.domain.model;

import lombok.Data;

@Data
public class FundAccount {
    private Long id;
    private String corpid;
    private String accountCode;
    private String accountName;
    private String currency;
    private String bankAccountNo;
    private String accountHolder;
    private String bankName;
    private String accountType;
    private Integer defaultFlag;
    private Integer enabled;
    private String remark;
    private String creatorId;
    private String modifyId;
}
