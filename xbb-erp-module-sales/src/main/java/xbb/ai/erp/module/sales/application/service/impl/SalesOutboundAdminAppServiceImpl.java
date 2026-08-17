package xbb.ai.erp.module.sales.application.service.impl;

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
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundSaveDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundSubmitSaveDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundDraftSaveDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundDraftListDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundDraftLoadDTO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundDetailVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundListItemVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundSaveItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundDraftListItemVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundDraftDetailVO;
import xbb.ai.erp.module.sales.application.service.SalesOutboundAdminAppService;
import xbb.ai.erp.module.sales.application.service.draft.SalesOutboundDraftAppService;
import xbb.ai.erp.module.sales.application.service.query.SalesOutboundQueryAppServiceImpl;
import xbb.ai.erp.module.sales.application.service.save.SalesOutboundSaveAppServiceImpl;

import java.util.List;
@Service
@RequiredArgsConstructor
public class SalesOutboundAdminAppServiceImpl implements SalesOutboundAdminAppService {

    private final SalesOutboundQueryAppServiceImpl queryService;
    private final SalesOutboundSaveAppServiceImpl saveService;
    private final SalesOutboundDraftAppService draftService;

    @Override
    public ListBaseVO<SalesOutboundListItemVO> list(ListBaseDTO dto) {
        return queryService.list(dto);
    }

    @Override
    public SaveItemVO<SalesOutboundSaveItemVO> addItem(BaseDTO dto) {
        return queryService.addItem(dto);
    }

    @Override
    public SaveItemVO<SalesOutboundSaveItemVO> updateItem(IdBaseDTO dto) {
        return queryService.updateItem(dto);
    }

    @Override
    public DraftSaveVO saveDraft(SalesOutboundDraftSaveDTO dto) {
        return draftService.saveDraft(dto);
    }

    @Override
    public BaseVO saveAndSubmit(SalesOutboundSubmitSaveDTO dto) {
        return saveService.saveAndSubmit(dto);
    }

    @Override
    public List<SalesOutboundDraftListItemVO> draftList(SalesOutboundDraftListDTO dto) {
        return draftService.draftList(dto);
    }

    @Override
    public SalesOutboundDraftDetailVO loadDraft(SalesOutboundDraftLoadDTO dto) {
        return draftService.loadDraft(dto);
    }

    @Override
    public Long save(SalesOutboundSaveDTO dto) {
        return saveService.save(dto);
    }

    @Override
    public SalesOutboundDetailVO detail(IdBaseDTO dto) {
        return queryService.detail(dto);
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        saveService.delete(dto);
    }
}
