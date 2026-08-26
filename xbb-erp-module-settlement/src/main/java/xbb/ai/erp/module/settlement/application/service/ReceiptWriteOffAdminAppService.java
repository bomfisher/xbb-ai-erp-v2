package xbb.ai.erp.module.settlement.application.service;

import java.util.List;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptWriteOffSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptWriteOffSourceQueryDTO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptWriteOffListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptWriteOffSaveItemVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptWriteOffSourceVO;

public interface ReceiptWriteOffAdminAppService {
    ListBaseVO<ReceiptWriteOffListItemVO> list(ListBaseDTO dto);
    SaveItemVO<ReceiptWriteOffSaveItemVO> addItem(BaseDTO dto);
    BaseVO saveAndSubmit(ReceiptWriteOffSaveDTO dto);
    List<ReceiptWriteOffSourceVO> findAdvanceSources(ReceiptWriteOffSourceQueryDTO dto);
    List<ReceiptWriteOffSourceVO> findReceivableSources(ReceiptWriteOffSourceQueryDTO dto);
    BaseVO reverse(IdBaseDTO dto);
}
