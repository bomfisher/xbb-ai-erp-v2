package xbb.ai.erp.module.customer.application.validator;

import xbb.ai.erp.base.common.filed.FieldRule;
import xbb.ai.erp.module.customer.admin.CustomerFieldEnum;

import java.util.List;

public final class CustomerSaveFieldRules {

    private static final List<FieldRule> FIELD_RULES = List.of(
        new FieldRule(CustomerFieldEnum.CUSTOMER_CODE.getAttr(), CustomerFieldEnum.CUSTOMER_CODE.getAttrName(), CustomerFieldEnum.CUSTOMER_CODE.getFieldType(), null, 1),
        new FieldRule(CustomerFieldEnum.CUSTOMER_NAME.getAttr(), CustomerFieldEnum.CUSTOMER_NAME.getAttrName(), CustomerFieldEnum.CUSTOMER_NAME.getFieldType(), null, 1),
        new FieldRule(CustomerFieldEnum.CUSTOMER_SHORT_NAME.getAttr(), CustomerFieldEnum.CUSTOMER_SHORT_NAME.getAttrName(), CustomerFieldEnum.CUSTOMER_SHORT_NAME.getFieldType(), null, 0),
        new FieldRule(CustomerFieldEnum.CUSTOMER_CATEGORY.getAttr(), CustomerFieldEnum.CUSTOMER_CATEGORY.getAttrName(), CustomerFieldEnum.CUSTOMER_CATEGORY.getFieldType(), null, 1),
        new FieldRule(CustomerFieldEnum.REGION_CODE.getAttr(), CustomerFieldEnum.REGION_CODE.getAttrName(), CustomerFieldEnum.REGION_CODE.getFieldType(), null, 0),
        new FieldRule(CustomerFieldEnum.OWNER_SALES_ID.getAttr(), CustomerFieldEnum.OWNER_SALES_ID.getAttrName(), CustomerFieldEnum.OWNER_SALES_ID.getFieldType(), null, 0),
        new FieldRule(CustomerFieldEnum.BIZ_STATUS.getAttr(), CustomerFieldEnum.BIZ_STATUS.getAttrName(), CustomerFieldEnum.BIZ_STATUS.getFieldType(), null, 0),
        new FieldRule(CustomerFieldEnum.REMARK.getAttr(), CustomerFieldEnum.REMARK.getAttrName(), CustomerFieldEnum.REMARK.getFieldType(), null, 0),
        new FieldRule(CustomerFieldEnum.CONTACT_NAME.getAttr(), CustomerFieldEnum.CONTACT_NAME.getAttrName(), CustomerFieldEnum.CONTACT_NAME.getFieldType(), null, 1),
        new FieldRule(CustomerFieldEnum.CONTACT_MOBILE.getAttr(), CustomerFieldEnum.CONTACT_MOBILE.getAttrName(), CustomerFieldEnum.CONTACT_MOBILE.getFieldType(), null, 0),
        new FieldRule(CustomerFieldEnum.CONTACT_PHONE.getAttr(), CustomerFieldEnum.CONTACT_PHONE.getAttrName(), CustomerFieldEnum.CONTACT_PHONE.getFieldType(), null, 0),
        new FieldRule(CustomerFieldEnum.CONTACT_EMAIL.getAttr(), CustomerFieldEnum.CONTACT_EMAIL.getAttrName(), CustomerFieldEnum.CONTACT_EMAIL.getFieldType(), null, 0),
        new FieldRule(CustomerFieldEnum.CONTACT_POSITION_NAME.getAttr(), CustomerFieldEnum.CONTACT_POSITION_NAME.getAttrName(), CustomerFieldEnum.CONTACT_POSITION_NAME.getFieldType(), null, 0),
        new FieldRule(CustomerFieldEnum.CONTACT_DEFAULT_FLAG.getAttr(), CustomerFieldEnum.CONTACT_DEFAULT_FLAG.getAttrName(), CustomerFieldEnum.CONTACT_DEFAULT_FLAG.getFieldType(), null, 0),
        new FieldRule(CustomerFieldEnum.ADDRESS_TYPE.getAttr(), CustomerFieldEnum.ADDRESS_TYPE.getAttrName(), CustomerFieldEnum.ADDRESS_TYPE.getFieldType(), null, 1),
        new FieldRule(CustomerFieldEnum.RECEIVER_NAME.getAttr(), CustomerFieldEnum.RECEIVER_NAME.getAttrName(), CustomerFieldEnum.RECEIVER_NAME.getFieldType(), null, 0),
        new FieldRule(CustomerFieldEnum.RECEIVER_MOBILE.getAttr(), CustomerFieldEnum.RECEIVER_MOBILE.getAttrName(), CustomerFieldEnum.RECEIVER_MOBILE.getFieldType(), null, 0),
        new FieldRule(CustomerFieldEnum.DETAIL_ADDRESS.getAttr(), CustomerFieldEnum.DETAIL_ADDRESS.getAttrName(), CustomerFieldEnum.DETAIL_ADDRESS.getFieldType(), null, 1),
        new FieldRule(CustomerFieldEnum.POSTAL_CODE.getAttr(), CustomerFieldEnum.POSTAL_CODE.getAttrName(), CustomerFieldEnum.POSTAL_CODE.getFieldType(), null, 0),
        new FieldRule(CustomerFieldEnum.ADDRESS_DEFAULT_FLAG.getAttr(), CustomerFieldEnum.ADDRESS_DEFAULT_FLAG.getAttrName(), CustomerFieldEnum.ADDRESS_DEFAULT_FLAG.getFieldType(), null, 0),
        new FieldRule(CustomerFieldEnum.ACCOUNT_NAME.getAttr(), CustomerFieldEnum.ACCOUNT_NAME.getAttrName(), CustomerFieldEnum.ACCOUNT_NAME.getFieldType(), null, 1),
        new FieldRule(CustomerFieldEnum.BANK_NAME.getAttr(), CustomerFieldEnum.BANK_NAME.getAttrName(), CustomerFieldEnum.BANK_NAME.getFieldType(), null, 1),
        new FieldRule(CustomerFieldEnum.ACCOUNT_NO.getAttr(), CustomerFieldEnum.ACCOUNT_NO.getAttrName(), CustomerFieldEnum.ACCOUNT_NO.getFieldType(), null, 1),
        new FieldRule(CustomerFieldEnum.ACCOUNT_USAGE.getAttr(), CustomerFieldEnum.ACCOUNT_USAGE.getAttrName(), CustomerFieldEnum.ACCOUNT_USAGE.getFieldType(), null, 0),
        new FieldRule(CustomerFieldEnum.BANK_DEFAULT_FLAG.getAttr(), CustomerFieldEnum.BANK_DEFAULT_FLAG.getAttrName(), CustomerFieldEnum.BANK_DEFAULT_FLAG.getFieldType(), null, 0),
        new FieldRule(CustomerFieldEnum.INVOICE_TITLE.getAttr(), CustomerFieldEnum.INVOICE_TITLE.getAttrName(), CustomerFieldEnum.INVOICE_TITLE.getFieldType(), null, 1),
        new FieldRule(CustomerFieldEnum.TAX_NO.getAttr(), CustomerFieldEnum.TAX_NO.getAttrName(), CustomerFieldEnum.TAX_NO.getFieldType(), null, 1),
        new FieldRule(CustomerFieldEnum.ADDRESS_PHONE.getAttr(), CustomerFieldEnum.ADDRESS_PHONE.getAttrName(), CustomerFieldEnum.ADDRESS_PHONE.getFieldType(), null, 0),
        new FieldRule(CustomerFieldEnum.BANK_ACCOUNT_NAME.getAttr(), CustomerFieldEnum.BANK_ACCOUNT_NAME.getAttrName(), CustomerFieldEnum.BANK_ACCOUNT_NAME.getFieldType(), null, 0),
        new FieldRule(CustomerFieldEnum.BANK_ACCOUNT_NO.getAttr(), CustomerFieldEnum.BANK_ACCOUNT_NO.getAttrName(), CustomerFieldEnum.BANK_ACCOUNT_NO.getFieldType(), null, 0),
        new FieldRule(CustomerFieldEnum.INVOICE_DEFAULT_FLAG.getAttr(), CustomerFieldEnum.INVOICE_DEFAULT_FLAG.getAttrName(), CustomerFieldEnum.INVOICE_DEFAULT_FLAG.getFieldType(), null, 0)
    );

    private CustomerSaveFieldRules() {
    }

    public static List<FieldRule> allFieldRules() {
        return FIELD_RULES;
    }

    public static List<String> formRequiredFields() {
        return FIELD_RULES.stream()
            .filter(rule -> Integer.valueOf(1).equals(rule.getRequired()))
            .map(FieldRule::getAttr)
            .toList();
    }
}
