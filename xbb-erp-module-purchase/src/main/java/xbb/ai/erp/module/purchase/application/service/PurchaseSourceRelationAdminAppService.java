package xbb.ai.erp.module.purchase.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseSourceRelationListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseSourceRelationSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseSourceRelationDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseSourceRelationListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseSourceRelationSaveItemVO;

public interface PurchaseSourceRelationAdminAppService {
    ListBaseVO<PurchaseSourceRelationListItemVO> list(PurchaseSourceRelationListDTO dto);

    SaveItemVO<PurchaseSourceRelationSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<PurchaseSourceRelationSaveItemVO> updateItem(IdBaseDTO dto);

    Long save(PurchaseSourceRelationSaveDTO dto);

    PurchaseSourceRelationDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);
}
