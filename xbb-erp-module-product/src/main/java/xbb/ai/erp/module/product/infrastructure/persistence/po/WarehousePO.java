package xbb.ai.erp.module.product.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("warehouse")
public class WarehousePO {

    private Long id;
    private String corpid;
    private Long bizOrgId;
    private String warehouseCode;
    private String warehouseName;
    private String warehouseType;
    private Integer enableStatus;
    private String address;
    private String managerId;
    private String bizStatus;
    private String creatorId;
    private String modifyId;
    private Integer del;
    private Long addTime;
    private Long updateTime;
}
