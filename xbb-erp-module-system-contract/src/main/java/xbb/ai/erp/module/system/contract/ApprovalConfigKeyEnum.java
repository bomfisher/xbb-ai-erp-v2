package xbb.ai.erp.module.system.contract;

import xbb.ai.erp.base.common.module.BusinessCodeEnum;

public enum ApprovalConfigKeyEnum implements BusinessConfigKey<ApprovalMode> {
    SALES_ORDER_APPROVAL_MODE(BusinessCodeEnum.SALES_ORDER),
    SALES_OUTBOUND_APPROVAL_MODE(BusinessCodeEnum.SALES_OUTBOUND),
    SALES_INVOICE_APPROVAL_MODE(BusinessCodeEnum.SALES_INVOICE),
    PURCHASE_ORDER_APPROVAL_MODE(BusinessCodeEnum.PURCHASE_ORDER),
    PURCHASE_INBOUND_APPROVAL_MODE(BusinessCodeEnum.PURCHASE_INBOUND),
    PURCHASE_INVOICE_APPROVAL_MODE(BusinessCodeEnum.PURCHASE_INVOICE),
    ADVANCE_PAYMENT_APPROVAL_MODE(BusinessCodeEnum.ADVANCE_PAYMENT),
    PAYABLE_APPROVAL_MODE(BusinessCodeEnum.PAYABLE),
    PAYMENT_APPROVAL_MODE(BusinessCodeEnum.PAYMENT),
    PAYMENT_WRITEOFF_APPROVAL_MODE(BusinessCodeEnum.PAYMENT_WRITEOFF),
    ADVANCE_RECEIPT_APPROVAL_MODE(BusinessCodeEnum.ADVANCE_RECEIPT),
    RECEIVABLE_APPROVAL_MODE(BusinessCodeEnum.RECEIVABLE),
    RECEIPT_APPROVAL_MODE(BusinessCodeEnum.RECEIPT),
    RECEIPT_WRITEOFF_APPROVAL_MODE(BusinessCodeEnum.RECEIPT_WRITEOFF);

    private final BusinessCodeEnum businessCode;

    ApprovalConfigKeyEnum(BusinessCodeEnum businessCode) {
        this.businessCode = businessCode;
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
    public Class<ApprovalMode> valueType() {
        return ApprovalMode.class;
    }

    @Override
    public ApprovalMode defaultValue() {
        return ApprovalMode.REQUIRED;
    }
}
