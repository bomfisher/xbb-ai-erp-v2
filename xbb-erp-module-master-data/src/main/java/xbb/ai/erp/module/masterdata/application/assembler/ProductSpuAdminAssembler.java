package xbb.ai.erp.module.masterdata.application.assembler;

import xbb.ai.erp.module.masterdata.admin.dto.ProductSpuMainDTO;
import xbb.ai.erp.module.masterdata.admin.dto.ProductSpuSaveDTO;
import xbb.ai.erp.module.masterdata.admin.vo.ProductSpuDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.ProductSpuListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.ProductSpuSaveItemVO;
import xbb.ai.erp.module.masterdata.domain.model.ProductSpu;

import java.util.Objects;

public final class ProductSpuAdminAssembler {

    private ProductSpuAdminAssembler() {
    }

    public static ProductSpuSaveItemVO buildEmptySaveItemVO() {
        ProductSpuSaveItemVO vo = new ProductSpuSaveItemVO();
        vo.setMain(new ProductSpuMainDTO());
        return vo;
    }

    public static ProductSpu toProductSpu(ProductSpuSaveDTO dto) {
        ProductSpu productSpu = new ProductSpu();
        ProductSpuMainDTO main = dto.getMain();
        if (main != null) {
            productSpu.setId(main.getId());
            productSpu.setCorpid(main.getCorpid());
            productSpu.setSpuCode(main.getSpuCode());
            productSpu.setSpuName(main.getSpuName());
            productSpu.setCategoryName(main.getCategoryName());
            productSpu.setEnabled(main.getEnabled());
            productSpu.setRemark(main.getRemark());
            if (Objects.isNull(dto.getMain().getId())) {
                productSpu.setCreatorId(dto.getUserId());
            }
            productSpu.setModifyId(dto.getUserId());
        }
        productSpu.setCorpid(dto.getCorpid());
        return productSpu;
    }

    public static ProductSpuListItemVO toListItemVO(ProductSpu productSpu) {
        ProductSpuListItemVO vo = new ProductSpuListItemVO();
        vo.setId(productSpu.getId());
        vo.setSpuCode(productSpu.getSpuCode());
        vo.setSpuName(productSpu.getSpuName());
        vo.setCategoryName(productSpu.getCategoryName());
        vo.setEnabled(Objects.isNull(productSpu.getEnabled()) ? "" : Objects.toString(productSpu.getEnabled()));
        vo.setRemark(productSpu.getRemark());
        vo.setCreatorId(productSpu.getCreatorId());
        vo.setModifyId(productSpu.getModifyId());
        return vo;
    }

    public static ProductSpuSaveItemVO toSaveItemVO(ProductSpu productSpu) {
        ProductSpuSaveItemVO vo = new ProductSpuSaveItemVO();
        if (productSpu == null) {
            return vo;
        }
        ProductSpuMainDTO main = new ProductSpuMainDTO();
        main.setId(productSpu.getId());
        main.setCorpid(productSpu.getCorpid());
        main.setSpuCode(productSpu.getSpuCode());
        main.setSpuName(productSpu.getSpuName());
        main.setCategoryName(productSpu.getCategoryName());
        main.setEnabled(productSpu.getEnabled());
        main.setRemark(productSpu.getRemark());
        main.setCreatorId(productSpu.getCreatorId());
        main.setModifyId(productSpu.getModifyId());
        vo.setMain(main);
        return vo;
    }

    public static ProductSpuDetailVO toDetailVO(ProductSpuSaveItemVO saveItemVO) {
        ProductSpuDetailVO detailVO = new ProductSpuDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }
}
