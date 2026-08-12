package xbb.ai.erp.module.masterdata.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("supplier")
public class SupplierPO extends BaseEntity {
    private String corpid;
    private String supplierCode;
    private String supplierName;
    private Long defaultContactId;
    private String mobile;
    private String address;
    private Integer enabled;
    private String remark;
    private String creatorId;
    private String modifyId;
}
