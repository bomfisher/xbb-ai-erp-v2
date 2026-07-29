package xbb.ai.erp.module.customer.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("customer")
public class CustomerPO extends BaseEntity {
    private String corpid;
    private String customerCode;
    private String customerName;
    private String customerShortName;
    private String customerCategory;
    private String regionCode;
    private String ownerSalesId;
    private String ownerSalesNameSnapshot;
    private String bizStatus;
    private String refStatus;
    private Long defaultContactId;
    private Long defaultAddressId;
    private Long defaultBankAccountId;
    private Long defaultInvoiceProfileId;
    private String remark;
    private String creatorId;
    private String modifyId;
    private Integer version;
}
