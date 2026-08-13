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
import xbb.ai.erp.module.masterdata.admin.dto.ProductSpuSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.ProductSpuSubmitSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.ProductSpuDraftSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.ProductSpuDraftListDTO;
import xbb.ai.erp.module.masterdata.admin.dto.ProductSpuDraftLoadDTO;
import xbb.ai.erp.module.masterdata.admin.vo.ProductSpuDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.ProductSpuListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.ProductSpuSaveItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.masterdata.admin.vo.ProductSpuDraftListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.ProductSpuDraftDetailVO;
import xbb.ai.erp.module.masterdata.application.service.ProductSpuAdminAppService;
import xbb.ai.erp.module.masterdata.application.service.draft.ProductSpuDraftAppService;
import xbb.ai.erp.module.masterdata.application.service.query.ProductSpuQueryAppServiceImpl;
import xbb.ai.erp.module.masterdata.application.service.save.ProductSpuSaveAppServiceImpl;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ProductSpuAdminAppServiceImpl implements ProductSpuAdminAppService {

    private final ProductSpuQueryAppServiceImpl queryService;
    private final ProductSpuSaveAppServiceImpl saveService;
    private final ProductSpuDraftAppService draftService;

    @Override
    public ListBaseVO<ProductSpuListItemVO> list(ListBaseDTO dto) {
        return queryService.list(dto);
    }

    @Override
    public SaveItemVO<ProductSpuSaveItemVO> addItem(BaseDTO dto) {
        return queryService.addItem(dto);
    }

    @Override
    public SaveItemVO<ProductSpuSaveItemVO> updateItem(IdBaseDTO dto) {
        return queryService.updateItem(dto);
    }

    @Override
    public DraftSaveVO saveDraft(ProductSpuDraftSaveDTO dto) {
        return draftService.saveDraft(dto);
    }

    @Override
    public BaseVO saveAndSubmit(ProductSpuSubmitSaveDTO dto) {
        return saveService.saveAndSubmit(dto);
    }

    @Override
    public List<ProductSpuDraftListItemVO> draftList(ProductSpuDraftListDTO dto) {
        return draftService.draftList(dto);
    }

    @Override
    public ProductSpuDraftDetailVO loadDraft(ProductSpuDraftLoadDTO dto) {
        return draftService.loadDraft(dto);
    }

    @Override
    public Long save(ProductSpuSaveDTO dto) {
        return saveService.save(dto);
    }

    @Override
    public ProductSpuDetailVO detail(IdBaseDTO dto) {
        return queryService.detail(dto);
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        saveService.delete(dto);
    }
}
