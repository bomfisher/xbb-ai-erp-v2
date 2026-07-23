package xbb.ai.erp.module.purchase.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("purchase_source_relation")
public class PurchaseSourceRelationPO {
    private Long id;
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
    private Integer deleted;
    private Long addTime;
    private Long updateTime;
    private String creatorId;
    private String modifyId;
}
