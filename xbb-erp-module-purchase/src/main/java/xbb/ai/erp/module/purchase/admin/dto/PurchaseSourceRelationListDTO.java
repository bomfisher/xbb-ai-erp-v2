package xbb.ai.erp.module.purchase.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseSourceRelationListDTO extends BaseDTO {
    private Long id;
    private String corpid;
    private String sourceDocType;
    private Long sourceDocId;
    private Long sourceLineId;
    private String targetDocType;
    private Long targetDocId;
    private Long targetLineId;
    private String relationStatus;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private String groupByStr;
    private String orderByStr;
}
