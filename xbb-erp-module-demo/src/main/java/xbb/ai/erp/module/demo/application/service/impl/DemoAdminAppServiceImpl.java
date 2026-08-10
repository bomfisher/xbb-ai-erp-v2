package xbb.ai.erp.module.demo.application.service.impl;

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
import xbb.ai.erp.module.demo.admin.dto.DemoBusinessSelectQueryDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoSaveDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoSubmitSaveDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoDraftSaveDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoDraftListDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoDraftLoadDTO;
import xbb.ai.erp.module.demo.admin.vo.DemoDetailVO;
import xbb.ai.erp.module.demo.admin.vo.DemoListItemVO;
import xbb.ai.erp.module.demo.admin.vo.DemoSaveItemVO;
import xbb.ai.erp.module.demo.admin.vo.DemoDraftListItemVO;
import xbb.ai.erp.module.demo.admin.vo.DemoDraftDetailVO;
import xbb.ai.erp.module.demo.admin.vo.DemoBusinessSelectOptionVO;
import xbb.ai.erp.module.demo.application.service.DemoAdminAppService;
import xbb.ai.erp.module.demo.application.service.draft.DemoDraftAppService;
import xbb.ai.erp.module.demo.application.service.query.DemoQueryAppServiceImpl;
import xbb.ai.erp.module.demo.application.service.save.DemoSaveAppServiceImpl;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DemoAdminAppServiceImpl implements DemoAdminAppService {

    private final DemoQueryAppServiceImpl queryService;
    private final DemoSaveAppServiceImpl saveService;
    private final DemoDraftAppService draftService;

    @Override
    public ListBaseVO<DemoListItemVO> list(ListBaseDTO dto) {
        return queryService.list(dto);
    }

    @Override
    public SaveItemVO<DemoSaveItemVO> addItem(BaseDTO dto) {
        return queryService.addItem(dto);
    }

    @Override
    public SaveItemVO<DemoSaveItemVO> updateItem(IdBaseDTO dto) {
        return queryService.updateItem(dto);
    }

    @Override
    public xbb.ai.erp.base.common.vo.DraftSaveVO saveDraft(DemoDraftSaveDTO dto) {
        return draftService.saveDraft(dto);
    }

    @Override
    public BaseVO saveAndSubmit(DemoSubmitSaveDTO dto) {
        return saveService.saveAndSubmit(dto);
    }

    @Override
    public List<DemoDraftListItemVO> draftList(DemoDraftListDTO dto) {
        return draftService.draftList(dto);
    }

    @Override
    public DemoDraftDetailVO loadDraft(DemoDraftLoadDTO dto) {
        return draftService.loadDraft(dto);
    }

    @Override
    public Long save(DemoSaveDTO dto) {
        return saveService.save(dto);
    }

    @Override
    public DemoDetailVO detail(IdBaseDTO dto) {
        return queryService.detail(dto);
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        saveService.delete(dto);
    }

    @Override
    public List<DemoBusinessSelectOptionVO> businessSelectQuickSearch(DemoBusinessSelectQueryDTO dto) {
        return queryService.businessSelectQuickSearch(dto);
    }

    @Override
    public ListBaseVO<DemoBusinessSelectOptionVO> businessSelectDialogSearch(DemoBusinessSelectQueryDTO dto) {
        return queryService.businessSelectDialogSearch(dto);
    }

    @Override
    public DemoBusinessSelectOptionVO businessSelectGetById(DemoBusinessSelectQueryDTO dto) {
        return queryService.businessSelectGetById(dto);
    }
}
