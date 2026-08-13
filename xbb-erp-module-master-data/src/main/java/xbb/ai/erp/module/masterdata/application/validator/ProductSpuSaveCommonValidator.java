package xbb.ai.erp.module.masterdata.application.validator;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.masterdata.admin.dto.ProductSpuSaveDTO;

@Component
public class ProductSpuSaveCommonValidator {
    public void validateForDraft(ProductSpuSaveDTO dto) {}
    public void validateForSubmit(ProductSpuSaveDTO dto) {
        if (dto.getMain().getSpuCode() == null || dto.getMain().getSpuCode().isBlank()) {
            throw new BizException("产品SPU编码不能为空");
        }
        if (dto.getMain().getSpuName() == null || dto.getMain().getSpuName().isBlank()) {
            throw new BizException("产品SPU名称不能为空");
        }
        if (dto.getMain().getCategoryName() == null || dto.getMain().getCategoryName().isBlank()) {
            throw new BizException("分类名称不能为空");
        }
    }
}
