package xbb.ai.erp.module.masterdata.domain.model;

import lombok.Data;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
public class Warehouse extends BaseEntity {
    private Long id;
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
