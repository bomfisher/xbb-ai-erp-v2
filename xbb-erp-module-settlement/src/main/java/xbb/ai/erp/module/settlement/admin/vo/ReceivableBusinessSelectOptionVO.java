package xbb.ai.erp.module.settlement.admin.vo;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ReceivableBusinessSelectOptionVO {
    private Long id;
    private String code;
    private String name;
    private String label;
    private BigDecimal remainingAmount;
}
