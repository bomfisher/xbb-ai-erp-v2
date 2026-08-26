package xbb.ai.erp.module.masterdata.admin.vo;

import lombok.Data;

@Data
public class WarehouseListItemVO {
    private Long id;
    private String warehouseCode;
    private String warehouseName;
    private String address;
    private String ownerId;
    private String enabled;
    private String remark;
    private String creatorId;
    private String modifyId;
}
