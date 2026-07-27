package xbb.ai.erp.module.customer.application.service.impl;

import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.customer.admin.dto.CustomerAddressItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerBankAccountItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerContactItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerInvoiceProfileItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerListDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSaveDTO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDetailVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerListItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerSaveItemVO;
import xbb.ai.erp.module.customer.application.assembler.CustomerAdminAssembler;
import xbb.ai.erp.module.customer.application.assembler.CustomerFieldAssembler;
import xbb.ai.erp.module.customer.application.service.CustomerAdminAppService;
import xbb.ai.erp.module.customer.domain.field.CustomerFieldFactory;
import xbb.ai.erp.module.customer.domain.field.DefaultCustomerFieldFactory;
import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.domain.model.CustomerAddress;
import xbb.ai.erp.module.customer.domain.model.CustomerBankAccount;
import xbb.ai.erp.module.customer.domain.model.CustomerContact;
import xbb.ai.erp.module.customer.domain.model.CustomerInvoiceProfile;
import xbb.ai.erp.module.customer.domain.repository.CustomerAddressRepository;
import xbb.ai.erp.module.customer.domain.repository.CustomerBankAccountRepository;
import xbb.ai.erp.module.customer.domain.repository.CustomerContactRepository;
import xbb.ai.erp.module.customer.domain.repository.CustomerInvoiceProfileRepository;
import xbb.ai.erp.module.customer.domain.repository.CustomerRepository;

