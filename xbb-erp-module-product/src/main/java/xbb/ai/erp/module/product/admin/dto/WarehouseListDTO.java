package xbb.ai.erp.module.product.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class WarehouseListDTO extends BaseDTO {

    private Long id;
    private Long bizOrgId;
    private String warehouseCode;
    private String warehouseName;
    private String warehouseType;
    private Integer enableStatus;
    private String address;
    private String managerId;
    private String bizStatus;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private String groupByStr;
    private String orderByStr;
}
