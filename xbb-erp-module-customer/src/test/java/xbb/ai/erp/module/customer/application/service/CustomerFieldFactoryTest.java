package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.module.customer.application.assembler.CustomerFieldAssembler;
import xbb.ai.erp.module.customer.domain.field.CustomerFieldFactory;
import xbb.ai.erp.module.customer.domain.field.CustomerFieldRule;
import xbb.ai.erp.module.customer.domain.field.DefaultCustomerFieldFactory;
import xbb.ai.erp.scene.meta.SceneFieldMeta;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerFieldFactoryTest {

    @Test
    void should_return_scene_specific_customer_fields() {
        CustomerFieldFactory factory = new DefaultCustomerFieldFactory(List.of());

        List<SceneFieldMeta> addItemFields = factory.buildAddItemFields();
        List<SceneFieldMeta> listFields = factory.buildListFields();

        assertTrue(addItemFields.stream().anyMatch(field -> "main.customerShortName".equals(field.getAttr())));
        assertTrue(addItemFields.stream().anyMatch(field -> "contacts.contactName".equals(field.getAttr())
            && "联系人姓名".equals(field.getAttrName())
            && Integer.valueOf(1).equals(field.getRequired())));
        assertTrue(addItemFields.stream().anyMatch(field -> "main.customerCode".equals(field.getAttr())
            && Integer.valueOf(1).equals(field.getRequired())));
        assertTrue(listFields.stream().anyMatch(field -> "contacts.contactName".equals(field.getAttr())
            && "默认联系人".equals(field.getAttrName())
            && Integer.valueOf(0).equals(field.getRequired())));
        assertTrue(listFields.stream().anyMatch(field -> "main.customerCode".equals(field.getAttr())
            && Integer.valueOf(0).equals(field.getRequired())));
        assertFalse(listFields.stream().anyMatch(field -> "main.customerShortName".equals(field.getAttr())));
    }

    @Test
    void should_merge_extension_rule_into_head_list() {
        CustomerFieldRule extensionRule = fields -> {
            fields.add(new xbb.ai.erp.module.customer.domain.field.CustomerFieldMeta("main.customLevel", "客户等级", FieldTypeEnum.TEXT.getType(), 0, 1));
            return fields;
        };
        CustomerFieldFactory factory = new DefaultCustomerFieldFactory(List.of(extensionRule));

        List<FieldEntity> headList = CustomerFieldAssembler.buildHeadList(factory.buildListFields());

        assertTrue(headList.stream().anyMatch(field -> "main.customLevel".equals(field.getAttr())
            && "客户等级".equals(field.getAttrName())));
    }
}
