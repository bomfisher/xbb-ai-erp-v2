package xbb.ai.erp.module.product.admin.vo;

import lombok.Data;
import xbb.ai.erp.base.common.filed.FieldEntity;

import java.util.List;

@Data
public class WarehouseDetailVO {

    private List<FieldEntity> headList;
    private WarehouseSaveItemVO mainData;
}
