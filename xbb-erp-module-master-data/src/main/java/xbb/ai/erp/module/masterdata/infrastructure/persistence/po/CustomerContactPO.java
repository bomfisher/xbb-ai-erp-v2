package xbb.ai.erp.module.masterdata.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("customer_contact")
public class CustomerContactPO extends BaseEntity {
    private String corpid;
    private Long customerId;
    private String contactName;
    private String mobile;
    private Integer defaultFlag;
    private String bizStatus;
    private String creatorId;
    private String modifyId;
}
