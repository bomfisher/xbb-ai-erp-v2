package xbb.ai.erp.module.purchase.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderItemListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderItemSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderItemDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderItemListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderItemSaveItemVO;

public interface PurchaseOrderItemAdminAppService {
    ListBaseVO<PurchaseOrderItemListItemVO> list(PurchaseOrderItemListDTO dto);

    SaveItemVO<PurchaseOrderItemSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<PurchaseOrderItemSaveItemVO> updateItem(IdBaseDTO dto);

    Long save(PurchaseOrderItemSaveDTO dto);

    PurchaseOrderItemDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);
}
