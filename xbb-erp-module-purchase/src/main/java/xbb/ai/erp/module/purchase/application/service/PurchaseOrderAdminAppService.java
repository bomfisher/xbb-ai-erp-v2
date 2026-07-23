package xbb.ai.erp.module.purchase.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderSaveItemVO;

public interface PurchaseOrderAdminAppService {
    ListBaseVO<PurchaseOrderListItemVO> list(PurchaseOrderListDTO dto);

    SaveItemVO<PurchaseOrderSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<PurchaseOrderSaveItemVO> updateItem(IdBaseDTO dto);

    Long save(PurchaseOrderSaveDTO dto);

    PurchaseOrderDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);
}
