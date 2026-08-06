package xbb.ai.erp.module.product.application.service.draft;

import xbb.ai.erp.module.product.admin.dto.ProductDraftListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftLoadDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftSaveDTO;
import xbb.ai.erp.module.product.admin.vo.ProductDraftDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductDraftListItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductDraftSaveVO;

import java.util.List;

public interface ProductDraftAppService {

    ProductDraftSaveVO saveDraft(ProductDraftSaveDTO dto);

    List<ProductDraftListItemVO> draftList(ProductDraftListDTO dto);

    ProductDraftDetailVO loadDraft(ProductDraftLoadDTO dto);
}
