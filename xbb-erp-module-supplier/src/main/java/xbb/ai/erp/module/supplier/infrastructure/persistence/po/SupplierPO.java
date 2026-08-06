package xbb.ai.erp.module.supplier.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("supplier")
public class SupplierPO {
    private Long id;
    private String corpid;
    private String supplierCode;
    private String supplierName;
    private String supplierShortName;
    private String supplierCategory;
    private String mainBusinessCategory;
    private String ownerPurchaserId;
    private String ownerPurchaserNameSnapshot;
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
    private Integer del;
    private Long addTime;
    private Long updateTime;
}
