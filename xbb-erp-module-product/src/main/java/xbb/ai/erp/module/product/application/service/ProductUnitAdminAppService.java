package xbb.ai.erp.module.product.application.service;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.dto.ProductUnitListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductUnitSaveDTO;
import xbb.ai.erp.module.product.admin.vo.ProductUnitDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductUnitSaveItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductUnitVO;

public interface ProductUnitAdminAppService {

    ListBaseVO<ProductUnitVO> list(ProductUnitListDTO dto);

    SaveItemVO<ProductUnitSaveItemVO> addItem(BaseDTO dto);

    SaveItemVO<ProductUnitSaveItemVO> updateItem(IdBaseDTO dto);

    Long save(ProductUnitSaveDTO dto);

    ProductUnitDetailVO detail(IdBaseDTO dto);

    void delete(BatchBaseDTO dto);
}
