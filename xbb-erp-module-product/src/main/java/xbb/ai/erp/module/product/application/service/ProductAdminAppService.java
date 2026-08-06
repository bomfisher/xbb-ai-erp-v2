package xbb.ai.erp.module.product.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductSaveItemVO;
import xbb.ai.erp.module.product.admin.dto.ProductBusinessSelectQueryDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftLoadDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftSaveDTO;
import xbb.ai.erp.module.product.admin.dto.ProductListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSubmitSaveDTO;
import xbb.ai.erp.module.product.admin.vo.ProductBusinessSelectOptionVO;
import xbb.ai.erp.module.product.admin.vo.ProductDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductDraftDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductDraftListItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductDraftSaveVO;
import xbb.ai.erp.module.product.admin.vo.ProductListItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductSaveItemVO;

import java.util.List;

public interface ProductAdminAppService {

    ListBaseVO<ProductListItemVO> list(ProductListDTO dto);

    SaveItemVO<ProductSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<ProductSaveItemVO> updateItem(IdBaseDTO dto);

    ProductDraftSaveVO saveDraft(ProductDraftSaveDTO dto);

    BaseVO saveAndSubmit(ProductSubmitSaveDTO dto);

    List<ProductDraftListItemVO> draftList(ProductDraftListDTO dto);

    ProductDraftDetailVO loadDraft(ProductDraftLoadDTO dto);

    List<ProductBusinessSelectOptionVO> businessSelectQuickSearch(ProductBusinessSelectQueryDTO dto);

    ListBaseVO<ProductBusinessSelectOptionVO> businessSelectDialogSearch(ProductBusinessSelectQueryDTO dto);

    ProductBusinessSelectOptionVO businessSelectGetById(ProductBusinessSelectQueryDTO dto);

    ProductDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);
}
