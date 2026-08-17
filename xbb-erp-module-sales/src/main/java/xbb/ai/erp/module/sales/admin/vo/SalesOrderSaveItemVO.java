package xbb.ai.erp.module.sales.admin.vo;

import lombok.Data;
import java.util.List;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderMainDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderItemDTO;

@Data
public class SalesOrderSaveItemVO {
    private SalesOrderMainDTO main;
    private List<SalesOrderItemDTO> items = List.of();
}
