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
import xbb.ai.erp.module.masterdata.admin.dto.SupplierSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.SupplierSubmitSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.SupplierDraftSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.SupplierDraftListDTO;
import xbb.ai.erp.module.masterdata.admin.dto.SupplierDraftLoadDTO;
import xbb.ai.erp.module.masterdata.admin.dto.SupplierBusinessSelectQueryDTO;
import xbb.ai.erp.module.masterdata.admin.vo.SupplierDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.SupplierListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.SupplierSaveItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.SupplierDraftListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.SupplierDraftDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.SupplierBusinessSelectOptionVO;
import xbb.ai.erp.module.masterdata.application.service.SupplierAdminAppService;
import xbb.ai.erp.module.masterdata.application.service.draft.SupplierDraftAppService;
import xbb.ai.erp.module.masterdata.application.service.query.SupplierQueryAppServiceImpl;
import xbb.ai.erp.module.masterdata.application.service.save.SupplierSaveAppServiceImpl;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierAdminAppServiceImpl implements SupplierAdminAppService {

    private final SupplierQueryAppServiceImpl queryService;
    private final SupplierSaveAppServiceImpl saveService;
    private final SupplierDraftAppService draftService;

    @Override
    public ListBaseVO<SupplierListItemVO> list(ListBaseDTO dto) {
        return queryService.list(dto);
    }

    @Override
    public SaveItemVO<SupplierSaveItemVO> addItem(BaseDTO dto) {
        return queryService.addItem(dto);
    }

    @Override
    public SaveItemVO<SupplierSaveItemVO> updateItem(IdBaseDTO dto) {
        return queryService.updateItem(dto);
    }

    @Override
    public DraftSaveVO saveDraft(SupplierDraftSaveDTO dto) {
        return draftService.saveDraft(dto);
    }

    @Override
    public BaseVO saveAndSubmit(SupplierSubmitSaveDTO dto) {
        return saveService.saveAndSubmit(dto);
    }

    @Override
    public List<SupplierDraftListItemVO> draftList(SupplierDraftListDTO dto) {
        return draftService.draftList(dto);
    }

    @Override
    public SupplierDraftDetailVO loadDraft(SupplierDraftLoadDTO dto) {
        return draftService.loadDraft(dto);
    }

    @Override
    public Long save(SupplierSaveDTO dto) {
        return saveService.save(dto);
    }

    @Override
    public SupplierDetailVO detail(IdBaseDTO dto) {
        return queryService.detail(dto);
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        saveService.delete(dto);
    }

    @Override
    public List<SupplierBusinessSelectOptionVO> businessSelectQuickSearch(SupplierBusinessSelectQueryDTO dto) {
        return queryService.businessSelectQuickSearch(dto);
    }

    @Override
    public ListBaseVO<SupplierBusinessSelectOptionVO> businessSelectDialogSearch(SupplierBusinessSelectQueryDTO dto) {
        return queryService.businessSelectDialogSearch(dto);
    }

    @Override
    public SupplierBusinessSelectOptionVO businessSelectGetById(SupplierBusinessSelectQueryDTO dto) {
        return queryService.businessSelectGetById(dto);
    }
}
