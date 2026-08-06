package xbb.ai.erp.module.product.application.validator;

import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.product.application.pojo.ProductSaveContextPojo;

public class ProductSaveProtocolValidator {

    public void validate(ProductSaveContextPojo context) {
        if (context == null || context.getMain() == null) {
            throw new BizException("商品主档不能为空");
        }
        if (context.getSkus() == null || context.getSkus().isEmpty()) {
            throw new BizException("商品SKU不能为空");
        }
    }
}
