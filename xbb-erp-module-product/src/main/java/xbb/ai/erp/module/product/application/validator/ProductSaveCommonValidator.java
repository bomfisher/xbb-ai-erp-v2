package xbb.ai.erp.module.product.application.validator;

import xbb.ai.erp.base.common.filed.FieldValidateModeEnum;
import xbb.ai.erp.base.common.filed.FieldValueValidator;
import xbb.ai.erp.module.product.application.pojo.ProductSaveContextPojo;
import xbb.ai.erp.module.product.application.support.ProductFieldEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductSaveCommonValidator {

    private final FieldValueValidator fieldValueValidator = new FieldValueValidator();

    public void validateForDraft(ProductSaveContextPojo context) {
        validateFieldRules(context, FieldValidateModeEnum.DRAFT);
        validateSkuMode(context);
    }

    public void validateForSubmit(ProductSaveContextPojo context) {
        validateFieldRules(context, FieldValidateModeEnum.SUBMIT);
        validateSkuMode(context);
    }

    private void validateFieldRules(ProductSaveContextPojo context, FieldValidateModeEnum mode) {
        fieldValueValidator.validate(ProductFieldEnum.allFieldRules(), context, this::buildValidateTarget, mode);
    }

    private Map<String, Object> buildValidateTarget(ProductSaveContextPojo context) {
        Map<String, Object> target = new HashMap<>();
        if (context == null) {
            return target;
        }
        target.put("main", context.getMain());
        target.put("skus", context.getSkus() == null ? List.of() : context.getSkus());
        return target;
    }

    private void validateSkuMode(ProductSaveContextPojo context) {
        if (context == null || context.getMain() == null || context.getSkus() == null) {
            return;
        }
        if (Integer.valueOf(0).equals(context.getMain().getEnableSpec()) && context.getSkus().size() != 1) {
            throw new xbb.ai.erp.base.common.exception.BizException("单规格商品仅允许一条SKU");
        }
        if (Integer.valueOf(1).equals(context.getMain().getEnableSpec()) && context.getSkus().isEmpty()) {
            throw new xbb.ai.erp.base.common.exception.BizException("商品SKU不能为空");
        }
    }
}
