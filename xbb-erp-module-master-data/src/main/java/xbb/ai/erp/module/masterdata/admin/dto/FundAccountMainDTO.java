package xbb.ai.erp.module.masterdata.admin.dto;

import lombok.Data;

@Data
public class FundAccountMainDTO {
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
