package xbb.ai.erp.module.masterdata.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
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

import java.util.List;

public interface ProductSpuAdminAppService {
    ListBaseVO<ProductSpuListItemVO> list(ListBaseDTO dto);

    SaveItemVO<ProductSpuSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<ProductSpuSaveItemVO> updateItem(IdBaseDTO dto);

    DraftSaveVO saveDraft(ProductSpuDraftSaveDTO dto);

    BaseVO saveAndSubmit(ProductSpuSubmitSaveDTO dto);

    List<ProductSpuDraftListItemVO> draftList(ProductSpuDraftListDTO dto);

    ProductSpuDraftDetailVO loadDraft(ProductSpuDraftLoadDTO dto);

    Long save(ProductSpuSaveDTO dto);

    ProductSpuDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);
}
