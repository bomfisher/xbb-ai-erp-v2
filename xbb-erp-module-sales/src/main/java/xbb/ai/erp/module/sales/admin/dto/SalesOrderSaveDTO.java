package xbb.ai.erp.module.sales.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SalesOrderSaveDTO extends BaseDTO {
    private SalesOrderMainDTO main;
    private List<SalesOrderItemDTO> items = List.of();
}
