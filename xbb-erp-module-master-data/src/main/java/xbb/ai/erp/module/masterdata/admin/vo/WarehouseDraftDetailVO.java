package xbb.ai.erp.module.masterdata.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.masterdata.admin.dto.WarehouseMainDTO;

@Data
public class WarehouseDraftDetailVO {
    private String draftCode;
    private WarehouseMainDTO main;
}
