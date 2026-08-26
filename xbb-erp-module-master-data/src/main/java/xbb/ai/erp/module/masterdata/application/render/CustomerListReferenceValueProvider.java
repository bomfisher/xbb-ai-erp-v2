package xbb.ai.erp.module.masterdata.application.render;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.common.application.render.ListReferenceKey;
import xbb.ai.erp.module.common.application.render.ListReferenceValueProviderSupport;
import xbb.ai.erp.module.masterdata.domain.model.Customer;
import xbb.ai.erp.module.masterdata.domain.repository.CustomerRepository;

@Component
public class CustomerListReferenceValueProvider extends ListReferenceValueProviderSupport {
  private final CustomerRepository customerRepository;

  public CustomerListReferenceValueProvider(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @Override
  public ListReferenceKey key() {
    return new ListReferenceKey(
        String.valueOf(FieldTypeEnum.BUSINESS.getType()), BusinessCodeEnum.CUSTOMER.getCode());
  }

  @Override
  public Map<String, String> findDisplayMap(String corpid, Set<String> values) {
    return customerRepository.findByIds(corpid, parseIds(values)).stream()
        .collect(Collectors.toMap(customer -> String.valueOf(customer.getId()), Customer::getCustomerName));
  }
}
