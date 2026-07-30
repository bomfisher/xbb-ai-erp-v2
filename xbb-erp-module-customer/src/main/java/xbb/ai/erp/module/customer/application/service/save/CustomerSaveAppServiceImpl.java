package xbb.ai.erp.module.customer.application.service.save;

import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.customer.admin.dto.CustomerAddressItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerBankAccountItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerContactItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerInvoiceProfileItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSaveDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSubmitSaveDTO;
import xbb.ai.erp.module.customer.application.assembler.CustomerAdminAssembler;
import xbb.ai.erp.module.customer.application.pojo.CustomerSaveContextPojo;
import xbb.ai.erp.module.customer.application.pojo.CustomerSaveExtPojo;
import xbb.ai.erp.module.customer.application.port.CustomerDraftRepository;
import xbb.ai.erp.module.customer.application.validator.CustomerSaveBusinessValidator;
import xbb.ai.erp.module.customer.application.validator.CustomerSaveCommonValidator;
import xbb.ai.erp.module.customer.application.validator.CustomerSaveProtocolValidator;
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
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CustomerSaveAppServiceImpl implements CustomerSaveAppService {

    private final CustomerRepository customerRepository;
    private final CustomerContactRepository customerContactRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final CustomerBankAccountRepository customerBankAccountRepository;
    private final CustomerInvoiceProfileRepository customerInvoiceProfileRepository;
    private final CustomerDraftRepository customerDraftRepository;
    private final CustomerSaveProtocolValidator protocolValidator;
    private final CustomerSaveCommonValidator commonValidator;
    private final CustomerSaveBusinessValidator businessValidator;

    public CustomerSaveAppServiceImpl(
        CustomerRepository customerRepository,
        CustomerContactRepository customerContactRepository,
        CustomerAddressRepository customerAddressRepository,
        CustomerBankAccountRepository customerBankAccountRepository,
        CustomerInvoiceProfileRepository customerInvoiceProfileRepository,
        CustomerDraftRepository customerDraftRepository
    ) {
        this.customerRepository = customerRepository;
        this.customerContactRepository = customerContactRepository;
        this.customerAddressRepository = customerAddressRepository;
        this.customerBankAccountRepository = customerBankAccountRepository;
        this.customerInvoiceProfileRepository = customerInvoiceProfileRepository;
        this.customerDraftRepository = customerDraftRepository;
        this.protocolValidator = new CustomerSaveProtocolValidator();
        this.commonValidator = new CustomerSaveCommonValidator();
        this.businessValidator = new CustomerSaveBusinessValidator(customerRepository);
    }

    @Override
    public BaseVO saveAndSubmit(CustomerSubmitSaveDTO dto) {
        CustomerSaveContextPojo context = CustomerAdminAssembler.toSubmitContext(dto);
        protocolValidator.validate(context);
        commonValidator.validateForSubmit(context);
        businessValidator.validateForSubmit(context);

        CustomerSaveExtPojo filteredExt = filterClosedSections(context);
        CustomerSaveDTO saveDTO = new CustomerSaveDTO();
        saveDTO.setCorpid(dto.getCorpid());
        saveDTO.setUserId(dto.getUserId());
        saveDTO.setMain(dto.getMain());
        saveDTO.setContacts(filteredExt.getContacts());
        saveDTO.setAddresses(filteredExt.getAddresses());
        saveDTO.setBankAccounts(filteredExt.getBankAccounts());
        saveDTO.setInvoiceProfiles(filteredExt.getInvoiceProfiles());
        save(saveDTO);

        if (customerDraftRepository != null && dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) {
            customerDraftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
        }
        return new BaseVO();
    }

    @Override
    public Long save(CustomerSaveDTO dto) {
        validateDefaultUniqueness(dto.getContacts(), CustomerContactItemDTO::getDefaultFlag, "联系人默认项只能有一个");
        validateDefaultUniqueness(dto.getAddresses(), CustomerAddressItemDTO::getDefaultFlag, "地址默认项只能有一个");
        validateDefaultUniqueness(dto.getBankAccounts(), CustomerBankAccountItemDTO::getDefaultFlag, "银行账户默认项只能有一个");
        validateDefaultUniqueness(dto.getInvoiceProfiles(), CustomerInvoiceProfileItemDTO::getDefaultFlag, "开票信息默认项只能有一个");

        Customer customer = CustomerAdminAssembler.toCustomer(dto);
        applyCustomerDefaults(customer);
        if (customer.getId() == null) {
            customerRepository.insert(customer);
        } else {
            customerRepository.update(customer);
        }

        Long customerId = customer.getId();
        syncContacts(dto.getCorpid(), customerId, dto.getUserId(), dto.getContacts());
        syncAddresses(dto.getCorpid(), customerId, dto.getUserId(), dto.getAddresses());
        syncBankAccounts(dto.getCorpid(), customerId, dto.getUserId(), dto.getBankAccounts());
        syncInvoiceProfiles(dto.getCorpid(), customerId, dto.getUserId(), dto.getInvoiceProfiles());
        return customerId;
    }

    private CustomerSaveExtPojo filterClosedSections(CustomerSaveContextPojo context) {
        CustomerSaveExtPojo filtered = new CustomerSaveExtPojo();
        if (context == null || context.getExt() == null) {
            return filtered;
        }
        if (context.getSectionState() == null) {
            context.setSectionState(new xbb.ai.erp.module.customer.application.pojo.CustomerSectionStatePojo());
        }
        filtered.setContacts(isOpen(context.getSectionState().getContacts(), context.getExt().getContacts()) ? context.getExt().getContacts() : List.of());
        filtered.setAddresses(isOpen(context.getSectionState().getAddresses(), context.getExt().getAddresses()) ? context.getExt().getAddresses() : List.of());
        filtered.setBankAccounts(isOpen(context.getSectionState().getBankAccounts(), context.getExt().getBankAccounts()) ? context.getExt().getBankAccounts() : List.of());
        filtered.setInvoiceProfiles(isOpen(context.getSectionState().getInvoiceProfiles(), context.getExt().getInvoiceProfiles()) ? context.getExt().getInvoiceProfiles() : List.of());
        return filtered;
    }

    private boolean isOpen(Integer value, List<?> rows) {
        if (value != null) {
            return Integer.valueOf(1).equals(value);
        }
        return rows != null && !rows.isEmpty();
    }

    private void applyCustomerDefaults(Customer customer) {
        long now = System.currentTimeMillis();
        if (customer.getCustomerCategory() == null || customer.getCustomerCategory().isBlank()) {
            customer.setCustomerCategory("A");
        }
        if (customer.getBizStatus() == null || customer.getBizStatus().isBlank()) {
            customer.setBizStatus("1");
        }
        if (customer.getRefStatus() == null || customer.getRefStatus().isBlank()) {
            customer.setRefStatus("0");
        }
        if (customer.getVersion() == null) {
            customer.setVersion(0);
        }
        if (customer.getDel() == null) {
            customer.setDel(0);
        }
        if (customer.getAddTime() == null) {
            customer.setAddTime(now);
        }
        if (customer.getUpdateTime() == null) {
            customer.setUpdateTime(now);
        }
    }

    private void applyContactDefaults(CustomerContact contact, String userId) {
        long now = System.currentTimeMillis();
        if (contact.getBizStatus() == null || contact.getBizStatus().isBlank()) {
            contact.setBizStatus("1");
        }
        if (contact.getVersion() == null) {
            contact.setVersion(0);
        }
        if (contact.getDel() == null) {
            contact.setDel(0);
        }
        if (contact.getAddTime() == null) {
            contact.setAddTime(now);
        }
        if (contact.getUpdateTime() == null) {
            contact.setUpdateTime(now);
        }
        if (contact.getCreatorId() == null || contact.getCreatorId().isBlank()) {
            contact.setCreatorId(userId);
        }
        if (contact.getModifyId() == null || contact.getModifyId().isBlank()) {
            contact.setModifyId(userId);
        }
    }

    private void applyAddressDefaults(CustomerAddress address, String userId) {
        long now = System.currentTimeMillis();
        if (address.getBizStatus() == null || address.getBizStatus().isBlank()) {
            address.setBizStatus("1");
        }
        if (address.getVersion() == null) {
            address.setVersion(0);
        }
        if (address.getDel() == null) {
            address.setDel(0);
        }
        if (address.getAddTime() == null) {
            address.setAddTime(now);
        }
        if (address.getUpdateTime() == null) {
            address.setUpdateTime(now);
        }
        if (address.getCreatorId() == null || address.getCreatorId().isBlank()) {
            address.setCreatorId(userId);
        }
        if (address.getModifyId() == null || address.getModifyId().isBlank()) {
            address.setModifyId(userId);
        }
    }

    private void applyBankAccountDefaults(CustomerBankAccount bankAccount, String userId) {
        long now = System.currentTimeMillis();
        if (bankAccount.getBizStatus() == null || bankAccount.getBizStatus().isBlank()) {
            bankAccount.setBizStatus("1");
        }
        if (bankAccount.getVersion() == null) {
            bankAccount.setVersion(0);
        }
        if (bankAccount.getDel() == null) {
            bankAccount.setDel(0);
        }
        if (bankAccount.getAddTime() == null) {
            bankAccount.setAddTime(now);
        }
        if (bankAccount.getUpdateTime() == null) {
            bankAccount.setUpdateTime(now);
        }
        if (bankAccount.getCreatorId() == null || bankAccount.getCreatorId().isBlank()) {
            bankAccount.setCreatorId(userId);
        }
        if (bankAccount.getModifyId() == null || bankAccount.getModifyId().isBlank()) {
            bankAccount.setModifyId(userId);
        }
    }

    private void applyInvoiceProfileDefaults(CustomerInvoiceProfile invoiceProfile, String userId) {
        long now = System.currentTimeMillis();
        if (invoiceProfile.getBizStatus() == null || invoiceProfile.getBizStatus().isBlank()) {
            invoiceProfile.setBizStatus("1");
        }
        if (invoiceProfile.getVersion() == null) {
            invoiceProfile.setVersion(0);
        }
        if (invoiceProfile.getDel() == null) {
            invoiceProfile.setDel(0);
        }
        if (invoiceProfile.getAddTime() == null) {
            invoiceProfile.setAddTime(now);
        }
        if (invoiceProfile.getUpdateTime() == null) {
            invoiceProfile.setUpdateTime(now);
        }
        if (invoiceProfile.getCreatorId() == null || invoiceProfile.getCreatorId().isBlank()) {
            invoiceProfile.setCreatorId(userId);
        }
        if (invoiceProfile.getModifyId() == null || invoiceProfile.getModifyId().isBlank()) {
            invoiceProfile.setModifyId(userId);
        }
    }

    private <T> void validateDefaultUniqueness(List<T> list, Function<T, Integer> getter, String message) {
        long count = list == null ? 0 : list.stream().filter(item -> Integer.valueOf(1).equals(getter.apply(item))).count();
        if (count > 1) {
            throw new BizException(message);
        }
    }

    private void syncContacts(String corpid, Long customerId, String userId, List<CustomerContactItemDTO> items) {
        if (items == null) {
            return;
        }
        CustomerContactQueryPojo queryPojo = new CustomerContactQueryPojo();
        queryPojo.setCorpid(corpid);
        queryPojo.setCustomerId(customerId);
        List<CustomerContact> existingList = customerContactRepository.findByCondition(queryPojo);
        Set<Long> incomingIds = items.stream()
            .map(CustomerContactItemDTO::getId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        for (CustomerContact existing : existingList) {
            if (!incomingIds.contains(existing.getId())) {
                if (Integer.valueOf(1).equals(existing.getDefaultFlag())) {
                    throw new BizException("默认联系人不允许通过整单保存删除");
                }
                customerContactRepository.removeById(corpid, existing.getId());
            }
        }
        for (CustomerContactItemDTO item : items) {
            CustomerContact contact = CustomerAdminAssembler.toCustomerContact(corpid, customerId, item);
            if (contact.getId() == null) {
                applyContactDefaults(contact, userId);
                customerContactRepository.insert(contact);
            } else {
                customerContactRepository.update(contact);
            }
        }
    }

    private void syncAddresses(String corpid, Long customerId, String userId, List<CustomerAddressItemDTO> items) {
        if (items == null) {
            return;
        }
        CustomerAddressQueryPojo queryPojo = new CustomerAddressQueryPojo();
        queryPojo.setCorpid(corpid);
        queryPojo.setCustomerId(customerId);
        List<CustomerAddress> existingList = customerAddressRepository.findByCondition(queryPojo);
        Set<Long> incomingIds = items.stream()
            .map(CustomerAddressItemDTO::getId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        for (CustomerAddress existing : existingList) {
            if (!incomingIds.contains(existing.getId())) {
                if (Integer.valueOf(1).equals(existing.getDefaultFlag())) {
                    throw new BizException("默认地址不允许通过整单保存删除");
                }
                customerAddressRepository.removeById(corpid, existing.getId());
            }
        }
        for (CustomerAddressItemDTO item : items) {
            CustomerAddress address = CustomerAdminAssembler.toCustomerAddress(corpid, customerId, item);
            if (address.getId() == null) {
                applyAddressDefaults(address, userId);
                customerAddressRepository.insert(address);
            } else {
                customerAddressRepository.update(address);
            }
        }
    }

    private void syncBankAccounts(String corpid, Long customerId, String userId, List<CustomerBankAccountItemDTO> items) {
        if (items == null) {
            return;
        }
        CustomerBankAccountQueryPojo queryPojo = new CustomerBankAccountQueryPojo();
        queryPojo.setCorpid(corpid);
        queryPojo.setCustomerId(customerId);
        List<CustomerBankAccount> existingList = customerBankAccountRepository.findByCondition(queryPojo);
        Set<Long> incomingIds = items.stream()
            .map(CustomerBankAccountItemDTO::getId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        for (CustomerBankAccount existing : existingList) {
            if (!incomingIds.contains(existing.getId())) {
                if (Integer.valueOf(1).equals(existing.getDefaultFlag())) {
                    throw new BizException("默认银行账户不允许通过整单保存删除");
                }
                customerBankAccountRepository.removeById(corpid, existing.getId());
            }
        }
        for (CustomerBankAccountItemDTO item : items) {
            CustomerBankAccount bankAccount = CustomerAdminAssembler.toCustomerBankAccount(corpid, customerId, item);
            if (bankAccount.getId() == null) {
                applyBankAccountDefaults(bankAccount, userId);
                customerBankAccountRepository.insert(bankAccount);
            } else {
                customerBankAccountRepository.update(bankAccount);
            }
        }
    }

    private void syncInvoiceProfiles(String corpid, Long customerId, String userId, List<CustomerInvoiceProfileItemDTO> items) {
        if (items == null) {
            return;
        }
        CustomerInvoiceProfileQueryPojo queryPojo = new CustomerInvoiceProfileQueryPojo();
        queryPojo.setCorpid(corpid);
        queryPojo.setCustomerId(customerId);
        List<CustomerInvoiceProfile> existingList = customerInvoiceProfileRepository.findByCondition(queryPojo);
        Set<Long> incomingIds = items.stream()
            .map(CustomerInvoiceProfileItemDTO::getId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        for (CustomerInvoiceProfile existing : existingList) {
            if (!incomingIds.contains(existing.getId())) {
                if (Integer.valueOf(1).equals(existing.getDefaultFlag())) {
                    throw new BizException("默认开票信息不允许通过整单保存删除");
                }
                customerInvoiceProfileRepository.removeById(corpid, existing.getId());
            }
        }
        for (CustomerInvoiceProfileItemDTO item : items) {
            CustomerInvoiceProfile profile = CustomerAdminAssembler.toCustomerInvoiceProfile(corpid, customerId, item);
            if (profile.getId() == null) {
                applyInvoiceProfileDefaults(profile, userId);
                customerInvoiceProfileRepository.insert(profile);
            } else {
                customerInvoiceProfileRepository.update(profile);
            }
        }
    }
}
