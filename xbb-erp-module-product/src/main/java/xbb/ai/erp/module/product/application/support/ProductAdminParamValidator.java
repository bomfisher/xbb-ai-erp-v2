package xbb.ai.erp.module.product.application.support;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;

public final class ProductAdminParamValidator {

    private ProductAdminParamValidator() {
    }

    public static void requireCorpid(BaseDTO dto) {
        if (dto == null || dto.getCorpid() == null || dto.getCorpid().isBlank()) {
            throw new BizException("公司不能为空");
        }
    }

    public static void validateIdQuery(IdBaseDTO dto) {
        requireCorpid(dto);
        if (dto.getId() == null) {
            throw new BizException("id不能为空");
        }
    }

    public static void validateBatchDelete(BatchBaseDTO dto) {
        requireCorpid(dto);
        if (dto.getIdList() == null || dto.getIdList().isEmpty()) {
            throw new BizException("idList不能为空");
        }
    }
}
