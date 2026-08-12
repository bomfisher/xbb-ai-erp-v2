package xbb.ai.erp.module.masterdata.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse")
public class WarehousePO extends BaseEntity {
    private String corpid;
    private String warehouseCode;
    private String warehouseName;
    private String address;
    private String ownerId;
    private Integer enabled;
    private String remark;
    private String creatorId;
    private String modifyId;
}
