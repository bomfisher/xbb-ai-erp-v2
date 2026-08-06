package xbb.ai.erp.module.product.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("product_sku_supplier_rel_history")
public class ProductSkuSupplierRelationHistoryPO {

    private Long id;
    private String corpid;
    private Long relationId;
    private String operateType;
    private String operatorId;
    private String changeSnapshot;
    private String remark;
    private Integer del;
    private Long addTime;
    private Long updateTime;
    private String creatorId;
    private String modifyId;
}
