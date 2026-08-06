package xbb.ai.erp.module.purchase.application.service.save;

import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSubmitSaveDTO;

public interface PurchaseOrderSaveAppService {
    Long save(PurchaseOrderSaveDTO dto);

    BaseVO saveAndSubmit(PurchaseOrderSubmitSaveDTO dto);
}
