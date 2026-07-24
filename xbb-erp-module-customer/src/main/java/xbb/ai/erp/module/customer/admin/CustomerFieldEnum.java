package xbb.ai.erp.module.customer.admin;

import lombok.Getter;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;

@Getter
public enum CustomerFieldEnum {
    CUSTOMER_CODE("main.customerCode", "客户编码", FieldTypeEnum.TEXT),
    CUSTOMER_NAME("main.customerName", "客户名称", FieldTypeEnum.TEXT),
    CUSTOMER_SHORT_NAME("main.customerShortName", "客户简称", FieldTypeEnum.TEXT),
    CUSTOMER_CATEGORY("main.customerCategory", "客户分类", FieldTypeEnum.TEXT),
    REGION_CODE("main.regionCode", "所属区域", FieldTypeEnum.TEXT),
    OWNER_SALES_ID("main.ownerSalesId", "归属销售", FieldTypeEnum.USER),
    BIZ_STATUS("main.bizStatus", "业务状态", FieldTypeEnum.COMB),
    REMARK("main.remark", "备注", FieldTypeEnum.TEXT),
    CONTACT_NAME("contacts.contactName", "联系人姓名", FieldTypeEnum.TEXT),
    CONTACT_MOBILE("contacts.mobile", "手机号", FieldTypeEnum.TEXT),
    CONTACT_PHONE("contacts.phone", "电话", FieldTypeEnum.TEXT),
    CONTACT_EMAIL("contacts.email", "邮箱", FieldTypeEnum.TEXT),
    CONTACT_POSITION_NAME("contacts.positionName", "职位", FieldTypeEnum.TEXT),
    CONTACT_DEFAULT_FLAG("contacts.defaultFlag", "是否默认", FieldTypeEnum.RADIO_BTN),
    ADDRESS_TYPE("addresses.addressType", "地址类型", FieldTypeEnum.COMB),
    RECEIVER_NAME("addresses.receiverName", "收件人", FieldTypeEnum.TEXT),
    RECEIVER_MOBILE("addresses.receiverMobile", "联系电话", FieldTypeEnum.TEXT),
    DETAIL_ADDRESS("addresses.detailAddress", "详细地址", FieldTypeEnum.TEXT),
    POSTAL_CODE("addresses.postalCode", "邮编", FieldTypeEnum.TEXT),
    ADDRESS_DEFAULT_FLAG("addresses.defaultFlag", "是否默认", FieldTypeEnum.RADIO_BTN),
    ACCOUNT_NAME("bankAccounts.accountName", "账户名称", FieldTypeEnum.TEXT),
    BANK_NAME("bankAccounts.bankName", "开户行", FieldTypeEnum.TEXT),
    ACCOUNT_NO("bankAccounts.accountNo", "银行账号", FieldTypeEnum.TEXT),
    ACCOUNT_USAGE("bankAccounts.accountUsage", "账户用途", FieldTypeEnum.TEXT),
    BANK_DEFAULT_FLAG("bankAccounts.defaultFlag", "是否默认", FieldTypeEnum.RADIO_BTN),
    INVOICE_TITLE("invoiceProfiles.invoiceTitle", "开票抬头", FieldTypeEnum.TEXT),
    TAX_NO("invoiceProfiles.taxNo", "税号", FieldTypeEnum.TEXT),
    ADDRESS_PHONE("invoiceProfiles.addressPhone", "地址电话", FieldTypeEnum.TEXT),
    BANK_ACCOUNT_NAME("invoiceProfiles.bankName", "开户行", FieldTypeEnum.TEXT),
    BANK_ACCOUNT_NO("invoiceProfiles.bankAccountNo", "银行账号", FieldTypeEnum.TEXT),
    INVOICE_DEFAULT_FLAG("invoiceProfiles.defaultFlag", "是否默认", FieldTypeEnum.RADIO_BTN),
    ;

    private final String attr;
    private final String attrName;
    private final Integer fieldType;

    CustomerFieldEnum(String attr, String attrName, FieldTypeEnum fieldType) {
        this.attr = attr;
        this.attrName = attrName;
        this.fieldType = fieldType.getType();
    }
}
