package xbb.ai.erp.module.purchase.admin.vo;

import lombok.Data;

@Data
public class PurchaseSourceRelationListItemVO {
    private Long id;
    private String sourceDocType;
    private Long sourceDocId;
    private String targetDocType;
    private Long targetDocId;
    private java.math.BigDecimal sourceQty;
    private java.math.BigDecimal reservedQty;
    private java.math.BigDecimal executedQty;
    private java.math.BigDecimal closedQty;
    private java.math.BigDecimal reversedQty;
    private String relationStatus;
    private Long addTime;
    private Long updateTime;
}
