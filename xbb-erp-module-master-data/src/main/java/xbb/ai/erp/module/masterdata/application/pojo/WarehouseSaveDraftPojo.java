package xbb.ai.erp.module.masterdata.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.masterdata.admin.dto.WarehouseMainDTO;

@Data
public class WarehouseSaveDraftPojo {
    private String corpid;
    private String draftCode;
    private String draftTitle;
    private WarehouseMainDTO main;
    private Long updatedTime;
}
