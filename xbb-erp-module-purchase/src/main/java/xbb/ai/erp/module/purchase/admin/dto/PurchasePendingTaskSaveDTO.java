package xbb.ai.erp.module.purchase.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class PurchasePendingTaskSaveDTO extends BaseDTO {
    private PurchasePendingTaskMainDTO main;
}
