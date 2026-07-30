package xbb.ai.erp.module.customer.application.service.delete;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerAddressRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerBankAccountRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerContactRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerInvoiceProfileRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerRepository;
import xbb.ai.erp.module.customer.domain.model.Customer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerDeleteAppServiceTest {

    @Test
    void should_delete_customer() {
        InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setCorpid("corp-001");
        customer.setCustomerCode("CUST-001");
        customerRepository.seed(customer);

        CustomerDeleteAppService service = new CustomerDeleteAppServiceImpl(
            customerRepository,
            new InMemoryCustomerContactRepository(),
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository()
        );

        BatchBaseDTO dto = new BatchBaseDTO();
        dto.setCorpid("corp-001");
        dto.setIdList(List.of(1L));
        service.delete(dto);

        assertTrue(customerRepository.all().isEmpty());
    }
}
