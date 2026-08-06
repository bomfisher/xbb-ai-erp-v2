package xbb.ai.erp.module.supplier.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("supplier_bank_account")
public class SupplierBankAccountPO {
    private Long id;
    private String corpid;
    private Long supplierId;
    private String accountName;
    private String bankName;
    private String accountNo;
    private String accountUsage;
    private Integer defaultFlag;
    private String bizStatus;
    private String remark;
    private String creatorId;
    private String modifyId;
    private Integer del;
    private Long addTime;
    private Long updateTime;
    private Integer version;
}
