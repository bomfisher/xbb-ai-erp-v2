package xbb.ai.erp.module.inventory.admin.dto;

import lombok.Data;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
public class InstantStockQueryDTO extends BaseDTO {
    private Long skuId;
    private Long warehouseId;
}
