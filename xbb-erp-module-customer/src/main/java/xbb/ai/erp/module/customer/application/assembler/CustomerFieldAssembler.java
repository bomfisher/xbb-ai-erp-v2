package xbb.ai.erp.module.customer.application.assembler;

import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.module.customer.admin.CustomerFieldEnum;

import java.util.Arrays;
import java.util.List;

public final class CustomerFieldAssembler {

    private CustomerFieldAssembler() {
    }

    public static List<FieldEntity> buildAddItemHeadList() {
        List<String> requiredList = CustomerFieldEnum.CUSTOMER_CODE.getRequiredList();
        return Arrays.stream(CustomerFieldEnum.values())
            .map(field -> {
                FieldEntity entity = new FieldEntity();
                entity.setAttr(field.getAttr());
                entity.setAttrName(field.getAttrName());
                entity.setFieldType(String.valueOf(field.getFieldType()));
                entity.setRequired(requiredList.contains(field.getAttr()) ? 1 : 0);
                entity.setEditable(1);
                return entity;
            })
            .toList();
    }

    public static List<FieldEntity> buildListHeadList() {
        return List.of(
            field("main.customerCode", "客户编码", CustomerFieldEnum.CUSTOMER_CODE.getFieldType()),
            field("main.customerName", "客户名称", CustomerFieldEnum.CUSTOMER_NAME.getFieldType()),
            field("main.customerCategory", "客户分类", CustomerFieldEnum.CUSTOMER_CATEGORY.getFieldType()),
            field("main.ownerSalesId", "归属销售", CustomerFieldEnum.OWNER_SALES_ID.getFieldType()),
            field("contacts.contactName", "默认联系人", CustomerFieldEnum.CONTACT_NAME.getFieldType()),
            field("contacts.mobile", "联系电话", CustomerFieldEnum.CONTACT_MOBILE.getFieldType()),
            field("addresses.detailAddress", "默认地址", CustomerFieldEnum.DETAIL_ADDRESS.getFieldType()),
            field("invoiceProfiles.invoiceTitle", "默认开票抬头", CustomerFieldEnum.INVOICE_TITLE.getFieldType()),
            field("main.bizStatus", "业务状态", CustomerFieldEnum.BIZ_STATUS.getFieldType())
        );
    }

    private static FieldEntity field(String attr, String attrName, Integer fieldType) {
        FieldEntity entity = new FieldEntity();
        entity.setAttr(attr);
        entity.setAttrName(attrName);
        entity.setFieldType(String.valueOf(fieldType));
        entity.setRequired(0);
        entity.setEditable(1);
        return entity;
    }
}
