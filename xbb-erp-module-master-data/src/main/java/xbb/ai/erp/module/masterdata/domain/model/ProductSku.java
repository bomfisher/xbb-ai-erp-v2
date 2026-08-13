package xbb.ai.erp.module.masterdata.domain.model;

import java.math.BigDecimal;
import lombok.Data;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
public class ProductSku extends BaseEntity {
    private Long id;
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
