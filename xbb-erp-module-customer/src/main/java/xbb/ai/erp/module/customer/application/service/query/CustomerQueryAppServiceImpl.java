package xbb.ai.erp.module.customer.application.service.query;

import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.customer.application.schema.CustomerListQueryAdapter;
import xbb.ai.erp.module.customer.admin.dto.CustomerListDTO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDetailVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerListItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerSaveItemVO;
import xbb.ai.erp.module.customer.application.assembler.CustomerAdminAssembler;
import xbb.ai.erp.module.customer.application.assembler.CustomerFieldAssembler;
import xbb.ai.erp.module.customer.application.field.CustomerFieldFactory;
import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.domain.model.CustomerAddress;
import xbb.ai.erp.module.customer.domain.model.CustomerBankAccount;
import xbb.ai.erp.module.customer.domain.model.CustomerContact;
import xbb.ai.erp.module.customer.domain.model.CustomerInvoiceProfile;
import xbb.ai.erp.module.customer.domain.pojo.CustomerAddressQueryPojo;
import xbb.ai.erp.module.customer.domain.pojo.CustomerBankAccountQueryPojo;
import xbb.ai.erp.module.customer.domain.pojo.CustomerContactQueryPojo;
import xbb.ai.erp.module.customer.domain.pojo.CustomerInvoiceProfileQueryPojo;
import xbb.ai.erp.module.customer.domain.pojo.CustomerQueryPojo;
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
import java.util.function.Function;
import java.util.stream.Collectors;

public class CustomerQueryAppServiceImpl implements CustomerQueryAppService {

    private final CustomerRepository customerRepository;
    private final CustomerContactRepository customerContactRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final CustomerBankAccountRepository customerBankAccountRepository;
    private final CustomerInvoiceProfileRepository customerInvoiceProfileRepository;
    private final CustomerFieldFactory customerFieldFactory;
    private final CustomerListQueryAdapter customerListQueryAdapter;

    public CustomerQueryAppServiceImpl(
        CustomerRepository customerRepository,
        CustomerContactRepository customerContactRepository,
        CustomerAddressRepository customerAddressRepository,
        CustomerBankAccountRepository customerBankAccountRepository,
        CustomerInvoiceProfileRepository customerInvoiceProfileRepository,
        CustomerFieldFactory customerFieldFactory,
        CustomerListQueryAdapter customerListQueryAdapter
    ) {
        this.customerRepository = customerRepository;
        this.customerContactRepository = customerContactRepository;
        this.customerAddressRepository = customerAddressRepository;
        this.customerBankAccountRepository = customerBankAccountRepository;
        this.customerInvoiceProfileRepository = customerInvoiceProfileRepository;
        this.customerFieldFactory = customerFieldFactory;
        this.customerListQueryAdapter = customerListQueryAdapter;
    }

