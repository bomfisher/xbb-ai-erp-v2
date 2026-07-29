package xbb.ai.erp.module.customer.application.validator;

import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.customer.application.pojo.CustomerSaveContextPojo;

public class CustomerSaveProtocolValidator {

    public void validate(CustomerSaveContextPojo context) {
        if (context == null || context.getMain() == null) {
            throw new BizException("客户主档不能为空");
        }
        if (context.getCorpid() == null || context.getCorpid().isBlank()) {
            throw new BizException("公司不能为空");
        }
    }
}
