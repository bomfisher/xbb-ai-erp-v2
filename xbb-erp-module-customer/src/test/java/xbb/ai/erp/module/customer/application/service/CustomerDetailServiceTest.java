package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.customer.admin.vo.CustomerDetailVO;
import xbb.ai.erp.module.customer.application.service.impl.CustomerAdminAppServiceImpl;
import xbb.ai.erp.module.customer.application.service.support.FakeCustomerRepository;
import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.domain.repository.CustomerRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerDetailServiceTest {

    @Test
    void should_reject_null_id_for_detail() {
        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(null, null, null, null, null);
        IdBaseDTO dto = new IdBaseDTO();
        dto.setCorpid("corp-001");

        BizException exception = assertThrows(BizException.class, () -> service.detail(dto));

        org.junit.jupiter.api.Assertions.assertEquals("id不能为空", exception.getMessage());
    }

    @Test
    void should_return_nested_data_and_empty_todo_sections() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setCorpid("corp-001");
        customer.setCustomerName("杭州客户");

        CustomerRepository customerRepository = new FakeCustomerRepository(List.of(customer));
        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(customerRepository, null, null, null, null);

        IdBaseDTO dto = new IdBaseDTO();
        dto.setCorpid("corp-001");
        dto.setId(1L);

        CustomerDetailVO result = service.detail(dto);

        assertNotNull(result.getMainData());
        assertTrue(result.getReferenceTodoSections().isEmpty());
        assertTrue(result.getOperateLogTodoSections().isEmpty());
    }
}