    @Override
    public ListBaseVO<CustomerListItemVO> list(CustomerListDTO dto) {
        CustomerQueryPojo fullQuery = customerListQueryAdapter.toQueryPojo(dto);
        List<Customer> allMatchedCustomers = customerRepository == null ? List.of() : customerRepository.findByCondition(fullQuery);

        CustomerQueryPojo pagedQuery = customerListQueryAdapter.toQueryPojo(dto);
        pagedQuery.setPageNum(dto.getPageNum());
        pagedQuery.setPageSize(dto.getPageSize());
        List<Customer> customers = customerRepository == null ? List.of() : customerRepository.findByCondition(pagedQuery);

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
    public CustomerDetailVO detail(IdBaseDTO dto) {
        return CustomerAdminAssembler.toDetailVO(loadSaveItem(dto));
    }

    private CustomerSaveItemVO loadSaveItem(IdBaseDTO dto) {
        if (dto.getCorpid() == null || dto.getCorpid().isBlank()) {
            throw new BizException("公司不能为空");
        }
        if (dto.getId() == null) {
            throw new BizException("id不能为空");
        }
        CustomerContactQueryPojo childQuery = new CustomerContactQueryPojo();
        childQuery.setCorpid(dto.getCorpid());
        childQuery.setCustomerId(dto.getId());
        CustomerAddressQueryPojo addressQuery = new CustomerAddressQueryPojo();
        addressQuery.setCorpid(dto.getCorpid());
        addressQuery.setCustomerId(dto.getId());
        CustomerBankAccountQueryPojo bankAccountQuery = new CustomerBankAccountQueryPojo();
        bankAccountQuery.setCorpid(dto.getCorpid());
        bankAccountQuery.setCustomerId(dto.getId());
        CustomerInvoiceProfileQueryPojo invoiceProfileQuery = new CustomerInvoiceProfileQueryPojo();
        invoiceProfileQuery.setCorpid(dto.getCorpid());
        invoiceProfileQuery.setCustomerId(dto.getId());
        Customer customer = customerRepository == null ? null : customerRepository.findById(dto.getCorpid(), dto.getId());
        List<CustomerContact> contacts = customerContactRepository == null ? List.of() : customerContactRepository.findByCondition(childQuery);
        List<CustomerAddress> addresses = customerAddressRepository == null ? List.of() : customerAddressRepository.findByCondition(addressQuery);
        List<CustomerBankAccount> bankAccounts = customerBankAccountRepository == null ? List.of() : customerBankAccountRepository.findByCondition(bankAccountQuery);
        List<CustomerInvoiceProfile> invoiceProfiles = customerInvoiceProfileRepository == null ? List.of() : customerInvoiceProfileRepository.findByCondition(invoiceProfileQuery);
        return CustomerAdminAssembler.toSaveItemVO(customer, contacts, addresses, bankAccounts, invoiceProfiles);
    }

    private Map<Long, CustomerContact> loadDefaultContactMap(String corpid, List<Long> customerIds) {
        if (customerContactRepository == null || customerIds == null || customerIds.isEmpty()) {
            return Map.of();
        }
        CustomerContactQueryPojo queryPojo = new CustomerContactQueryPojo();
        queryPojo.setCorpid(corpid);
        queryPojo.setCustomerIds(customerIds);
        queryPojo.setDefaultFlag(1);
        return customerContactRepository.findByCondition(queryPojo)
            .stream()
            .filter(item -> item.getCustomerId() != null)
            .collect(Collectors.toMap(CustomerContact::getCustomerId, Function.identity(), (left, right) -> left));
    }

    private Map<Long, CustomerAddress> loadDefaultAddressMap(String corpid, List<Long> customerIds) {
        if (customerAddressRepository == null || customerIds == null || customerIds.isEmpty()) {
            return Map.of();
        }
        CustomerAddressQueryPojo queryPojo = new CustomerAddressQueryPojo();
        queryPojo.setCorpid(corpid);
        queryPojo.setCustomerIds(customerIds);
        queryPojo.setDefaultFlag(1);
        return customerAddressRepository.findByCondition(queryPojo)
            .stream()
            .filter(item -> item.getCustomerId() != null)
            .collect(Collectors.toMap(CustomerAddress::getCustomerId, Function.identity(), (left, right) -> left));
    }

    private Map<Long, CustomerInvoiceProfile> loadDefaultInvoiceProfileMap(String corpid, List<Long> customerIds) {
        if (customerInvoiceProfileRepository == null || customerIds == null || customerIds.isEmpty()) {
            return Map.of();
        }
        CustomerInvoiceProfileQueryPojo queryPojo = new CustomerInvoiceProfileQueryPojo();
        queryPojo.setCorpid(corpid);
        queryPojo.setCustomerIds(customerIds);
        queryPojo.setDefaultFlag(1);
        return customerInvoiceProfileRepository.findByCondition(queryPojo)
            .stream()
            .filter(item -> item.getCustomerId() != null)
            .collect(Collectors.toMap(CustomerInvoiceProfile::getCustomerId, Function.identity(), (left, right) -> left));
    }
}
