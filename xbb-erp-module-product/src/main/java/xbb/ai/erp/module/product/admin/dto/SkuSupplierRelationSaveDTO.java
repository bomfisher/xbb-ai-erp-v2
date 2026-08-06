package xbb.ai.erp.module.product.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class SkuSupplierRelationSaveDTO extends BaseDTO {

    private Long id;
    private Long skuId;
    private Long supplierId;
    private BigDecimal purchasePrice;
    private Integer deliveryCycleDay;
    private BigDecimal minOrderQty;
    private String supplierSkuCode;
    private Integer defaultFlag;
    private Integer enableStatus;
    private String remark;
}
