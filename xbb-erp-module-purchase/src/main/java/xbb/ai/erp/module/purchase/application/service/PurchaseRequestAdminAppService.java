package xbb.ai.erp.module.purchase.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestSaveItemVO;

public interface PurchaseRequestAdminAppService {
    ListBaseVO<PurchaseRequestListItemVO> list(PurchaseRequestListDTO dto);

    SaveItemVO<PurchaseRequestSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<PurchaseRequestSaveItemVO> updateItem(IdBaseDTO dto);

    Long save(PurchaseRequestSaveDTO dto);

    PurchaseRequestDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);
}
