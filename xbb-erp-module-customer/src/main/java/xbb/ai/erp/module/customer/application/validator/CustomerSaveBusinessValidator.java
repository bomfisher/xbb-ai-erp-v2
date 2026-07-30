package xbb.ai.erp.module.customer.application.validator;

import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.customer.application.pojo.CustomerSaveContextPojo;
import xbb.ai.erp.module.customer.domain.repository.CustomerRepository;

public class CustomerSaveBusinessValidator {

    private final CustomerRepository customerRepository;

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
        boolean duplicated = customerRepository.existsByCustomerCode(
            context.getCorpid(),
            customerCode,
            context.getMain().getId()
        );
        if (duplicated) {
            throw new BizException("客户编码已存在");
        }
    }
}
