package xbb.ai.erp.module.purchase.admin.vo;

import java.util.Map;
import lombok.Data;

@Data
public class PurchaseInboundSelectionFillVO {
    private Long referenceId;
    private Map<String, Object> patch;
}
