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
import xbb.ai.erp.module.settlement.admin.dto.ReceiptSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptSubmitSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptDraftSaveDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptDraftListDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptDraftLoadDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptWriteOffDTO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptSaveItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptDraftListItemVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptDraftDetailVO;
import xbb.ai.erp.module.settlement.application.service.ReceiptAdminAppService;
import xbb.ai.erp.module.settlement.application.service.draft.ReceiptDraftAppService;
import xbb.ai.erp.module.settlement.application.service.query.ReceiptQueryAppServiceImpl;
import xbb.ai.erp.module.settlement.application.service.save.ReceiptSaveAppServiceImpl;
import xbb.ai.erp.module.settlement.application.service.ReceiptWriteOffService;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ReceiptAdminAppServiceImpl implements ReceiptAdminAppService {

    private final ReceiptQueryAppServiceImpl queryService;
    private final ReceiptSaveAppServiceImpl saveService;
    private final ReceiptDraftAppService draftService;
    private final ReceiptWriteOffService receiptWriteOffService;

    @Override
    public ListBaseVO<ReceiptListItemVO> list(ListBaseDTO dto) {
        return queryService.list(dto);
    }

    @Override
    public ListBaseVO<ReceiptListItemVO> listAdvanceReceipt(ListBaseDTO dto) {
        return queryService.listAdvanceReceipt(dto);
    }

    @Override
    public SaveItemVO<ReceiptSaveItemVO> addItem(BaseDTO dto) {
        return queryService.addItem(dto);
    }

    @Override
    public SaveItemVO<ReceiptSaveItemVO> addAdvanceReceipt(BaseDTO dto) {
        return queryService.addAdvanceReceipt(dto);
    }

    @Override
    public SaveItemVO<ReceiptSaveItemVO> updateItem(IdBaseDTO dto) {
        return queryService.updateItem(dto);
    }

    @Override
    public SaveItemVO<ReceiptSaveItemVO> updateAdvanceReceipt(IdBaseDTO dto) {
        return queryService.updateAdvanceReceipt(dto);
    }

    @Override
    public DraftSaveVO saveDraft(ReceiptDraftSaveDTO dto) {
        return draftService.saveDraft(dto);
    }

    @Override
    public BaseVO saveAndSubmit(ReceiptSubmitSaveDTO dto) {
        return saveService.saveAndSubmit(dto);
    }

    @Override
    public BaseVO saveAdvanceReceipt(ReceiptSubmitSaveDTO dto) {
        return saveService.saveAdvanceReceipt(dto);
    }

    @Override
    public List<ReceiptDraftListItemVO> draftList(ReceiptDraftListDTO dto) {
        return draftService.draftList(dto);
    }

    @Override
    public ReceiptDraftDetailVO loadDraft(ReceiptDraftLoadDTO dto) {
        return draftService.loadDraft(dto);
    }

    @Override
    public Long save(ReceiptSaveDTO dto) {
        return saveService.save(dto);
    }

    @Override
    public ReceiptDetailVO detail(IdBaseDTO dto) {
        return queryService.detail(dto);
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        saveService.delete(dto);
    }

    @Override
    public BaseVO writeOff(ReceiptWriteOffDTO dto) {
        return receiptWriteOffService.writeOff(dto);
    }

    @Override
    public BaseVO audit(IdBaseDTO dto) { return saveService.audit(dto); }

    @Override
    public BaseVO unaudit(IdBaseDTO dto) { return saveService.unaudit(dto); }

}
