package xbb.ai.erp.module.product.application.assembler;

import xbb.ai.erp.module.product.admin.dto.ProductBrandMainDTO;
import xbb.ai.erp.module.product.admin.dto.ProductBrandSaveDTO;
import xbb.ai.erp.module.product.admin.vo.ProductBrandDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductBrandSaveItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductBrandVO;
import xbb.ai.erp.module.product.domain.model.ProductBrand;

public final class ProductBrandAdminAssembler {

    private ProductBrandAdminAssembler() {
    }

    public static ProductBrandSaveItemVO buildEmptySaveItemVO() {
        ProductBrandSaveItemVO saveItemVO = new ProductBrandSaveItemVO();
        saveItemVO.setMain(new ProductBrandMainDTO());
        return saveItemVO;
    }

    public static ProductBrand toProductBrand(ProductBrandSaveDTO dto) {
        ProductBrand productBrand = new ProductBrand();
        ProductBrandMainDTO main = dto.getMain();
        if (main != null) {
            productBrand.setId(main.getId());
            productBrand.setCorpid(main.getCorpid());
            productBrand.setBrandCode(main.getBrandCode());
            productBrand.setBrandName(main.getBrandName());
            productBrand.setSortNo(main.getSortNo());
            productBrand.setEnableStatus(main.getEnableStatus());
        }
        productBrand.setCorpid(dto.getCorpid());
        return productBrand;
    }

    public static ProductBrandVO toVO(ProductBrand productBrand) {
        if (productBrand == null) {
            return null;
        }
        ProductBrandVO vo = new ProductBrandVO();
        vo.setId(productBrand.getId());
        vo.setBrandCode(productBrand.getBrandCode());
        vo.setBrandName(productBrand.getBrandName());
        vo.setSortNo(productBrand.getSortNo());
        vo.setEnableStatus(productBrand.getEnableStatus());
        return vo;
    }

    public static ProductBrandSaveItemVO toSaveItemVO(ProductBrand productBrand) {
        ProductBrandSaveItemVO vo = buildEmptySaveItemVO();
        if (productBrand == null) {
            return vo;
        }
        ProductBrandMainDTO main = new ProductBrandMainDTO();
        main.setId(productBrand.getId());
        main.setCorpid(productBrand.getCorpid());
        main.setBrandCode(productBrand.getBrandCode());
        main.setBrandName(productBrand.getBrandName());
        main.setSortNo(productBrand.getSortNo());
        main.setEnableStatus(productBrand.getEnableStatus());
        vo.setMain(main);
        return vo;
    }

    public static ProductBrandDetailVO toDetailVO(ProductBrandSaveItemVO saveItemVO) {
        ProductBrandDetailVO detailVO = new ProductBrandDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }
}
