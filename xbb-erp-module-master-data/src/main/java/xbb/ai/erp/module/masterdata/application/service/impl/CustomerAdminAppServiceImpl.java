package xbb.ai.erp.module.masterdata.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerBusinessSelectQueryDTO;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerSubmitSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerDraftSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerDraftListDTO;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerDraftLoadDTO;
import xbb.ai.erp.module.masterdata.admin.vo.CustomerDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.CustomerListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.CustomerSaveItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.CustomerDraftListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.CustomerDraftDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.CustomerBusinessSelectOptionVO;
import xbb.ai.erp.module.masterdata.application.service.CustomerAdminAppService;
import xbb.ai.erp.module.masterdata.application.service.draft.CustomerDraftAppService;
import xbb.ai.erp.module.masterdata.application.service.query.CustomerQueryAppServiceImpl;
import xbb.ai.erp.module.masterdata.application.service.save.CustomerSaveAppServiceImpl;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerAdminAppServiceImpl implements CustomerAdminAppService {

    private final CustomerQueryAppServiceImpl queryService;
    private final CustomerSaveAppServiceImpl saveService;
    private final CustomerDraftAppService draftService;

    @Override
    public ListBaseVO<CustomerListItemVO> list(ListBaseDTO dto) {
        return queryService.list(dto);
    }

    @Override
    public SaveItemVO<CustomerSaveItemVO> addItem(BaseDTO dto) {
        return queryService.addItem(dto);
    }

    @Override
    public SaveItemVO<CustomerSaveItemVO> updateItem(IdBaseDTO dto) {
        return queryService.updateItem(dto);
    }

    @Override
    public DraftSaveVO saveDraft(CustomerDraftSaveDTO dto) {
        return draftService.saveDraft(dto);
    }

    @Override
    public BaseVO saveAndSubmit(CustomerSubmitSaveDTO dto) {
        return saveService.saveAndSubmit(dto);
    }

    @Override
    public List<CustomerDraftListItemVO> draftList(CustomerDraftListDTO dto) {
        return draftService.draftList(dto);
    }

    @Override
    public CustomerDraftDetailVO loadDraft(CustomerDraftLoadDTO dto) {
        return draftService.loadDraft(dto);
    }

    @Override
    public List<CustomerBusinessSelectOptionVO> businessSelectQuickSearch(CustomerBusinessSelectQueryDTO dto) {
        return queryService.businessSelectQuickSearch(dto);
    }

    @Override
    public ListBaseVO<CustomerBusinessSelectOptionVO> businessSelectDialogSearch(CustomerBusinessSelectQueryDTO dto) {
        return queryService.businessSelectDialogSearch(dto);
    }

    @Override
    public CustomerBusinessSelectOptionVO businessSelectGetById(CustomerBusinessSelectQueryDTO dto) {
        return queryService.businessSelectGetById(dto);
    }

    @Override
    public Long save(CustomerSaveDTO dto) {
        return saveService.save(dto);
    }

    @Override
    public CustomerDetailVO detail(IdBaseDTO dto) {
        return queryService.detail(dto);
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        saveService.delete(dto);
    }
}
