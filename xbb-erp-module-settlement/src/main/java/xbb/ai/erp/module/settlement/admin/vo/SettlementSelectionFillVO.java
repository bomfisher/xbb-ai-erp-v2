package xbb.ai.erp.module.settlement.admin.vo;

import java.util.Map;
import lombok.Data;

@Data
public class SettlementSelectionFillVO {
    private Long referenceId;
    private Map<String, Object> patch;
}
