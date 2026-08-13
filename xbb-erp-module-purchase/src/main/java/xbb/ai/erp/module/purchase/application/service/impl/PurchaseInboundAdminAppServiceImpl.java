package xbb.ai.erp.module.purchase.application.service.impl;

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
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundSubmitSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundConfirmDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundDraftSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundDraftListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundDraftLoadDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundSaveItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundDraftListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundDraftDetailVO;
import xbb.ai.erp.module.purchase.application.service.PurchaseInboundAdminAppService;
import xbb.ai.erp.module.purchase.application.service.draft.PurchaseInboundDraftAppService;
import xbb.ai.erp.module.purchase.application.service.query.PurchaseInboundQueryAppServiceImpl;
import xbb.ai.erp.module.purchase.application.service.save.PurchaseInboundSaveAppServiceImpl;

import java.util.List;
@Service
@RequiredArgsConstructor
public class PurchaseInboundAdminAppServiceImpl implements PurchaseInboundAdminAppService {

    private final PurchaseInboundQueryAppServiceImpl queryService;
    private final PurchaseInboundSaveAppServiceImpl saveService;
    private final PurchaseInboundDraftAppService draftService;

    @Override
    public ListBaseVO<PurchaseInboundListItemVO> list(ListBaseDTO dto) {
        return queryService.list(dto);
    }

    @Override
    public SaveItemVO<PurchaseInboundSaveItemVO> addItem(BaseDTO dto) {
        return queryService.addItem(dto);
    }

    @Override
    public SaveItemVO<PurchaseInboundSaveItemVO> updateItem(IdBaseDTO dto) {
        return queryService.updateItem(dto);
    }

    @Override
    public DraftSaveVO saveDraft(PurchaseInboundDraftSaveDTO dto) {
        return draftService.saveDraft(dto);
    }

    @Override
    public BaseVO saveAndSubmit(PurchaseInboundSubmitSaveDTO dto) {
        return saveService.saveAndSubmit(dto);
    }

    @Override
    public BaseVO confirmInbound(PurchaseInboundConfirmDTO dto) {
        return saveService.confirmInbound(dto);
    }

    @Override
    public List<PurchaseInboundDraftListItemVO> draftList(PurchaseInboundDraftListDTO dto) {
        return draftService.draftList(dto);
    }

    @Override
    public PurchaseInboundDraftDetailVO loadDraft(PurchaseInboundDraftLoadDTO dto) {
        return draftService.loadDraft(dto);
    }

    @Override
    public Long save(PurchaseInboundSaveDTO dto) {
        return saveService.save(dto);
    }

    @Override
    public PurchaseInboundDetailVO detail(IdBaseDTO dto) {
        return queryService.detail(dto);
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        saveService.delete(dto);
    }
}
