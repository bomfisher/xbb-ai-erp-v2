package xbb.ai.erp.module.purchase.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchasePendingTaskListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchasePendingTaskSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchasePendingTaskDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchasePendingTaskListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchasePendingTaskSaveItemVO;

public interface PurchasePendingTaskAdminAppService {
    ListBaseVO<PurchasePendingTaskListItemVO> list(PurchasePendingTaskListDTO dto);

    SaveItemVO<PurchasePendingTaskSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<PurchasePendingTaskSaveItemVO> updateItem(IdBaseDTO dto);

    Long save(PurchasePendingTaskSaveDTO dto);

    PurchasePendingTaskDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);
}
