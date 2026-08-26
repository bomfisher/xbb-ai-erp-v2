package xbb.ai.erp.module.sales.admin.vo;

import java.util.Map;
import lombok.Data;

@Data
public class SalesOutboundSelectionFillVO {
    private Long referenceId;
    private Map<String, Object> patch;
}
