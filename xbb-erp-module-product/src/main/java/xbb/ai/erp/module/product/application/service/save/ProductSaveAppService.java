package xbb.ai.erp.module.product.application.service.save;

import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.product.admin.dto.ProductSaveDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSubmitSaveDTO;

public interface ProductSaveAppService {

    Long save(ProductSaveDTO dto);

    BaseVO saveAndSubmit(ProductSubmitSaveDTO dto);
}
