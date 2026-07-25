package xbb.ai.erp.module.product.admin.vo;

import lombok.Data;

@Data
public class WarehouseListItemVO {

    private Long id;
    private Long bizOrgId;
    private String warehouseCode;
    private String warehouseName;
    private String warehouseType;
    private Integer enableStatus;
    private String address;
    private String managerId;
    private String bizStatus;
    private Long addTime;
    private Long updateTime;
}
