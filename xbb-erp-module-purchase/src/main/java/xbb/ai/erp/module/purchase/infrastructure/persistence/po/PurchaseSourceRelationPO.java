package xbb.ai.erp.module.purchase.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("purchase_source_relation")
public class PurchaseSourceRelationPO extends BaseEntity {
    private String corpid;
    private String sourceDocType;
    private Long sourceDocId;
    private Long sourceLineId;
    private String targetDocType;
    private Long targetDocId;
    private Long targetLineId;
    private java.math.BigDecimal sourceQty;
    private java.math.BigDecimal reservedQty;
    private java.math.BigDecimal executedQty;
    private java.math.BigDecimal closedQty;
    private java.math.BigDecimal reversedQty;
    private String relationStatus;
    private Integer version;
    private String creatorId;
    private String modifyId;
}
