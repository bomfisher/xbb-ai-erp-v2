package xbb.ai.erp.module.masterdata.application.pojo;

import lombok.Data;

@Data
public class FundAccountQueryPojo {
    private String corpid;
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private Long id;
    private String accountCode;
    private String accountName;
    private String currency;
    private String bankAccountNo;
    private String accountType;
    private Integer defaultFlag;
    private Integer enabled;
    private String creatorId;
    private String modifyId;
}
