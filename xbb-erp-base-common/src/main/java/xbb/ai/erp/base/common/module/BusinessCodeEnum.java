package xbb.ai.erp.base.common.module;

import lombok.Getter;

/**
 * 业务code枚举
 */
@Getter
public enum BusinessCodeEnum {
    ORG_MEMBER("ORG_MEMBER", "组织成员"),
    ORG_DEPARTMENT("ORG_DEPARTMENT", "组织部门"),
    DEMO("DEMO", "示例"),
    DEMO_ITEM("DEMO_ITEM", "示例明细"),
    DEMO_SUB("DEMO_SUB", "示例下游"),
    CUSTOMER("CUSTOMER", "客户"),
    SUPPLIER("SUPPLIER", "供应商"),
    WAREHOUSE("WAREHOUSE", "仓库"),
    PRODUCT_SPU("PRODUCT_SPU", "产品 SPU"),
    PRODUCT_SKU("PRODUCT_SKU", "产品 SKU"),
    PURCHASE_ORDER("PURCHASE_ORDER", "采购订单"),
    PURCHASE_INBOUND("PURCHASE_INBOUND", "采购入库单"),
    SALES_ORDER("SALES_ORDER", "销售订单"),
    SALES_CONTRACT("SALES_CONTRACT", "销售合同"),
    SALES_OUTBOUND("SALES_OUTBOUND", "销售出库单"),
    SALES_INVOICE("SALES_INVOICE", "销售发票"),
    RECEIPT("RECEIPT", "收款单"),
    ADVANCE_RECEIPT("ADVANCE_RECEIPT", "预收款单"),
    RECEIVABLE("RECEIVABLE", "应收单"),
    RECEIPT_WRITEOFF("RECEIPT_WRITEOFF", "收款核销"),
    PAYMENT("PAYMENT", "付款单"),
    ADVANCE_PAYMENT("ADVANCE_PAYMENT", "预付款单"),
    PAYABLE("PAYABLE", "应付款"),
    PAYMENT_WRITEOFF("PAYMENT_WRITEOFF", "付款核销"),
    PURCHASE_INVOICE("PURCHASE_INVOICE", "采购发票"),
    FUND_TRANSACTION("FUND_TRANSACTION", "资金流水"),
    FUND_ACCOUNT("FUND_ACCOUNT", "资金账户"),
    ;

    private final String code;
    private final String chineseName;

    BusinessCodeEnum(String code, String chineseName) {
        this.code = code;
        this.chineseName = chineseName;
    }

    public static boolean containsCode(String businessCode) {
        for (BusinessCodeEnum business : values()) {
            if (business.code.equals(businessCode)) {
                return true;
            }
        }
        return false;
    }

    public static String chineseNameOf(String businessCode) {
        for (BusinessCodeEnum business : values()) {
            if (business.code.equals(businessCode)) {
                return business.chineseName;
            }
        }
        return businessCode;
    }
}
