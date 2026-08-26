package xbb.ai.erp.module.sales.admin.vo;

import java.math.BigDecimal;
import java.util.Map;
import lombok.Data;

@Data
public class SalesOutboundSourceProductOptionVO {
    private Long id;
    private String code;
    private String name;
    private String label;
    private BigDecimal remainingQty;
    private Map<String, Object> linePatch;
}
