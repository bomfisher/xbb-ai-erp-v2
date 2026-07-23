package xbb.ai.erp.module.purchase.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestItemListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestItemSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestItemDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestItemListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestItemSaveItemVO;

public interface PurchaseRequestItemAdminAppService {
    ListBaseVO<PurchaseRequestItemListItemVO> list(PurchaseRequestItemListDTO dto);

    SaveItemVO<PurchaseRequestItemSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<PurchaseRequestItemSaveItemVO> updateItem(IdBaseDTO dto);

    Long save(PurchaseRequestItemSaveDTO dto);

    PurchaseRequestItemDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);
}
