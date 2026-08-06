package xbb.ai.erp.module.supplier.application.validator;

import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.supplier.application.pojo.SupplierSaveContextPojo;

public class SupplierSaveProtocolValidator {

    public void validate(SupplierSaveContextPojo context) {
        if (context == null || context.getMain() == null) {
            throw new BizException("供应商主档不能为空");
        }
        if (context.getCorpid() == null || context.getCorpid().isBlank()) {
            throw new BizException("公司不能为空");
        }
    }
}
