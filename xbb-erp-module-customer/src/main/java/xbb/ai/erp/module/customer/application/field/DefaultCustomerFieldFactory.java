package xbb.ai.erp.module.customer.application.field;

import xbb.ai.erp.module.customer.admin.CustomerBizStatusEnum;
import xbb.ai.erp.module.customer.admin.CustomerFieldEnum;
import xbb.ai.erp.module.customer.application.validator.CustomerSaveFieldRules;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DefaultCustomerFieldFactory implements CustomerFieldFactory {

    private final List<CustomerFieldRule> rules;

    public DefaultCustomerFieldFactory(List<CustomerFieldRule> rules) {
        this.rules = rules;
    }

    @Override
    public String sceneKey() {
        return "customer.archive";
    }

    @Override
    public List<SceneFieldMeta> getFields(SceneTypeEnum sceneType) {
        return switch (sceneType) {
            case LIST -> new ArrayList<>(buildListFields());
            case CREATE -> new ArrayList<>(buildAddItemFields());
            case UPDATE -> new ArrayList<>(buildUpdateItemFields());
            case DETAIL -> new ArrayList<>(buildUpdateItemFields());
        };
    }

    @Override
    public List<SceneFieldMeta> buildListFields() {
        List<CustomerFieldMeta> fields = new ArrayList<>(List.of(
            listField(CustomerFieldEnum.CUSTOMER_CODE),
            listField(CustomerFieldEnum.CUSTOMER_NAME),
            listField(CustomerFieldEnum.CUSTOMER_CATEGORY),
            listField(CustomerFieldEnum.OWNER_SALES_ID),
            listField(CustomerFieldEnum.CONTACT_NAME).withAttrName("默认联系人"),
            listField(CustomerFieldEnum.CONTACT_MOBILE).withAttrName("联系电话"),
            listField(CustomerFieldEnum.DETAIL_ADDRESS).withAttrName("默认地址"),
            listField(CustomerFieldEnum.INVOICE_TITLE).withAttrName("默认开票抬头"),
            listField(CustomerFieldEnum.BIZ_STATUS)
        ));
        return List.copyOf(applyRules(fields));
    }

    @Override
    public List<SceneFieldMeta> buildAddItemFields() {
        List<CustomerFieldMeta> fields = new ArrayList<>(java.util.Arrays.stream(CustomerFieldEnum.values())
            .map(this::formField)
            .toList());
        return List.copyOf(applyRules(fields));
    }

    @Override
    public List<SceneFieldMeta> buildUpdateItemFields() {
        return List.copyOf(applyRules(new ArrayList<>(java.util.Arrays.stream(CustomerFieldEnum.values())
            .map(this::formField)
            .toList())));
    }

    private CustomerFieldMeta formField(CustomerFieldEnum fieldEnum) {
        return withOptions(new CustomerFieldMeta(
            fieldEnum.getAttr(),
            fieldEnum.getAttrName(),
            fieldEnum.getFieldType(),
            CustomerSaveFieldRules.formRequiredFields().contains(fieldEnum.getAttr()) ? 1 : 0,
            1
        ), fieldEnum);
    }

    private CustomerFieldMeta listField(CustomerFieldEnum fieldEnum) {
        return withOptions(new CustomerFieldMeta(
            fieldEnum.getAttr(),
            fieldEnum.getAttrName(),
            fieldEnum.getFieldType(),
            0,
            1
        ), fieldEnum);
    }

    private CustomerFieldMeta withOptions(CustomerFieldMeta field, CustomerFieldEnum fieldEnum) {
        if (Objects.equals(CustomerFieldEnum.BIZ_STATUS, fieldEnum)) {
            return field.withItemList(CustomerBizStatusEnum.toFieldItems());
        }
        return field;
    }

    private List<CustomerFieldMeta> applyRules(List<CustomerFieldMeta> fields) {
        List<CustomerFieldMeta> current = fields;
        for (CustomerFieldRule rule : rules) {
            current = rule.apply(current);
        }
        return current;
    }
}