import xbb.ai.erp.scene.meta.SceneTypeEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CustomerAdminAppServiceImpl implements CustomerAdminAppService {

    private final CustomerRepository customerRepository;
    private final CustomerContactRepository customerContactRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final CustomerBankAccountRepository customerBankAccountRepository;
    private final CustomerInvoiceProfileRepository customerInvoiceProfileRepository;
    private final CustomerFieldFactory customerFieldFactory;

    public CustomerAdminAppServiceImpl() {
        this(null, null, null, null, null, new DefaultCustomerFieldFactory(List.of()));
    }

    public CustomerAdminAppServiceImpl(
        CustomerRepository customerRepository,
        CustomerContactRepository customerContactRepository,
        CustomerAddressRepository customerAddressRepository,
        CustomerBankAccountRepository customerBankAccountRepository,
        CustomerInvoiceProfileRepository customerInvoiceProfileRepository,
        CustomerFieldFactory customerFieldFactory
    ) {
        this.customerRepository = customerRepository;
        this.customerContactRepository = customerContactRepository;
        this.customerAddressRepository = customerAddressRepository;
        this.customerBankAccountRepository = customerBankAccountRepository;
        this.customerInvoiceProfileRepository = customerInvoiceProfileRepository;
        this.customerFieldFactory = customerFieldFactory;
    }

    public static CustomerAdminAppServiceImpl forTesting(
        CustomerRepository customerRepository,
        CustomerContactRepository customerContactRepository,
        CustomerAddressRepository customerAddressRepository,
        CustomerBankAccountRepository customerBankAccountRepository,
        CustomerInvoiceProfileRepository customerInvoiceProfileRepository
    ) {
        return new CustomerAdminAppServiceImpl(
            customerRepository,
            customerContactRepository,
            customerAddressRepository,
            customerBankAccountRepository,
            customerInvoiceProfileRepository,
            new DefaultCustomerFieldFactory(List.of())
        );
    }

    public static CustomerAdminAppServiceImpl forTesting(
        CustomerRepository customerRepository,
        CustomerContactRepository customerContactRepository,
        CustomerAddressRepository customerAddressRepository,
        CustomerBankAccountRepository customerBankAccountRepository,
        CustomerInvoiceProfileRepository customerInvoiceProfileRepository,
        CustomerFieldFactory customerFieldFactory
    ) {
        return new CustomerAdminAppServiceImpl(
            customerRepository,
            customerContactRepository,
            customerAddressRepository,
            customerBankAccountRepository,
            customerInvoiceProfileRepository,
            customerFieldFactory
        );
    }

    @Override
    public ListBaseVO<CustomerListItemVO> list(CustomerListDTO dto) {
        Map<String, Object> fullConditionMap = buildListConditionMap(dto);
        List<Customer> allMatchedCustomers = customerRepository == null ? List.of() : customerRepository.findByCondition(fullConditionMap);

        Map<String, Object> pagedConditionMap = new HashMap<>(fullConditionMap);
        pagedConditionMap.put("pageNum", dto.getPageNum());
        pagedConditionMap.put("pageSize", dto.getPageSize());
        List<Customer> customers = customerRepository == null ? List.of() : customerRepository.findByCondition(pagedConditionMap);

        List<CustomerListItemVO> list = customers.stream().map(customer -> {
            CustomerContact defaultContact = findDefaultContact(dto.getCorpid(), customer.getId());
            CustomerAddress defaultAddress = findDefaultAddress(dto.getCorpid(), customer.getId());
            CustomerInvoiceProfile defaultInvoiceProfile = findDefaultInvoiceProfile(dto.getCorpid(), customer.getId());
            return CustomerAdminAssembler.toListItemVO(customer, defaultContact, defaultAddress, defaultInvoiceProfile);
        }).toList();

        int pageNum = dto.getPageNum() == null || dto.getPageNum() < 1 ? 1 : dto.getPageNum();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? Math.max(allMatchedCustomers.size(), 1) : dto.getPageSize();
        int pageCount = Math.max((allMatchedCustomers.size() + pageSize - 1) / pageSize, 1);

        ListBaseVO<CustomerListItemVO> vo = new ListBaseVO<>();
        vo.setList(list);
        vo.setPageHelper(new ListBaseVO.PageHelper(pageNum, pageCount));
        return vo;
    }

    private Map<String, Object> buildListConditionMap(CustomerListDTO dto) {
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("corpid", dto.getCorpid());
        conditionMap.put("keyword", dto.getKeyword());
        conditionMap.put("customerCode", dto.getCustomerCode());
        conditionMap.put("customerName", dto.getCustomerName());
        conditionMap.put("customerCategory", dto.getCustomerCategory());
        conditionMap.put("regionCode", dto.getRegionCode());
        conditionMap.put("ownerSalesId", dto.getOwnerSalesId());
        conditionMap.put("bizStatus", dto.getBizStatus());
        conditionMap.put("refStatus", dto.getRefStatus());
        return conditionMap;
    }

    @Override
    public SaveItemVO<CustomerSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<CustomerSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(CustomerFieldAssembler.buildHeadList(customerFieldFactory.getFields(SceneTypeEnum.CREATE)));
        vo.setData(CustomerAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    @Override
    public SaveItemVO<CustomerSaveItemVO> updateItem(IdBaseDTO dto) {
        SaveItemVO<CustomerSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(CustomerFieldAssembler.buildHeadList(customerFieldFactory.getFields(SceneTypeEnum.UPDATE)));
        vo.setData(CustomerAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    @Override
    public Long save(CustomerSaveDTO dto) {
        validateDefaultUniqueness(dto.getContacts(), CustomerContactItemDTO::getDefaultFlag, "联系人默认项只能有一个");
        validateDefaultUniqueness(dto.getAddresses(), CustomerAddressItemDTO::getDefaultFlag, "地址默认项只能有一个");
        validateDefaultUniqueness(dto.getBankAccounts(), CustomerBankAccountItemDTO::getDefaultFlag, "银行账户默认项只能有一个");
        validateDefaultUniqueness(dto.getInvoiceProfiles(), CustomerInvoiceProfileItemDTO::getDefaultFlag, "开票信息默认项只能有一个");

        Customer customer = CustomerAdminAssembler.toCustomer(dto);
        if (customer.getId() == null) {
            customerRepository.insert(customer);
        } else {
            customerRepository.update(customer);
        }

        Long customerId = customer.getId();
        syncContacts(dto.getCorpid(), customerId, dto.getContacts());
        syncAddresses(dto.getCorpid(), customerId, dto.getAddresses());
        syncBankAccounts(dto.getCorpid(), customerId, dto.getBankAccounts());
        syncInvoiceProfiles(dto.getCorpid(), customerId, dto.getInvoiceProfiles());
        return customerId;
    }

    @Override
    public CustomerDetailVO detail(IdBaseDTO dto) {
        Customer customer = customerRepository == null ? null : customerRepository.findById(dto.getCorpid(), dto.getId());
        List<CustomerContact> contacts = customerContactRepository == null ? List.of() : customerContactRepository.findByCondition(Map.of("corpid", dto.getCorpid(), "customerId", dto.getId()));
        List<CustomerAddress> addresses = customerAddressRepository == null ? List.of() : customerAddressRepository.findByCondition(Map.of("corpid", dto.getCorpid(), "customerId", dto.getId()));
        List<CustomerBankAccount> bankAccounts = customerBankAccountRepository == null ? List.of() : customerBankAccountRepository.findByCondition(Map.of("corpid", dto.getCorpid(), "customerId", dto.getId()));
        List<CustomerInvoiceProfile> invoiceProfiles = customerInvoiceProfileRepository == null ? List.of() : customerInvoiceProfileRepository.findByCondition(Map.of("corpid", dto.getCorpid(), "customerId", dto.getId()));
        CustomerSaveItemVO saveItemVO = CustomerAdminAssembler.toSaveItemVO(customer, contacts, addresses, bankAccounts, invoiceProfiles);
        return CustomerAdminAssembler.toDetailVO(saveItemVO);
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() == null || dto.getIdList().isEmpty()) {
            return;
        }
        for (Long id : dto.getIdList()) {
            Customer customer = customerRepository.findById(dto.getCorpid(), id);
            if (customer == null) {
                throw new IllegalArgumentException("客户不存在");
            }
            if (isReferenced(customer)) {
                throw new IllegalArgumentException("客户已被引用，不能删除");
            }
            removeContacts(dto.getCorpid(), id);
            removeAddresses(dto.getCorpid(), id);
            removeBankAccounts(dto.getCorpid(), id);
            removeInvoiceProfiles(dto.getCorpid(), id);
            customerRepository.removeById(dto.getCorpid(), id);
        }
    }

    private <T> void validateDefaultUniqueness(List<T> list, Function<T, Integer> getter, String message) {
        long count = list == null ? 0 : list.stream().filter(item -> Integer.valueOf(1).equals(getter.apply(item))).count();
        if (count > 1) {
            throw new IllegalArgumentException(message);
        }
    }

    private void syncContacts(String corpid, Long customerId, List<CustomerContactItemDTO> items) {
        if (items == null) {
            return;
        }
        List<CustomerContact> existingList = customerContactRepository.findByCondition(Map.of("corpid", corpid, "customerId", customerId));
        Set<Long> incomingIds = items.stream()
            .map(CustomerContactItemDTO::getId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        for (CustomerContact existing : existingList) {
            if (!incomingIds.contains(existing.getId())) {
                if (Integer.valueOf(1).equals(existing.getDefaultFlag())) {
                    throw new IllegalArgumentException("默认联系人不允许通过整单保存删除");
                }
                customerContactRepository.removeById(corpid, existing.getId());
            }
        }
        for (CustomerContactItemDTO item : items) {
            CustomerContact contact = CustomerAdminAssembler.toCustomerContact(corpid, customerId, item);
            if (contact.getId() == null) {
                customerContactRepository.insert(contact);
            } else {
                customerContactRepository.update(contact);
            }
        }
    }

    private void syncAddresses(String corpid, Long customerId, List<CustomerAddressItemDTO> items) {
        if (items == null) {
            return;
        }
        List<CustomerAddress> existingList = customerAddressRepository.findByCondition(Map.of("corpid", corpid, "customerId", customerId));
        Set<Long> incomingIds = items.stream()
            .map(CustomerAddressItemDTO::getId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        for (CustomerAddress existing : existingList) {
            if (!incomingIds.contains(existing.getId())) {
                if (Integer.valueOf(1).equals(existing.getDefaultFlag())) {
                    throw new IllegalArgumentException("默认地址不允许通过整单保存删除");
                }
                customerAddressRepository.removeById(corpid, existing.getId());
            }
        }
        for (CustomerAddressItemDTO item : items) {
            CustomerAddress address = CustomerAdminAssembler.toCustomerAddress(corpid, customerId, item);
            if (address.getId() == null) {
                customerAddressRepository.insert(address);
            } else {
                customerAddressRepository.update(address);
            }
        }
    }

    private void syncBankAccounts(String corpid, Long customerId, List<CustomerBankAccountItemDTO> items) {
        if (items == null) {
            return;
        }
        List<CustomerBankAccount> existingList = customerBankAccountRepository.findByCondition(Map.of("corpid", corpid, "customerId", customerId));
        Set<Long> incomingIds = items.stream()
            .map(CustomerBankAccountItemDTO::getId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        for (CustomerBankAccount existing : existingList) {
            if (!incomingIds.contains(existing.getId())) {
                if (Integer.valueOf(1).equals(existing.getDefaultFlag())) {
                    throw new IllegalArgumentException("默认银行账户不允许通过整单保存删除");
                }
                customerBankAccountRepository.removeById(corpid, existing.getId());
            }
        }
        for (CustomerBankAccountItemDTO item : items) {
            CustomerBankAccount bankAccount = CustomerAdminAssembler.toCustomerBankAccount(corpid, customerId, item);
            if (bankAccount.getId() == null) {
                customerBankAccountRepository.insert(bankAccount);
            } else {
                customerBankAccountRepository.update(bankAccount);
            }
        }
    }

    private void syncInvoiceProfiles(String corpid, Long customerId, List<CustomerInvoiceProfileItemDTO> items) {
        if (items == null) {
            return;
        }
        List<CustomerInvoiceProfile> existingList = customerInvoiceProfileRepository.findByCondition(Map.of("corpid", corpid, "customerId", customerId));
        Set<Long> incomingIds = items.stream()
            .map(CustomerInvoiceProfileItemDTO::getId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        for (CustomerInvoiceProfile existing : existingList) {
            if (!incomingIds.contains(existing.getId())) {
                if (Integer.valueOf(1).equals(existing.getDefaultFlag())) {
                    throw new IllegalArgumentException("默认开票信息不允许通过整单保存删除");
                }
                customerInvoiceProfileRepository.removeById(corpid, existing.getId());
            }
        }
        for (CustomerInvoiceProfileItemDTO item : items) {
            CustomerInvoiceProfile profile = CustomerAdminAssembler.toCustomerInvoiceProfile(corpid, customerId, item);
            if (profile.getId() == null) {
                customerInvoiceProfileRepository.insert(profile);
            } else {
                customerInvoiceProfileRepository.update(profile);
            }
        }
    }

    private CustomerContact findDefaultContact(String corpid, Long customerId) {
        if (customerContactRepository == null) {
            return null;
        }
        return customerContactRepository.findByCondition(Map.of("corpid", corpid, "customerId", customerId, "defaultFlag", 1))
            .stream()
            .findFirst()
            .orElse(null);
    }

    private CustomerAddress findDefaultAddress(String corpid, Long customerId) {
        if (customerAddressRepository == null) {
            return null;
        }
        return customerAddressRepository.findByCondition(Map.of("corpid", corpid, "customerId", customerId, "defaultFlag", 1))
            .stream()
            .findFirst()
            .orElse(null);
    }

    private CustomerInvoiceProfile findDefaultInvoiceProfile(String corpid, Long customerId) {
        if (customerInvoiceProfileRepository == null) {
            return null;
        }
        return customerInvoiceProfileRepository.findByCondition(Map.of("corpid", corpid, "customerId", customerId, "defaultFlag", 1))
            .stream()
            .findFirst()
            .orElse(null);
    }

    private void removeContacts(String corpid, Long customerId) {
        List<Long> ids = customerContactRepository.findByCondition(Map.of("corpid", corpid, "customerId", customerId))
            .stream()
            .map(CustomerContact::getId)
            .toList();
        if (!ids.isEmpty()) {
            customerContactRepository.removeBatchByIds(corpid, ids);
        }
    }

    private void removeAddresses(String corpid, Long customerId) {
        List<Long> ids = customerAddressRepository.findByCondition(Map.of("corpid", corpid, "customerId", customerId))
            .stream()
            .map(CustomerAddress::getId)
            .toList();
        if (!ids.isEmpty()) {
            customerAddressRepository.removeBatchByIds(corpid, ids);
        }
    }

    private void removeBankAccounts(String corpid, Long customerId) {
        List<Long> ids = customerBankAccountRepository.findByCondition(Map.of("corpid", corpid, "customerId", customerId))
            .stream()
            .map(CustomerBankAccount::getId)
            .toList();
        if (!ids.isEmpty()) {
            customerBankAccountRepository.removeBatchByIds(corpid, ids);
        }
    }

    private void removeInvoiceProfiles(String corpid, Long customerId) {
        List<Long> ids = customerInvoiceProfileRepository.findByCondition(Map.of("corpid", corpid, "customerId", customerId))
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
