package xbb.ai.erp.module.purchase.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.IdBaseDTO;

/**
 * 审核通过或免审提交后确认采购入库的请求。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseInboundConfirmDTO extends IdBaseDTO {
}
