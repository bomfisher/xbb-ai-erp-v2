package xbb.ai.erp.module.purchase.application.validator;

import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseOrderSaveContextPojo;

public class PurchaseOrderSaveProtocolValidator {

    public void validate(PurchaseOrderSaveContextPojo context) {
        if (context == null || context.getMain() == null) {
            throw new BizException("采购订单主档不能为空");
        }
        if (context.getCorpid() == null || context.getCorpid().isBlank()) {
            throw new BizException("公司不能为空");
        }
    }
}
