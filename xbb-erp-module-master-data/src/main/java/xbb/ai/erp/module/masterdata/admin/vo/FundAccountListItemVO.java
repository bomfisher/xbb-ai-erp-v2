package xbb.ai.erp.module.masterdata.admin.vo;

import lombok.Data;

@Data
public class FundAccountListItemVO {
    private String id;
    private String accountCode;
    private String accountName;
    private String currency;
    private String bankAccountNo;
    private String accountHolder;
    private String bankName;
    private String accountType;
    private String defaultFlag;
    private String enabled;
    private String remark;
    private String creatorId;
    private String modifyId;
}
