package xbb.ai.erp.module.settlement.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.settlement.admin.dto.PayableListDTO;
import xbb.ai.erp.module.settlement.admin.dto.PayableSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.PayableSubmitSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.PayableDraftSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.PayableDraftListDTO;
import xbb.ai.erp.module.settlement.admin.dto.PayableDraftLoadDTO;
import xbb.ai.erp.module.settlement.admin.vo.PayableDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.PayableListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.PayableSaveItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.settlement.admin.vo.PayableDraftListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.PayableDraftDetailVO;
import xbb.ai.erp.module.settlement.application.service.PayableAdminAppService;
import xbb.ai.erp.module.settlement.application.service.draft.PayableDraftAppService;
import xbb.ai.erp.module.settlement.application.service.query.PayableQueryAppServiceImpl;
import xbb.ai.erp.module.settlement.application.service.save.PayableSaveAppServiceImpl;
import xbb.ai.erp.module.settlement.admin.dto.SettlementSelectionFillDTO;
import xbb.ai.erp.module.settlement.admin.vo.SettlementSelectionFillVO;

import java.util.List;
@Service
@RequiredArgsConstructor
public class PayableAdminAppServiceImpl implements PayableAdminAppService {

    private final PayableQueryAppServiceImpl queryService;
    private final PayableSaveAppServiceImpl saveService;
    private final PayableDraftAppService draftService;

    @Override
    public ListBaseVO<PayableListItemVO> list(PayableListDTO dto) {
        return queryService.list(dto);
    }

    @Override
    public BaseVO audit(IdBaseDTO dto) { return saveService.audit(dto); }

    @Override
    public BaseVO unaudit(IdBaseDTO dto) { return saveService.unaudit(dto); }

    @Override
    public BaseVO voidPayable(IdBaseDTO dto) {
        return saveService.voidPayable(dto);
    }

    @Override
    public BaseVO redFlush(IdBaseDTO dto) {
        return saveService.redFlush(dto);
    }

    @Override
    public SaveItemVO<PayableSaveItemVO> addItem(BaseDTO dto) {
        return queryService.addItem(dto);
    }

    @Override
    public SaveItemVO<PayableSaveItemVO> updateItem(IdBaseDTO dto) {
        return queryService.updateItem(dto);
    }

    @Override
    public DraftSaveVO saveDraft(PayableDraftSaveDTO dto) {
        return draftService.saveDraft(dto);
    }

    @Override
    public BaseVO saveAndSubmit(PayableSubmitSaveDTO dto) {
        return saveService.saveAndSubmit(dto);
    }

    @Override
    public List<PayableDraftListItemVO> draftList(PayableDraftListDTO dto) {
        return draftService.draftList(dto);
    }

    @Override
    public PayableDraftDetailVO loadDraft(PayableDraftLoadDTO dto) {
        return draftService.loadDraft(dto);
    }

    @Override
    public Long save(PayableSaveDTO dto) {
        return saveService.save(dto);
    }

    @Override
    public PayableDetailVO detail(IdBaseDTO dto) {
        return queryService.detail(dto);
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        saveService.delete(dto);
    }

    @Override
    public SettlementSelectionFillVO selectionFill(SettlementSelectionFillDTO dto) {
        return queryService.selectionFill(dto);
    }
}
