package xbb.ai.erp.module.masterdata.application.service.impl;

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
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountSubmitSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountDraftSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountDraftListDTO;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountDraftLoadDTO;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountBusinessSelectQueryDTO;
import xbb.ai.erp.module.masterdata.admin.vo.FundAccountBusinessSelectOptionVO;
import xbb.ai.erp.module.masterdata.admin.vo.FundAccountDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.FundAccountListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.FundAccountSaveItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.masterdata.admin.vo.FundAccountDraftListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.FundAccountDraftDetailVO;
import xbb.ai.erp.module.masterdata.application.service.FundAccountAdminAppService;
import xbb.ai.erp.module.masterdata.application.service.draft.FundAccountDraftAppService;
import xbb.ai.erp.module.masterdata.application.service.query.FundAccountQueryAppServiceImpl;
import xbb.ai.erp.module.masterdata.application.service.save.FundAccountSaveAppServiceImpl;

import java.util.List;
@Service
@RequiredArgsConstructor
public class FundAccountAdminAppServiceImpl implements FundAccountAdminAppService {

    private final FundAccountQueryAppServiceImpl queryService;
    private final FundAccountSaveAppServiceImpl saveService;
    private final FundAccountDraftAppService draftService;

    @Override
    public ListBaseVO<FundAccountListItemVO> list(ListBaseDTO dto) {
        return queryService.list(dto);
    }

    @Override
    public SaveItemVO<FundAccountSaveItemVO> addItem(BaseDTO dto) {
        return queryService.addItem(dto);
    }

    @Override
    public SaveItemVO<FundAccountSaveItemVO> updateItem(IdBaseDTO dto) {
        return queryService.updateItem(dto);
    }

    @Override
    public DraftSaveVO saveDraft(FundAccountDraftSaveDTO dto) {
        return draftService.saveDraft(dto);
    }

    @Override
    public BaseVO saveAndSubmit(FundAccountSubmitSaveDTO dto) {
        return saveService.saveAndSubmit(dto);
    }

    @Override
    public List<FundAccountDraftListItemVO> draftList(FundAccountDraftListDTO dto) {
        return draftService.draftList(dto);
    }

    @Override
    public FundAccountDraftDetailVO loadDraft(FundAccountDraftLoadDTO dto) {
        return draftService.loadDraft(dto);
    }

    @Override
    public List<FundAccountBusinessSelectOptionVO> businessSelectQuickSearch(FundAccountBusinessSelectQueryDTO dto) {
        return queryService.businessSelectQuickSearch(dto);
    }

    @Override
    public ListBaseVO<FundAccountBusinessSelectOptionVO> businessSelectDialogSearch(
        FundAccountBusinessSelectQueryDTO dto) {
        return queryService.businessSelectDialogSearch(dto);
    }

    @Override
    public FundAccountBusinessSelectOptionVO businessSelectGetById(FundAccountBusinessSelectQueryDTO dto) {
        return queryService.businessSelectGetById(dto);
    }

    @Override
    public Long save(FundAccountSaveDTO dto) {
        return saveService.save(dto);
    }

    @Override
    public FundAccountDetailVO detail(IdBaseDTO dto) {
        return queryService.detail(dto);
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        saveService.delete(dto);
    }
}
