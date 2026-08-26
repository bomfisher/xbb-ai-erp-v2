package xbb.ai.erp.module.masterdata.application.render;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.masterdata.domain.model.Customer;
import xbb.ai.erp.module.masterdata.domain.model.Supplier;
import xbb.ai.erp.module.masterdata.domain.repository.CustomerRepository;
import xbb.ai.erp.module.masterdata.domain.repository.SupplierRepository;

class MasterDataListReferenceValueProviderTest {
  @Test
  void shouldRenderCustomerAndSupplierNames() {
    Customer customer = new Customer();
    customer.setId(1L);
    customer.setCustomerName("客户一");
    Supplier supplier = new Supplier();
    supplier.setId(2L);
    supplier.setSupplierName("供应商二");
    CustomerRepository customerRepository = mock(CustomerRepository.class);
    SupplierRepository supplierRepository = mock(SupplierRepository.class);
    when(customerRepository.findByIds("corp-001", Set.of(1L))).thenReturn(List.of(customer));
    when(supplierRepository.findByIds("corp-001", Set.of(2L))).thenReturn(List.of(supplier));

    assertEquals(
        "客户一",
        new CustomerListReferenceValueProvider(customerRepository)
            .findDisplayMap("corp-001", Set.of("1", "invalid"))
            .get("1"));
    assertEquals(
        "供应商二",
        new SupplierListReferenceValueProvider(supplierRepository)
            .findDisplayMap("corp-001", Set.of("2"))
            .get("2"));
  }
}
