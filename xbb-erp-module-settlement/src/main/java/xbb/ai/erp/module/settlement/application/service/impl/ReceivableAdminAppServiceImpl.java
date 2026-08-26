package xbb.ai.erp.module.settlement.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableBusinessSelectQueryDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableSubmitSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableDraftSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableDraftListDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableDraftLoadDTO;
import xbb.ai.erp.module.settlement.admin.vo.ReceivableDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceivableListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceivableSaveItemVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceivableBusinessSelectOptionVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceivableDraftListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceivableDraftDetailVO;
import xbb.ai.erp.module.settlement.application.service.ReceivableAdminAppService;
import xbb.ai.erp.module.settlement.application.service.draft.ReceivableDraftAppService;
import xbb.ai.erp.module.settlement.application.service.query.ReceivableQueryAppServiceImpl;
import xbb.ai.erp.module.settlement.application.service.save.ReceivableSaveAppServiceImpl;
import xbb.ai.erp.module.settlement.admin.dto.SettlementSelectionFillDTO;
import xbb.ai.erp.module.settlement.admin.vo.SettlementSelectionFillVO;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ReceivableAdminAppServiceImpl implements ReceivableAdminAppService {

    private final ReceivableQueryAppServiceImpl queryService;
    private final ReceivableSaveAppServiceImpl saveService;
    private final ReceivableDraftAppService draftService;

    @Override
    public ListBaseVO<ReceivableListItemVO> list(ListBaseDTO dto) {
        return queryService.list(dto);
    }

    @Override
    public BaseVO audit(IdBaseDTO dto) { return saveService.audit(dto); }

    @Override
    public BaseVO unaudit(IdBaseDTO dto) { return saveService.unaudit(dto); }

    @Override
    public BaseVO voidReceivable(IdBaseDTO dto) {
        return saveService.voidReceivable(dto);
    }

    @Override
    public BaseVO redFlush(IdBaseDTO dto) {
        return saveService.redFlush(dto);
    }

    @Override
    public SaveItemVO<ReceivableSaveItemVO> addItem(BaseDTO dto) {
        return queryService.addItem(dto);
    }

    @Override
    public SaveItemVO<ReceivableSaveItemVO> updateItem(IdBaseDTO dto) {
        return queryService.updateItem(dto);
    }

    @Override
    public DraftSaveVO saveDraft(ReceivableDraftSaveDTO dto) {
        return draftService.saveDraft(dto);
    }

    @Override
    public BaseVO saveAndSubmit(ReceivableSubmitSaveDTO dto) {
        return saveService.saveAndSubmit(dto);
    }

    @Override
    public List<ReceivableDraftListItemVO> draftList(ReceivableDraftListDTO dto) {
        return draftService.draftList(dto);
    }

    @Override
    public ReceivableDraftDetailVO loadDraft(ReceivableDraftLoadDTO dto) {
        return draftService.loadDraft(dto);
    }

    @Override
    public Long save(ReceivableSaveDTO dto) {
        return saveService.save(dto);
    }

    @Override
    public ReceivableDetailVO detail(IdBaseDTO dto) {
        return queryService.detail(dto);
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        saveService.delete(dto);
    }

    @Override
    public List<ReceivableBusinessSelectOptionVO> businessSelectQuickSearch(ReceivableBusinessSelectQueryDTO dto) {
        return queryService.businessSelectQuickSearch(dto);
    }

    @Override
    public ListBaseVO<ReceivableBusinessSelectOptionVO> businessSelectDialogSearch(ReceivableBusinessSelectQueryDTO dto) {
        return queryService.businessSelectDialogSearch(dto);
    }

    @Override
    public ReceivableBusinessSelectOptionVO businessSelectGetById(ReceivableBusinessSelectQueryDTO dto) {
        return queryService.businessSelectGetById(dto);
    }

    @Override
    public SettlementSelectionFillVO selectionFill(SettlementSelectionFillDTO dto) {
        return queryService.selectionFill(dto);
    }
}
