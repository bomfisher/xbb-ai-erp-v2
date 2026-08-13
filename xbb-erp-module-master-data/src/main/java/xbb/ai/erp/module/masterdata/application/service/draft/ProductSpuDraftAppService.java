package xbb.ai.erp.module.masterdata.application.service.draft;

import xbb.ai.erp.module.masterdata.admin.dto.ProductSpuDraftListDTO;
import xbb.ai.erp.module.masterdata.admin.dto.ProductSpuDraftLoadDTO;
import xbb.ai.erp.module.masterdata.admin.dto.ProductSpuDraftSaveDTO;
import xbb.ai.erp.module.masterdata.admin.vo.ProductSpuDraftDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.ProductSpuDraftListItemVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;

import java.util.List;

public interface ProductSpuDraftAppService {
    DraftSaveVO saveDraft(ProductSpuDraftSaveDTO dto);

    List<ProductSpuDraftListItemVO> draftList(ProductSpuDraftListDTO dto);

    ProductSpuDraftDetailVO loadDraft(ProductSpuDraftLoadDTO dto);
}
