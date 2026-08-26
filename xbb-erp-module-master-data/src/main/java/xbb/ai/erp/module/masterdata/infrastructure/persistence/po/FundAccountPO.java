package xbb.ai.erp.module.masterdata.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("fund_account")
public class FundAccountPO extends BaseEntity {
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
