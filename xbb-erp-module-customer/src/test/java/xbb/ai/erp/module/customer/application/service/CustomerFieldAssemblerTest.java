package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.module.customer.application.assembler.CustomerFieldAssembler;
import xbb.ai.erp.module.customer.domain.field.CustomerFieldFactory;
import xbb.ai.erp.module.customer.domain.field.DefaultCustomerFieldFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerFieldAssemblerTest {

    @Test
    void should_build_customer_head_list() {
        CustomerFieldFactory factory = new DefaultCustomerFieldFactory(List.of());

        List<FieldEntity> headList = CustomerFieldAssembler.buildHeadList(factory.buildAddItemFields());

        assertTrue(headList.stream().anyMatch(field -> "main.customerName".equals(field.getAttr())));
        assertTrue(headList.stream().anyMatch(field -> "contacts.contactName".equals(field.getAttr())));
        assertTrue(headList.stream().anyMatch(field -> "bankAccounts.accountNo".equals(field.getAttr())));
    }
}
