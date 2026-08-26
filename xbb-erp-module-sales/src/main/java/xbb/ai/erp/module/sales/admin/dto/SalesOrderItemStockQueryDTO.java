package xbb.ai.erp.module.sales.admin.dto;

import lombok.Data;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
public class SalesOrderItemStockQueryDTO extends BaseDTO {
    private Long skuId;
    private Long warehouseId;
}
