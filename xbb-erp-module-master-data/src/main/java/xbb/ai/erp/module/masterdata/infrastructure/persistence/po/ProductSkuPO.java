package xbb.ai.erp.module.masterdata.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("product_sku")
public class ProductSkuPO extends BaseEntity {
    private String corpid;
    private Long spuId;
    private String skuCode;
    private String skuName;
    private String specification;
    private String unitName;
    private BigDecimal salePrice;
    private BigDecimal purchasePrice;
    private Integer enabled;
    private String remark;
    private String creatorId;
    private String modifyId;
}
