package xbb.ai.erp.module.product.infrastructure.persistence.po;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductSkuPO {

    private Long id;
    private String corpid;
    private Long spuId;
    private String skuCode;
    private String skuName;
    private String mnemonicCode;
    private String mainBarcode;
    private String specSignature;
    private String specSnapshot;
    private Integer canPurchase;
    private Integer canSale;
    private Integer canInventory;
    private Integer canProduce;
    private Integer enableStatus;
    private Integer listingStatus;
    private BigDecimal weight;
    private BigDecimal volume;
    private String extJson;
    private Integer del;
    private Long addTime;
    private Long updateTime;
    private String creatorId;
    private String modifyId;
}
