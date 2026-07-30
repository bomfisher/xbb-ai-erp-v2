package xbb.ai.erp.module.customer.application.service.delete;

import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.domain.model.CustomerAddress;
import xbb.ai.erp.module.customer.domain.model.CustomerBankAccount;
import xbb.ai.erp.module.customer.domain.model.CustomerContact;
import xbb.ai.erp.module.customer.domain.model.CustomerInvoiceProfile;
import xbb.ai.erp.module.customer.domain.pojo.CustomerAddressQueryPojo;
import xbb.ai.erp.module.customer.domain.pojo.CustomerBankAccountQueryPojo;
import xbb.ai.erp.module.customer.domain.pojo.CustomerContactQueryPojo;
import xbb.ai.erp.module.customer.domain.pojo.CustomerInvoiceProfileQueryPojo;
import xbb.ai.erp.module.customer.domain.repository.CustomerAddressRepository;
import xbb.ai.erp.module.customer.domain.repository.CustomerBankAccountRepository;
import xbb.ai.erp.module.customer.domain.repository.CustomerContactRepository;
import xbb.ai.erp.module.customer.domain.repository.CustomerInvoiceProfileRepository;
import xbb.ai.erp.module.customer.domain.repository.CustomerRepository;

import java.util.List;
import java.util.Map;

public class CustomerDeleteAppServiceImpl implements CustomerDeleteAppService {

    private final CustomerRepository customerRepository;
    private final CustomerContactRepository customerContactRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final CustomerBankAccountRepository customerBankAccountRepository;
    private final CustomerInvoiceProfileRepository customerInvoiceProfileRepository;

    public CustomerDeleteAppServiceImpl(
        CustomerRepository customerRepository,
        CustomerContactRepository customerContactRepository,
        CustomerAddressRepository customerAddressRepository,
        CustomerBankAccountRepository customerBankAccountRepository,
        CustomerInvoiceProfileRepository customerInvoiceProfileRepository
    ) {
        this.customerRepository = customerRepository;
        this.customerContactRepository = customerContactRepository;
        this.customerAddressRepository = customerAddressRepository;
        this.customerBankAccountRepository = customerBankAccountRepository;
        this.customerInvoiceProfileRepository = customerInvoiceProfileRepository;
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() == null || dto.getIdList().isEmpty()) {
            return;
        }
        for (Long id : dto.getIdList()) {
            Customer customer = customerRepository.findById(dto.getCorpid(), id);
            if (customer == null) {
                throw new BizException("客户不存在");
            }
            if (isReferenced(customer)) {
                throw new BizException("客户已被引用，不能删除");
            }
            removeContacts(dto.getCorpid(), id);
            removeAddresses(dto.getCorpid(), id);
            removeBankAccounts(dto.getCorpid(), id);
            removeInvoiceProfiles(dto.getCorpid(), id);
            customerRepository.removeById(dto.getCorpid(), id);
        }
    }

    private void removeContacts(String corpid, Long customerId) {
        CustomerContactQueryPojo queryPojo = new CustomerContactQueryPojo();
        queryPojo.setCorpid(corpid);
        queryPojo.setCustomerId(customerId);
        List<Long> ids = customerContactRepository.findByCondition(queryPojo)
            .stream()
            .map(CustomerContact::getId)
            .toList();
        if (!ids.isEmpty()) {
            customerContactRepository.removeBatchByIds(corpid, ids);
        }
    }

    private void removeAddresses(String corpid, Long customerId) {
        CustomerAddressQueryPojo queryPojo = new CustomerAddressQueryPojo();
        queryPojo.setCorpid(corpid);
        queryPojo.setCustomerId(customerId);
        List<Long> ids = customerAddressRepository.findByCondition(queryPojo)
            .stream()
            .map(CustomerAddress::getId)
            .toList();
        if (!ids.isEmpty()) {
            customerAddressRepository.removeBatchByIds(corpid, ids);
        }
    }

    private void removeBankAccounts(String corpid, Long customerId) {
        CustomerBankAccountQueryPojo queryPojo = new CustomerBankAccountQueryPojo();
        queryPojo.setCorpid(corpid);
        queryPojo.setCustomerId(customerId);
        List<Long> ids = customerBankAccountRepository.findByCondition(queryPojo)
            .stream()
            .map(CustomerBankAccount::getId)
            .toList();
        if (!ids.isEmpty()) {
            customerBankAccountRepository.removeBatchByIds(corpid, ids);
        }
    }

    private void removeInvoiceProfiles(String corpid, Long customerId) {
        CustomerInvoiceProfileQueryPojo queryPojo = new CustomerInvoiceProfileQueryPojo();
        queryPojo.setCorpid(corpid);
        queryPojo.setCustomerId(customerId);
        List<Long> ids = customerInvoiceProfileRepository.findByCondition(queryPojo)
            .stream()
            .map(CustomerInvoiceProfile::getId)
            .toList();
        if (!ids.isEmpty()) {
            customerInvoiceProfileRepository.removeBatchByIds(corpid, ids);
        }
    }

    private boolean isReferenced(Customer customer) {
        return "1".equals(customer.getRefStatus());
    }
}
