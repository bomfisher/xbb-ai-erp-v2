package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.module.customer.application.assembler.CustomerFieldAssembler;
import xbb.ai.erp.module.customer.application.field.CustomerFieldFactory;
import xbb.ai.erp.module.customer.application.field.CustomerFieldMeta;
import xbb.ai.erp.module.customer.application.field.CustomerFieldRule;
import xbb.ai.erp.module.customer.application.field.DefaultCustomerFieldFactory;
import xbb.ai.erp.scene.meta.SceneFieldMeta;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerFieldFactoryTest {

    @Test
    void should_return_scene_specific_customer_fields() {
        CustomerFieldFactory factory = new DefaultCustomerFieldFactory(List.of());

        List<SceneFieldMeta> addItemFields = factory.buildAddItemFields();
        List<SceneFieldMeta> listFields = factory.buildListFields();
        Map<String, SceneFieldMeta> addItemFieldMap = addItemFields.stream().collect(Collectors.toMap(SceneFieldMeta::getAttr, Function.identity()));
        Map<String, SceneFieldMeta> listFieldMap = listFields.stream().collect(Collectors.toMap(SceneFieldMeta::getAttr, Function.identity()));

        assertTrue(addItemFields.stream().anyMatch(field -> "main.customerShortName".equals(field.getAttr())));
        assertTrue(addItemFields.stream().anyMatch(field -> "contacts.contactName".equals(field.getAttr())
            && "联系人姓名".equals(field.getAttrName())
            && Integer.valueOf(1).equals(field.getRequired())));
        assertTrue(addItemFields.stream().anyMatch(field -> "main.customerCode".equals(field.getAttr())
            && Integer.valueOf(1).equals(field.getRequired())));
        assertTrue(addItemFields.stream().anyMatch(field -> "main.customerCategory".equals(field.getAttr())
            && Integer.valueOf(1).equals(field.getRequired())));
        assertEquals(Integer.valueOf(0), addItemFieldMap.get("main.bizStatus").getRequired());
        assertEquals(2, addItemFieldMap.get("main.bizStatus").getItemList().size());
        assertEquals("1", String.valueOf(addItemFieldMap.get("main.bizStatus").getItemList().get(0).getValue()));
        assertEquals("启用", addItemFieldMap.get("main.bizStatus").getItemList().get(0).getText());
        assertTrue(listFields.stream().anyMatch(field -> "contacts.contactName".equals(field.getAttr())
            && "默认联系人".equals(field.getAttrName())
            && Integer.valueOf(0).equals(field.getRequired())));
        assertTrue(listFields.stream().anyMatch(field -> "main.customerCode".equals(field.getAttr())
            && Integer.valueOf(0).equals(field.getRequired())));
        assertEquals(2, listFieldMap.get("main.bizStatus").getItemList().size());
        assertEquals("0", String.valueOf(listFieldMap.get("main.bizStatus").getItemList().get(1).getValue()));
        assertEquals("停用", listFieldMap.get("main.bizStatus").getItemList().get(1).getText());
        assertFalse(listFields.stream().anyMatch(field -> "main.customerShortName".equals(field.getAttr())));
    }

    @Test
    void should_merge_extension_rule_into_head_list() {
        CustomerFieldRule extensionRule = fields -> {
            fields.add(new CustomerFieldMeta("main.customLevel", "客户等级", FieldTypeEnum.TEXT.getType(), 0, 1));
            return fields;
        };
        CustomerFieldFactory factory = new DefaultCustomerFieldFactory(List.of(extensionRule));

        List<FieldEntity> headList = CustomerFieldAssembler.buildHeadList(factory.buildListFields());

        assertTrue(headList.stream().anyMatch(field -> "main.customLevel".equals(field.getAttr())
            && "客户等级".equals(field.getAttrName())));
    }
}
