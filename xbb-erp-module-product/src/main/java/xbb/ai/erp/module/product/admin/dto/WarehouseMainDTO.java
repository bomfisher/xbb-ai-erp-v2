package xbb.ai.erp.module.product.admin.dto;

import lombok.Data;

@Data
public class WarehouseMainDTO {

    private Long id;
    private String corpid;
    private Long bizOrgId;
    private String warehouseCode;
    private String warehouseName;
    private String warehouseType;
    private Integer enableStatus;
    private String address;
    private String managerId;
    private String bizStatus;
    private String creatorId;
    private String modifyId;
    private Integer deleted;
    private Long addTime;
    private Long updateTime;
}
