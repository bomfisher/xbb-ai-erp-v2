package xbb.ai.erp.module.customer.application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.customer.application.schema.CustomerListQueryAdapter;
import xbb.ai.erp.module.customer.application.schema.CustomerListSchemaProvider;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftListDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftLoadDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftSaveDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerListDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSaveDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSubmitSaveDTO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDetailVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDraftDetailVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDraftListItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDraftSaveVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerListItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerSaveItemVO;
import xbb.ai.erp.module.customer.application.field.CustomerFieldFactory;
import xbb.ai.erp.module.customer.application.field.DefaultCustomerFieldFactory;
import xbb.ai.erp.module.customer.application.port.CustomerDraftRepository;
import xbb.ai.erp.module.customer.application.service.CustomerAdminAppService;
import xbb.ai.erp.module.customer.application.service.delete.CustomerDeleteAppService;
import xbb.ai.erp.module.customer.application.service.delete.CustomerDeleteAppServiceImpl;
import xbb.ai.erp.module.customer.application.service.draft.CustomerDraftAppService;
import xbb.ai.erp.module.customer.application.service.draft.CustomerDraftAppServiceImpl;
import xbb.ai.erp.module.customer.application.service.query.CustomerQueryAppService;
import xbb.ai.erp.module.customer.application.service.query.CustomerQueryAppServiceImpl;
import xbb.ai.erp.module.customer.application.service.save.CustomerSaveAppService;
import xbb.ai.erp.module.customer.application.service.save.CustomerSaveAppServiceImpl;
import xbb.ai.erp.module.customer.domain.repository.CustomerAddressRepository;
import xbb.ai.erp.module.customer.domain.repository.CustomerBankAccountRepository;
import xbb.ai.erp.module.customer.domain.repository.CustomerContactRepository;
import xbb.ai.erp.module.customer.domain.repository.CustomerInvoiceProfileRepository;
import xbb.ai.erp.module.customer.domain.repository.CustomerRepository;

import java.util.List;

@Service
public class CustomerAdminAppServiceImpl implements CustomerAdminAppService {

    private final CustomerQueryAppService queryAppService;
    private final CustomerDraftAppService draftAppService;
    private final CustomerSaveAppService saveAppService;
    private final CustomerDeleteAppService deleteAppService;

    @Autowired
    public CustomerAdminAppServiceImpl(
        CustomerRepository customerRepository,
        CustomerContactRepository customerContactRepository,
        CustomerAddressRepository customerAddressRepository,
        CustomerBankAccountRepository customerBankAccountRepository,
        CustomerInvoiceProfileRepository customerInvoiceProfileRepository,
        CustomerDraftRepository customerDraftRepository,
        CustomerFieldFactory customerFieldFactory,
        CustomerListQueryAdapter customerListQueryAdapter
    ) {
        this.queryAppService = new CustomerQueryAppServiceImpl(
            customerRepository,
            customerContactRepository,
            customerAddressRepository,
            customerBankAccountRepository,
            customerInvoiceProfileRepository,
            customerFieldFactory,
            customerListQueryAdapter
        );
        this.draftAppService = new CustomerDraftAppServiceImpl(customerDraftRepository);
        this.saveAppService = new CustomerSaveAppServiceImpl(
            customerRepository,
            customerContactRepository,
            customerAddressRepository,
            customerBankAccountRepository,
            customerInvoiceProfileRepository,
            customerDraftRepository
        );
        this.deleteAppService = new CustomerDeleteAppServiceImpl(
            customerRepository,
            customerContactRepository,
            customerAddressRepository,
            customerBankAccountRepository,
            customerInvoiceProfileRepository
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
            null,
            customerFieldFactory,
            new CustomerListQueryAdapter(new CustomerListSchemaProvider())
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
            new CustomerListQueryAdapter(new CustomerListSchemaProvider())
        );
    }

    @Override
    public ListBaseVO<CustomerListItemVO> list(CustomerListDTO dto) {
        return queryAppService.list(dto);
    }

    @Override
    public SaveItemVO<CustomerSaveItemVO> addItem(BaseDTO dto) {
        return queryAppService.addItem(dto);
    }

    @Override
    public SaveItemVO<CustomerSaveItemVO> updateItem(IdBaseDTO dto) {
        return queryAppService.updateItem(dto);
    }

    @Override
    public CustomerDraftSaveVO saveDraft(CustomerDraftSaveDTO dto) {
        return draftAppService.saveDraft(dto);
    }

    @Override
    public BaseVO saveAndSubmit(CustomerSubmitSaveDTO dto) {
        return saveAppService.saveAndSubmit(dto);
    }

    @Override
    public List<CustomerDraftListItemVO> draftList(CustomerDraftListDTO dto) {
        return draftAppService.draftList(dto);
    }

    @Override
    public CustomerDraftDetailVO loadDraft(CustomerDraftLoadDTO dto) {
        return draftAppService.loadDraft(dto);
    }

    @Override
    public Long save(CustomerSaveDTO dto) {
        return saveAppService.save(dto);
    }

    @Override
    public CustomerDetailVO detail(IdBaseDTO dto) {
        return queryAppService.detail(dto);
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        deleteAppService.delete(dto);
    }
}
