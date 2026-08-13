package xbb.ai.erp.module.masterdata.application.service;

import java.util.List;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.module.masterdata.admin.dto.ProductSelectQueryDTO;
import xbb.ai.erp.module.masterdata.admin.vo.ProductSelectOptionVO;

public interface ProductSelectAppService {
    List<ProductSelectOptionVO> quickSearch(ProductSelectQueryDTO dto);

    ListBaseVO<ProductSelectOptionVO> dialogSearch(ProductSelectQueryDTO dto);

    ProductSelectOptionVO getById(ProductSelectQueryDTO dto);
}
