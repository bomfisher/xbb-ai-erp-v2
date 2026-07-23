package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.module.customer.admin.CustomerFieldEnum;
import xbb.ai.erp.module.customer.application.assembler.CustomerFieldAssembler;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerFieldAssemblerTest {

    @Test
    void should_build_customer_head_list() {
        List<FieldEntity> headList = CustomerFieldAssembler.buildAddItemHeadList();

        assertTrue(headList.stream().anyMatch(field -> "main.customerName".equals(field.getAttr())));
        assertTrue(headList.stream().anyMatch(field -> "contacts.contactName".equals(field.getAttr())));
        assertTrue(headList.stream().anyMatch(field -> "bankAccounts.accountNo".equals(field.getAttr())));
        assertEquals("main.customerCode", CustomerFieldEnum.CUSTOMER_CODE.getAttr());
    }
}
