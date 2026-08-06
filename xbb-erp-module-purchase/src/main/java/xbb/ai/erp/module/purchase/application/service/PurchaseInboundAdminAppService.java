package xbb.ai.erp.module.purchase.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundSaveItemVO;

public interface PurchaseInboundAdminAppService {
    ListBaseVO<PurchaseInboundListItemVO> list(PurchaseInboundListDTO dto);

    SaveItemVO<PurchaseInboundSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<PurchaseInboundSaveItemVO> updateItem(IdBaseDTO dto);

    Long save(PurchaseInboundSaveDTO dto);

    PurchaseInboundDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);
}
