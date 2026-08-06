package xbb.ai.erp.module.product.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.dto.ProductBrandListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductBrandSaveDTO;
import xbb.ai.erp.module.product.admin.vo.ProductBrandDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductBrandSaveItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductBrandVO;

public interface ProductBrandAdminAppService {

    ListBaseVO<ProductBrandVO> list(ProductBrandListDTO dto);

    SaveItemVO<ProductBrandSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<ProductBrandSaveItemVO> updateItem(IdBaseDTO dto);

    Long save(ProductBrandSaveDTO dto);

    ProductBrandDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);
}
