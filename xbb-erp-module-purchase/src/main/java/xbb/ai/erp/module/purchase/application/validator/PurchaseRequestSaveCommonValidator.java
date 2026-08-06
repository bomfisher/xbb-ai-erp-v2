package xbb.ai.erp.module.purchase.application.validator;

import xbb.ai.erp.module.purchase.application.pojo.PurchaseRequestSaveContextPojo;

public class PurchaseRequestSaveCommonValidator {

    public void validateForDraft(PurchaseRequestSaveContextPojo context) {
        // 当前首版样板仅保持 customer 风格校验入口，最小实现不增加额外规则。
    }

    public void validateForSubmit(PurchaseRequestSaveContextPojo context) {
        // 当前首版样板仅保持 customer 风格校验入口，最小实现不增加额外规则。
    }
}
