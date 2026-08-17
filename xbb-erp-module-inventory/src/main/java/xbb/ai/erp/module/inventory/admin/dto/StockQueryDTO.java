package xbb.ai.erp.module.inventory.admin.dto;

import java.math.BigDecimal;
import lombok.Data;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
public class StockQueryDTO extends BaseDTO {
    private Long skuId; private Long warehouseId; private String categoryName; private String specification;
    private BigDecimal minQty; private BigDecimal maxQty; private BigDecimal minAvailableQty; private BigDecimal maxAvailableQty;
    private Integer showZero; private Integer showDisabled; private Integer pageNum; private Integer pageSize;
}
