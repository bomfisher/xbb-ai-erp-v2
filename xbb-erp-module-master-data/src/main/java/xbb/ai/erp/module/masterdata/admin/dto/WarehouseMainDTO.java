package xbb.ai.erp.module.masterdata.admin.dto;

import lombok.Data;

@Data
public class WarehouseMainDTO {
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
