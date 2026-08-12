package xbb.ai.erp.module.masterdata.domain.pojo;

import lombok.Data;

@Data
public class WarehouseQueryPojo {
    private String corpid;
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private Long id;
    private String warehouseCode;
    private String warehouseName;
    private String ownerId;
    private Integer enabled;
}
