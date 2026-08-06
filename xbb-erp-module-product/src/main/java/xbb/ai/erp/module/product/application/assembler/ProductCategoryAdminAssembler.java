package xbb.ai.erp.module.product.application.assembler;

import xbb.ai.erp.module.product.admin.dto.ProductCategoryMainDTO;
import xbb.ai.erp.module.product.admin.dto.ProductCategorySaveDTO;
import xbb.ai.erp.module.product.admin.vo.ProductCategoryDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductCategorySaveItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductCategoryVO;
import xbb.ai.erp.module.product.domain.model.ProductCategory;

public final class ProductCategoryAdminAssembler {

    private ProductCategoryAdminAssembler() {
    }

    public static ProductCategorySaveItemVO buildEmptySaveItemVO() {
        ProductCategorySaveItemVO saveItemVO = new ProductCategorySaveItemVO();
        saveItemVO.setMain(new ProductCategoryMainDTO());
        return saveItemVO;
    }

    public static ProductCategory toProductCategory(ProductCategorySaveDTO dto) {
        ProductCategory productCategory = new ProductCategory();
        ProductCategoryMainDTO main = dto.getMain();
        if (main != null) {
            productCategory.setId(main.getId());
            productCategory.setCorpid(main.getCorpid());
            productCategory.setCategoryCode(main.getCategoryCode());
            productCategory.setCategoryName(main.getCategoryName());
            productCategory.setParentId(main.getParentId());
            productCategory.setCategoryLevel(main.getCategoryLevel());
            productCategory.setSortNo(main.getSortNo());
            productCategory.setEnableStatus(main.getEnableStatus());
        }
        productCategory.setCorpid(dto.getCorpid());
        return productCategory;
    }

    public static ProductCategoryVO toVO(ProductCategory productCategory) {
        if (productCategory == null) {
            return null;
        }
        ProductCategoryVO vo = new ProductCategoryVO();
        vo.setId(productCategory.getId());
        vo.setCategoryCode(productCategory.getCategoryCode());
        vo.setCategoryName(productCategory.getCategoryName());
        vo.setParentId(productCategory.getParentId());
        vo.setCategoryLevel(productCategory.getCategoryLevel());
        vo.setSortNo(productCategory.getSortNo());
        vo.setEnableStatus(productCategory.getEnableStatus());
        return vo;
    }

    public static ProductCategorySaveItemVO toSaveItemVO(ProductCategory productCategory) {
        ProductCategorySaveItemVO vo = buildEmptySaveItemVO();
        if (productCategory == null) {
            return vo;
        }
        ProductCategoryMainDTO main = new ProductCategoryMainDTO();
        main.setId(productCategory.getId());
        main.setCorpid(productCategory.getCorpid());
        main.setCategoryCode(productCategory.getCategoryCode());
        main.setCategoryName(productCategory.getCategoryName());
        main.setParentId(productCategory.getParentId());
        main.setCategoryLevel(productCategory.getCategoryLevel());
        main.setSortNo(productCategory.getSortNo());
        main.setEnableStatus(productCategory.getEnableStatus());
        vo.setMain(main);
        return vo;
    }

    public static ProductCategoryDetailVO toDetailVO(ProductCategorySaveItemVO saveItemVO) {
        ProductCategoryDetailVO detailVO = new ProductCategoryDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }
}
