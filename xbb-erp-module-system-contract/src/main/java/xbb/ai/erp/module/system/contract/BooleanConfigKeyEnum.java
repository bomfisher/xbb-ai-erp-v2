package xbb.ai.erp.module.system.contract;

import xbb.ai.erp.base.common.module.BusinessCodeEnum;

public enum BooleanConfigKeyEnum implements BusinessConfigKey<Boolean> {
    SALES_INVOICE_AUTO_CREATE_RECEIVABLE(BusinessCodeEnum.SALES_INVOICE, false),
    PURCHASE_INVOICE_AUTO_CREATE_PAYABLE(BusinessCodeEnum.PURCHASE_INVOICE, false);

    private final BusinessCodeEnum businessCode;
    private final Boolean defaultValue;

    BooleanConfigKeyEnum(BusinessCodeEnum businessCode, Boolean defaultValue) {
        this.businessCode = businessCode;
        this.defaultValue = defaultValue;
    }

    @Override
    public String code() {
        return name();
    }

    @Override
    public BusinessCodeEnum businessCode() {
        return businessCode;
    }

    @Override
    public Class<Boolean> valueType() {
        return Boolean.class;
    }

    @Override
    public Boolean defaultValue() {
        return defaultValue;
    }
}
