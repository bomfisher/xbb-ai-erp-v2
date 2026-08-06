package xbb.ai.erp.module.product.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.dto.ProductCategoryListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductCategorySaveDTO;
import xbb.ai.erp.module.product.admin.vo.ProductCategoryDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductCategorySaveItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductCategoryVO;

public interface ProductCategoryAdminAppService {

    ListBaseVO<ProductCategoryVO> list(ProductCategoryListDTO dto);

    SaveItemVO<ProductCategorySaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<ProductCategorySaveItemVO> updateItem(IdBaseDTO dto);

    Long save(ProductCategorySaveDTO dto);

    ProductCategoryDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);
}
