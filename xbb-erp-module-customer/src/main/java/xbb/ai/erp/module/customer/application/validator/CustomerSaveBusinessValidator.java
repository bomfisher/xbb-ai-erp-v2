package xbb.ai.erp.module.customer.application.validator;

import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.common.admin.pojo.ListFilterCondition;
import xbb.ai.erp.module.common.application.filter.ListFilterConditionBuilder;
import xbb.ai.erp.module.customer.application.pojo.CustomerSaveContextPojo;
import xbb.ai.erp.module.customer.application.provider.CustomerListMetaProvider;
import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.domain.repository.CustomerRepository;

import java.util.List;
import java.util.Map;

public class CustomerSaveBusinessValidator {

    private final CustomerRepository customerRepository;
    private final ListFilterConditionBuilder listFilterConditionBuilder = new ListFilterConditionBuilder();

    public CustomerSaveBusinessValidator(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void validateForSubmit(CustomerSaveContextPojo context) {
        validateCustomerCodeDuplicate(context);
    }

    private void validateCustomerCodeDuplicate(CustomerSaveContextPojo context) {
        if (customerRepository == null || context.getMain() == null) {
            return;
        }
        String customerCode = context.getMain().getCustomerCode();
        if (customerCode == null || customerCode.isBlank()) {
            return;
        }
        ListFilterCondition condition = new ListFilterCondition();
        condition.setAttr("customerCode");
        condition.setFieldType("TEXT");
        condition.setSymbol("EQ");
        condition.setValue(List.of(customerCode));
        List<Customer> matchedCustomers = customerRepository.findByCondition(Map.of(
            "corpid", context.getCorpid(),
            "conditions", listFilterConditionBuilder.build(List.of(condition), CustomerListMetaProvider.buildConditionMetaMap())
        ));
        Long currentId = context.getMain().getId();
        boolean duplicated = matchedCustomers.stream()
            .anyMatch(customer -> currentId == null || !currentId.equals(customer.getId()));
        if (duplicated) {
            throw new BizException("客户编码已存在");
        }
    }
}
