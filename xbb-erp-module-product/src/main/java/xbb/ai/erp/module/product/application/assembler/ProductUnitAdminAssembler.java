package xbb.ai.erp.module.product.application.assembler;

import xbb.ai.erp.module.product.admin.dto.ProductUnitMainDTO;
import xbb.ai.erp.module.product.admin.dto.ProductUnitSaveDTO;
import xbb.ai.erp.module.product.admin.vo.ProductUnitDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductUnitSaveItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductUnitVO;
import xbb.ai.erp.module.product.domain.model.ProductUnit;

public final class ProductUnitAdminAssembler {

    private ProductUnitAdminAssembler() {
    }

    public static ProductUnitSaveItemVO buildEmptySaveItemVO() {
        ProductUnitSaveItemVO saveItemVO = new ProductUnitSaveItemVO();
        saveItemVO.setMain(new ProductUnitMainDTO());
        return saveItemVO;
    }

    public static ProductUnit toProductUnit(ProductUnitSaveDTO dto) {
        ProductUnit productUnit = new ProductUnit();
        ProductUnitMainDTO main = dto.getMain();
        if (main != null) {
            productUnit.setId(main.getId());
            productUnit.setCorpid(main.getCorpid());
            productUnit.setUnitCode(main.getUnitCode());
            productUnit.setUnitName(main.getUnitName());
            productUnit.setPrecisionNum(main.getPrecisionNum());
            productUnit.setEnableStatus(main.getEnableStatus());
        }
        productUnit.setCorpid(dto.getCorpid());
        return productUnit;
    }

    public static ProductUnitVO toVO(ProductUnit productUnit) {
        if (productUnit == null) {
            return null;
        }
        ProductUnitVO vo = new ProductUnitVO();
        vo.setId(productUnit.getId());
        vo.setUnitCode(productUnit.getUnitCode());
        vo.setUnitName(productUnit.getUnitName());
        vo.setPrecisionNum(productUnit.getPrecisionNum());
        vo.setEnableStatus(productUnit.getEnableStatus());
        return vo;
    }

    public static ProductUnitSaveItemVO toSaveItemVO(ProductUnit productUnit) {
        ProductUnitSaveItemVO vo = buildEmptySaveItemVO();
        if (productUnit == null) {
            return vo;
        }
        ProductUnitMainDTO main = new ProductUnitMainDTO();
        main.setId(productUnit.getId());
        main.setCorpid(productUnit.getCorpid());
        main.setUnitCode(productUnit.getUnitCode());
        main.setUnitName(productUnit.getUnitName());
        main.setPrecisionNum(productUnit.getPrecisionNum());
        main.setEnableStatus(productUnit.getEnableStatus());
        vo.setMain(main);
        return vo;
    }

    public static ProductUnitDetailVO toDetailVO(ProductUnitSaveItemVO saveItemVO) {
        ProductUnitDetailVO detailVO = new ProductUnitDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }
}
