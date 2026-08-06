package xbb.ai.erp.module.product.application.service.impl;

import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.dto.ProductBusinessSelectQueryDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftLoadDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftSaveDTO;
import xbb.ai.erp.module.product.admin.dto.ProductListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSubmitSaveDTO;
import xbb.ai.erp.module.product.admin.vo.ProductBusinessSelectOptionVO;
import xbb.ai.erp.module.product.admin.vo.ProductSaveItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductDraftDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductDraftListItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductDraftSaveVO;
import xbb.ai.erp.module.product.admin.vo.ProductListItemVO;
import xbb.ai.erp.module.product.application.service.ProductAdminAppService;
import xbb.ai.erp.module.product.application.service.delete.ProductDeleteAppService;
import xbb.ai.erp.module.product.application.service.draft.ProductDraftAppService;
import xbb.ai.erp.module.product.application.service.query.ProductQueryAppService;
import xbb.ai.erp.module.product.application.service.save.ProductSaveAppService;

import java.util.List;

@Service
public class ProductAdminAppServiceImpl implements ProductAdminAppService {

    private final ProductQueryAppService productQueryAppService;
    private final ProductDeleteAppService productDeleteAppService;
    private final ProductSaveAppService productSaveAppService;
    private final ProductDraftAppService productDraftAppService;

    public ProductAdminAppServiceImpl(ProductQueryAppService productQueryAppService, ProductDeleteAppService productDeleteAppService, ProductSaveAppService productSaveAppService, ProductDraftAppService productDraftAppService) {
        this.productQueryAppService = productQueryAppService;
        this.productDeleteAppService = productDeleteAppService;
        this.productSaveAppService = productSaveAppService;
        this.productDraftAppService = productDraftAppService;
    }

    @Override
    public ListBaseVO<ProductListItemVO> list(ProductListDTO dto) {
        return productQueryAppService.list(dto);
    }

    @Override
    public SaveItemVO<ProductSaveItemVO> addItem(BaseDTO dto) {
        return productQueryAppService.addItem();
    }

    @Override
    public SaveItemVO<ProductSaveItemVO> updateItem(IdBaseDTO dto) {
        return productQueryAppService.updateItem(dto);
    }

    @Override
    public ProductDraftSaveVO saveDraft(ProductDraftSaveDTO dto) {
        if (productDraftAppService == null) {
            return new ProductDraftSaveVO();
        }
        return productDraftAppService.saveDraft(dto);
    }

    @Override
    public BaseVO saveAndSubmit(ProductSubmitSaveDTO dto) {
        if (productSaveAppService == null) {
            return new BaseVO();
        }
        return productSaveAppService.saveAndSubmit(dto);
    }

    @Override
    public List<ProductDraftListItemVO> draftList(ProductDraftListDTO dto) {
        if (productDraftAppService == null) {
            return List.of();
        }
        return productDraftAppService.draftList(dto);
    }

    @Override
    public ProductDraftDetailVO loadDraft(ProductDraftLoadDTO dto) {
        if (productDraftAppService == null) {
            return new ProductDraftDetailVO();
        }
        return productDraftAppService.loadDraft(dto);
    }

    @Override
    public List<ProductBusinessSelectOptionVO> businessSelectQuickSearch(ProductBusinessSelectQueryDTO dto) {
        return productQueryAppService.businessSelectQuickSearch(dto);
    }

    @Override
    public ListBaseVO<ProductBusinessSelectOptionVO> businessSelectDialogSearch(ProductBusinessSelectQueryDTO dto) {
        return productQueryAppService.businessSelectDialogSearch(dto);
    }

    @Override
    public ProductBusinessSelectOptionVO businessSelectGetById(ProductBusinessSelectQueryDTO dto) {
        return productQueryAppService.businessSelectGetById(dto);
    }

    @Override
    public ProductDetailVO detail(IdBaseDTO dto) {
        return productQueryAppService.detail(dto);
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        productDeleteAppService.delete(dto);
    }

    public static ProductAdminAppServiceImpl forTesting(
        ProductQueryAppService productQueryAppService,
        ProductDeleteAppService productDeleteAppService,
        ProductSaveAppService productSaveAppService,
        ProductDraftAppService productDraftAppService
    ) {
        return new ProductAdminAppServiceImpl(productQueryAppService, productDeleteAppService, productSaveAppService, productDraftAppService);
    }
}
