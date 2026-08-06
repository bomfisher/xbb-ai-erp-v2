package xbb.ai.erp.module.product.application.service.query;

import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.dto.ProductBusinessSelectQueryDTO;
import xbb.ai.erp.module.product.admin.dto.ProductListDTO;
import xbb.ai.erp.module.product.admin.vo.ProductBusinessSelectOptionVO;
import xbb.ai.erp.module.product.admin.vo.ProductDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductListItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductSaveItemVO;

import java.util.List;

public interface ProductQueryAppService {

    ListBaseVO<ProductListItemVO> list(ProductListDTO dto);

    SaveItemVO<ProductSaveItemVO> addItem();

    SaveItemVO<ProductSaveItemVO> updateItem(IdBaseDTO dto);

    List<ProductBusinessSelectOptionVO> businessSelectQuickSearch(ProductBusinessSelectQueryDTO dto);

    ListBaseVO<ProductBusinessSelectOptionVO> businessSelectDialogSearch(ProductBusinessSelectQueryDTO dto);

    ProductBusinessSelectOptionVO businessSelectGetById(ProductBusinessSelectQueryDTO dto);

    ProductDetailVO detail(IdBaseDTO dto);
}
