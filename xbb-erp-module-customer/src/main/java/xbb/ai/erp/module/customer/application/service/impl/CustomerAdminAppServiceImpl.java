package xbb.ai.erp.module.customer.application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.common.application.filter.ListFilterConditionBuilder;
import xbb.ai.erp.module.customer.admin.dto.CustomerAddressItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerBankAccountItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerContactItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftListDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftLoadDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftSaveDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerInvoiceProfileItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerListDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSaveDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSubmitSaveDTO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDetailVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDraftDetailVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDraftListItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDraftSaveVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerListItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerSaveItemVO;
import xbb.ai.erp.module.customer.application.assembler.CustomerAdminAssembler;
import xbb.ai.erp.module.customer.application.assembler.CustomerFieldAssembler;
import xbb.ai.erp.module.customer.application.pojo.CustomerSaveContextPojo;
import xbb.ai.erp.module.customer.application.pojo.CustomerSaveDraftPojo;
import xbb.ai.erp.module.customer.application.pojo.CustomerSaveExtPojo;
import xbb.ai.erp.module.customer.application.provider.CustomerListMetaProvider;
import xbb.ai.erp.module.customer.application.service.CustomerAdminAppService;
import xbb.ai.erp.module.customer.application.validator.CustomerSaveBusinessValidator;
import xbb.ai.erp.module.customer.application.validator.CustomerSaveCommonValidator;
import xbb.ai.erp.module.customer.application.validator.CustomerSaveProtocolValidator;
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
import xbb.ai.erp.module.customer.domain.repository.CustomerDraftRepository;
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
    private final CustomerDraftRepository customerDraftRepository;
    private final CustomerFieldFactory customerFieldFactory;
    private final CustomerSaveProtocolValidator protocolValidator;
    private final CustomerSaveCommonValidator commonValidator;
    private final CustomerSaveBusinessValidator businessValidator;
    private final ListFilterConditionBuilder listFilterConditionBuilder;
    private final CustomerListMetaProvider customerListMetaProvider;


    @Autowired
    public CustomerAdminAppServiceImpl(
        CustomerRepository customerRepository,
        CustomerContactRepository customerContactRepository,
        CustomerAddressRepository customerAddressRepository,
        CustomerBankAccountRepository customerBankAccountRepository,
        CustomerInvoiceProfileRepository customerInvoiceProfileRepository,
        CustomerDraftRepository customerDraftRepository,
        CustomerFieldFactory customerFieldFactory,
        CustomerListMetaProvider customerListMetaProvider
    ) {
        this.customerRepository = customerRepository;
        this.customerContactRepository = customerContactRepository;
        this.customerAddressRepository = customerAddressRepository;
        this.customerBankAccountRepository = customerBankAccountRepository;
        this.customerInvoiceProfileRepository = customerInvoiceProfileRepository;
        this.customerDraftRepository = customerDraftRepository;
        this.customerFieldFactory = customerFieldFactory;
        this.customerListMetaProvider = customerListMetaProvider;
        this.protocolValidator = new CustomerSaveProtocolValidator();
        this.commonValidator = new CustomerSaveCommonValidator();
        this.businessValidator = new CustomerSaveBusinessValidator(customerRepository);
        this.listFilterConditionBuilder = new ListFilterConditionBuilder();
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
            null,
            customerFieldFactory,
            new CustomerListMetaProvider(customerFieldFactory)
        );
    }

    public static CustomerAdminAppServiceImpl forTesting(
        CustomerRepository customerRepository,
        CustomerContactRepository customerContactRepository,
        CustomerAddressRepository customerAddressRepository,
        CustomerBankAccountRepository customerBankAccountRepository,
        CustomerInvoiceProfileRepository customerInvoiceProfileRepository
    ) {
        return forTesting(
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
        CustomerDraftRepository customerDraftRepository
    ) {
        CustomerFieldFactory customerFieldFactory = new DefaultCustomerFieldFactory(List.of());
        return new CustomerAdminAppServiceImpl(
            customerRepository,
            customerContactRepository,
            customerAddressRepository,
            customerBankAccountRepository,
            customerInvoiceProfileRepository,
            customerDraftRepository,
            customerFieldFactory,
            new CustomerListMetaProvider(customerFieldFactory)
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

        List<Long> customerIds = customers.stream()
            .map(Customer::getId)
            .filter(Objects::nonNull)
            .toList();
        Map<Long, CustomerContact> defaultContactMap = loadDefaultContactMap(dto.getCorpid(), customerIds);
        Map<Long, CustomerAddress> defaultAddressMap = loadDefaultAddressMap(dto.getCorpid(), customerIds);
        Map<Long, CustomerInvoiceProfile> defaultInvoiceProfileMap = loadDefaultInvoiceProfileMap(dto.getCorpid(), customerIds);

        List<CustomerListItemVO> list = customers.stream().map(customer ->
            CustomerAdminAssembler.toListItemVO(
                customer,
                defaultContactMap.get(customer.getId()),
                defaultAddressMap.get(customer.getId()),
                defaultInvoiceProfileMap.get(customer.getId())
            )
        ).toList();

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
        conditionMap.put("conditions", listFilterConditionBuilder.build(dto.getConditions(), customerListMetaProvider.conditionMetaMap()));
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
        vo.setData(loadSaveItem(dto));
        return vo;
    }

    @Override
    public CustomerDraftSaveVO saveDraft(CustomerDraftSaveDTO dto) {
        CustomerSaveContextPojo context = CustomerAdminAssembler.toDraftContext(dto);
        protocolValidator.validate(context);
        commonValidator.validateForDraft(context);
        CustomerSaveDraftPojo draft = CustomerAdminAssembler.toDraftPojo(dto);
        String draftCode = customerDraftRepository.saveDraft(draft);
        if (dto.getDraftMeta() != null) {
            dto.getDraftMeta().setDraftCode(draftCode);
        }
        CustomerDraftSaveVO vo = new CustomerDraftSaveVO();
        vo.setDraftCode(draftCode);
        return vo;
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
    public List<CustomerDraftListItemVO> draftList(CustomerDraftListDTO dto) {
        if (customerDraftRepository == null) {
            return List.of();
        }
        return customerDraftRepository.listDrafts(dto.getCorpid(), 10).stream()
            .map(CustomerAdminAssembler::toDraftListItemVO)
            .toList();
    }

    @Override
    public CustomerDraftDetailVO loadDraft(CustomerDraftLoadDTO dto) {
        if (customerDraftRepository == null) {
            return new CustomerDraftDetailVO();
        }
        return CustomerAdminAssembler.toDraftDetailVO(
            customerDraftRepository.loadDraft(dto.getCorpid(), dto.getDraftCode())
        );
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

    @Override
    public CustomerDetailVO detail(IdBaseDTO dto) {
        return CustomerAdminAssembler.toDetailVO(loadSaveItem(dto));
    }

    private CustomerSaveItemVO loadSaveItem(IdBaseDTO dto) {
        Customer customer = customerRepository == null ? null : customerRepository.findById(dto.getCorpid(), dto.getId());
        List<CustomerContact> contacts = customerContactRepository == null ? List.of() : customerContactRepository.findByCondition(Map.of("corpid", dto.getCorpid(), "customerId", dto.getId()));
        List<CustomerAddress> addresses = customerAddressRepository == null ? List.of() : customerAddressRepository.findByCondition(Map.of("corpid", dto.getCorpid(), "customerId", dto.getId()));
        List<CustomerBankAccount> bankAccounts = customerBankAccountRepository == null ? List.of() : customerBankAccountRepository.findByCondition(Map.of("corpid", dto.getCorpid(), "customerId", dto.getId()));
        List<CustomerInvoiceProfile> invoiceProfiles = customerInvoiceProfileRepository == null ? List.of() : customerInvoiceProfileRepository.findByCondition(Map.of("corpid", dto.getCorpid(), "customerId", dto.getId()));
        return CustomerAdminAssembler.toSaveItemVO(customer, contacts, addresses, bankAccounts, invoiceProfiles);
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
        List<CustomerContact> existingList = customerContactRepository.findByCondition(Map.of("corpid", corpid, "customerId", customerId));
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
        List<CustomerAddress> existingList = customerAddressRepository.findByCondition(Map.of("corpid", corpid, "customerId", customerId));
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
        List<CustomerBankAccount> existingList = customerBankAccountRepository.findByCondition(Map.of("corpid", corpid, "customerId", customerId));
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
        List<CustomerInvoiceProfile> existingList = customerInvoiceProfileRepository.findByCondition(Map.of("corpid", corpid, "customerId", customerId));
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

    private Map<Long, CustomerContact> loadDefaultContactMap(String corpid, List<Long> customerIds) {
        if (customerContactRepository == null || customerIds == null || customerIds.isEmpty()) {
            return Map.of();
        }
        return customerContactRepository.findByCondition(Map.of("corpid", corpid, "customerIds", customerIds, "defaultFlag", 1))
            .stream()
            .filter(item -> item.getCustomerId() != null)
            .collect(Collectors.toMap(CustomerContact::getCustomerId, Function.identity(), (left, right) -> left));
    }

    private Map<Long, CustomerAddress> loadDefaultAddressMap(String corpid, List<Long> customerIds) {
        if (customerAddressRepository == null || customerIds == null || customerIds.isEmpty()) {
            return Map.of();
        }
        return customerAddressRepository.findByCondition(Map.of("corpid", corpid, "customerIds", customerIds, "defaultFlag", 1))
            .stream()
            .filter(item -> item.getCustomerId() != null)
            .collect(Collectors.toMap(CustomerAddress::getCustomerId, Function.identity(), (left, right) -> left));
    }

    private Map<Long, CustomerInvoiceProfile> loadDefaultInvoiceProfileMap(String corpid, List<Long> customerIds) {
        if (customerInvoiceProfileRepository == null || customerIds == null || customerIds.isEmpty()) {
            return Map.of();
        }
        return customerInvoiceProfileRepository.findByCondition(Map.of("corpid", corpid, "customerIds", customerIds, "defaultFlag", 1))
            .stream()
            .filter(item -> item.getCustomerId() != null)
            .collect(Collectors.toMap(CustomerInvoiceProfile::getCustomerId, Function.identity(), (left, right) -> left));
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
