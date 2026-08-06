package xbb.ai.erp.module.purchase.application.service.save;

import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestSubmitSaveDTO;

public interface PurchaseRequestSaveAppService {
    Long save(PurchaseRequestSaveDTO dto);

    BaseVO saveAndSubmit(PurchaseRequestSubmitSaveDTO dto);
}
