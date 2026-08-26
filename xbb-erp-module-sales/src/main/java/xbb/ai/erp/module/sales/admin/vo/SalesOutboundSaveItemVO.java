package xbb.ai.erp.module.sales.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundMainDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundItemDTO;
import java.util.List;

@Data
public class SalesOutboundSaveItemVO {
    private SalesOutboundMainDTO main;
    private List<SalesOutboundItemDTO> items;
}
