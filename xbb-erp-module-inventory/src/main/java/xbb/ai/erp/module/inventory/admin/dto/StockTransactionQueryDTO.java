package xbb.ai.erp.module.inventory.admin.dto;
import java.time.LocalDateTime;
import lombok.Data;
import xbb.ai.erp.base.common.dto.BaseDTO;
@Data public class StockTransactionQueryDTO extends BaseDTO { private LocalDateTime startAt; private LocalDateTime endAt; private Long warehouseId; private Long skuId; private String categoryName; private String specification; private String sourceType; private Integer pageNum; private Integer pageSize; }
