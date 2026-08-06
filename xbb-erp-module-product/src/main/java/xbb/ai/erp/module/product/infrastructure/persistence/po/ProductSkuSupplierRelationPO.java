package xbb.ai.erp.module.product.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("product_sku_supplier_rel")
public class ProductSkuSupplierRelationPO {

    private Long id;
    private String corpid;
    private Long skuId;
    private Long supplierId;
    private BigDecimal purchasePrice;
    private Integer deliveryCycleDay;
    private BigDecimal minOrderQty;
    private String supplierSkuCode;
    private Integer defaultFlag;
    private Integer enableStatus;
    private String remark;
    private Integer version;
    private Integer del;
    private Long addTime;
    private Long updateTime;
    private String creatorId;
    private String modifyId;
}
