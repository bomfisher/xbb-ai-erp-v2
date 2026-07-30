package xbb.ai.erp.module.customer.application.assembler;

import xbb.ai.erp.module.customer.admin.dto.CustomerAddressItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerBankAccountItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerContactItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerInvoiceProfileItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftMetaDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftSaveDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerMainDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSaveDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSaveExtDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSectionStateDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSubmitSaveDTO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDetailVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDraftDetailVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDraftListItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerDraftMetaVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerListItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerSaveExtVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerSaveItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerSectionStateVO;
import xbb.ai.erp.module.customer.application.pojo.CustomerDraftMetaPojo;
import xbb.ai.erp.module.customer.application.pojo.CustomerSaveContextPojo;
import xbb.ai.erp.module.customer.application.pojo.CustomerSaveDraftPojo;
import xbb.ai.erp.module.customer.application.pojo.CustomerSaveExtPojo;
import xbb.ai.erp.module.customer.application.pojo.CustomerSectionStatePojo;
import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.domain.model.CustomerAddress;
import xbb.ai.erp.module.customer.domain.model.CustomerBankAccount;
import xbb.ai.erp.module.customer.domain.model.CustomerContact;
import xbb.ai.erp.module.customer.domain.model.CustomerInvoiceProfile;

import java.util.List;

public final class CustomerAdminAssembler {

    private CustomerAdminAssembler() {
    }

    public static CustomerSaveItemVO buildEmptySaveItemVO() {
        CustomerSaveItemVO vo = new CustomerSaveItemVO();
        vo.setSectionState(defaultSectionState());
        return vo;
    }

    public static CustomerSaveContextPojo toDraftContext(CustomerDraftSaveDTO dto) {
        CustomerSaveContextPojo context = new CustomerSaveContextPojo();
        context.setCorpid(dto.getCorpid());
        context.setMain(dto.getMain());
        context.setExt(toSaveExtPojo(dto.getExt()));
        context.setSectionState(toSectionStatePojo(dto.getSectionState()));
        context.setDraftMeta(toDraftMetaPojo(dto.getDraftMeta()));
        context.setSubmitMode(0);
        return context;
    }

    public static CustomerSaveContextPojo toSubmitContext(CustomerSubmitSaveDTO dto) {
        CustomerSaveContextPojo context = new CustomerSaveContextPojo();
        context.setCorpid(dto.getCorpid());
        context.setMain(dto.getMain());
        context.setExt(toSaveExtPojo(dto.getExt()));
        context.setSectionState(toSectionStatePojo(dto.getSectionState()));
        context.setDraftMeta(toDraftMetaPojo(dto.getDraftMeta()));
        context.setSubmitMode(1);
        return context;
    }

    public static CustomerSaveDraftPojo toDraftPojo(CustomerDraftSaveDTO dto) {
        CustomerSaveDraftPojo draft = new CustomerSaveDraftPojo();
        draft.setCorpid(dto.getCorpid());
        draft.setDraftCode(dto.getDraftMeta() == null ? null : dto.getDraftMeta().getDraftCode());
        draft.setDraftTitle(dto.getDraftMeta() == null ? null : dto.getDraftMeta().getDraftTitle());
        draft.setUpdatedTime(dto.getDraftMeta() == null ? null : dto.getDraftMeta().getUpdatedTime());
        draft.setMain(dto.getMain());
        draft.setExt(toSaveExtPojo(dto.getExt()));
        draft.setSectionState(toSectionStatePojo(dto.getSectionState()));
        return draft;
    }

    public static CustomerDraftListItemVO toDraftListItemVO(CustomerSaveDraftPojo pojo) {
        CustomerDraftListItemVO vo = new CustomerDraftListItemVO();
        vo.setDraftCode(pojo.getDraftCode());
        vo.setDraftTitle(pojo.getDraftTitle());
        if (pojo.getMain() != null) {
            vo.setCustomerCode(pojo.getMain().getCustomerCode());
            vo.setCustomerName(pojo.getMain().getCustomerName());
        }
        vo.setUpdatedTime(pojo.getUpdatedTime());
        return vo;
    }

    public static CustomerDraftDetailVO toDraftDetailVO(CustomerSaveDraftPojo pojo) {
        CustomerDraftDetailVO vo = new CustomerDraftDetailVO();
        if (pojo == null) {
            return vo;
        }
        if (pojo.getMain() != null) {
            vo.setMain(pojo.getMain());
        }
        if (pojo.getExt() != null) {
            vo.setExt(toSaveExtVO(pojo.getExt()));
        }
        if (pojo.getSectionState() != null) {
            vo.setSectionState(toSectionStateVO(pojo.getSectionState()));
        }
        vo.setDraftMeta(toDraftMetaVO(pojo));
        return vo;
    }

    public static Customer toCustomer(CustomerSaveDTO dto) {
        Customer customer = new Customer();
        CustomerMainDTO main = dto.getMain();
        if (main != null) {
            customer.setId(main.getId());
            customer.setCustomerCode(main.getCustomerCode());
            customer.setCustomerName(main.getCustomerName());
            customer.setCustomerShortName(main.getCustomerShortName());
            customer.setCustomerCategory(main.getCustomerCategory());
            customer.setRegionCode(main.getRegionCode());
            customer.setOwnerSalesId(main.getOwnerSalesId());
            customer.setOwnerSalesNameSnapshot(main.getOwnerSalesNameSnapshot());
            customer.setBizStatus(main.getBizStatus());
            customer.setRefStatus(main.getRefStatus());
            customer.setDefaultContactId(main.getDefaultContactId());
            customer.setDefaultAddressId(main.getDefaultAddressId());
            customer.setDefaultBankAccountId(main.getDefaultBankAccountId());
            customer.setDefaultInvoiceProfileId(main.getDefaultInvoiceProfileId());
            customer.setRemark(main.getRemark());
            customer.setVersion(main.getVersion());
        }
        customer.setCorpid(dto.getCorpid());
        customer.setCreatorId(dto.getUserId());
        customer.setModifyId(dto.getUserId());
        return customer;
    }

    public static CustomerContact toCustomerContact(String corpid, Long customerId, CustomerContactItemDTO item) {
        CustomerContact contact = new CustomerContact();
        contact.setId(item.getId());
        contact.setCorpid(corpid);
        contact.setCustomerId(customerId);
        contact.setContactName(item.getContactName());
        contact.setMobile(item.getMobile());
        contact.setPhone(item.getPhone());
        contact.setEmail(item.getEmail());
        contact.setPositionName(item.getPositionName());
        contact.setDefaultFlag(item.getDefaultFlag());
        contact.setBizStatus(item.getBizStatus());
        contact.setRemark(item.getRemark());
        contact.setVersion(item.getVersion());
        return contact;
    }

    public static CustomerAddress toCustomerAddress(String corpid, Long customerId, CustomerAddressItemDTO item) {
        CustomerAddress address = new CustomerAddress();
        address.setId(item.getId());
        address.setCorpid(corpid);
        address.setCustomerId(customerId);
        address.setAddressType(item.getAddressType());
        address.setReceiverName(item.getReceiverName());
        address.setReceiverMobile(item.getReceiverMobile());
        address.setProvinceCode(item.getProvinceCode());
        address.setCityCode(item.getCityCode());
        address.setDistrictCode(item.getDistrictCode());
        address.setDetailAddress(item.getDetailAddress());
        address.setPostalCode(item.getPostalCode());
        address.setDefaultFlag(item.getDefaultFlag());
        address.setBizStatus(item.getBizStatus());
        address.setVersion(item.getVersion());
        return address;
    }

    public static CustomerBankAccount toCustomerBankAccount(String corpid, Long customerId, CustomerBankAccountItemDTO item) {
        CustomerBankAccount bankAccount = new CustomerBankAccount();
        bankAccount.setId(item.getId());
        bankAccount.setCorpid(corpid);
        bankAccount.setCustomerId(customerId);
        bankAccount.setAccountName(item.getAccountName());
        bankAccount.setBankName(item.getBankName());
        bankAccount.setAccountNo(item.getAccountNo());
        bankAccount.setAccountUsage(item.getAccountUsage());
        bankAccount.setDefaultFlag(item.getDefaultFlag());
        bankAccount.setBizStatus(item.getBizStatus());
        bankAccount.setRemark(item.getRemark());
        bankAccount.setVersion(item.getVersion());
        return bankAccount;
    }

    public static CustomerInvoiceProfile toCustomerInvoiceProfile(String corpid, Long customerId, CustomerInvoiceProfileItemDTO item) {
        CustomerInvoiceProfile profile = new CustomerInvoiceProfile();
        profile.setId(item.getId());
        profile.setCorpid(corpid);
        profile.setCustomerId(customerId);
        profile.setInvoiceTitle(item.getInvoiceTitle());
        profile.setTaxNo(item.getTaxNo());
        profile.setAddressPhone(item.getAddressPhone());
        profile.setBankName(item.getBankName());
        profile.setBankAccountNo(item.getBankAccountNo());
        profile.setDefaultFlag(item.getDefaultFlag());
        profile.setBizStatus(item.getBizStatus());
        profile.setRemark(item.getRemark());
        profile.setVersion(item.getVersion());
        return profile;
    }

    public static CustomerListItemVO toListItemVO(
        Customer customer,
        CustomerContact defaultContact,
        CustomerAddress defaultAddress,
        CustomerInvoiceProfile defaultInvoiceProfile
    ) {
        CustomerListItemVO vo = new CustomerListItemVO();
        vo.setId(customer.getId());
        vo.setCustomerCode(customer.getCustomerCode());
        vo.setCustomerName(customer.getCustomerName());
        vo.setCustomerShortName(customer.getCustomerShortName());
        vo.setCustomerCategory(customer.getCustomerCategory());
        vo.setRegionCode(customer.getRegionCode());
        vo.setOwnerSalesId(customer.getOwnerSalesId());
        vo.setDefaultContactName(defaultContact == null ? null : defaultContact.getContactName());
        vo.setDefaultContactMobile(defaultContact == null ? null : defaultContact.getMobile());
        vo.setDefaultAddressSummary(defaultAddress == null ? null : defaultAddress.getDetailAddress());
        vo.setDefaultInvoiceTitle(defaultInvoiceProfile == null ? null : defaultInvoiceProfile.getInvoiceTitle());
        vo.setBizStatus(customer.getBizStatus());
        vo.setRefStatus(customer.getRefStatus());
        vo.setAddTime(customer.getAddTime());
        vo.setUpdateTime(customer.getUpdateTime());
        return vo;
    }

    public static CustomerDetailVO toDetailVO(CustomerSaveItemVO saveItemVO) {
        CustomerDetailVO detailVO = new CustomerDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }

    public static CustomerSaveItemVO toSaveItemVO(
        Customer customer,
        List<CustomerContact> contacts,
        List<CustomerAddress> addresses,
        List<CustomerBankAccount> bankAccounts,
        List<CustomerInvoiceProfile> invoiceProfiles
    ) {
        CustomerSaveItemVO vo = new CustomerSaveItemVO();
        if (customer != null) {
            CustomerMainDTO main = new CustomerMainDTO();
            main.setId(customer.getId());
            main.setCustomerCode(customer.getCustomerCode());
            main.setCustomerName(customer.getCustomerName());
            main.setCustomerShortName(customer.getCustomerShortName());
            main.setCustomerCategory(customer.getCustomerCategory());
            main.setRegionCode(customer.getRegionCode());
            main.setOwnerSalesId(customer.getOwnerSalesId());
            main.setOwnerSalesNameSnapshot(customer.getOwnerSalesNameSnapshot());
            main.setBizStatus(customer.getBizStatus());
            main.setRefStatus(customer.getRefStatus());
            main.setDefaultContactId(customer.getDefaultContactId());
            main.setDefaultAddressId(customer.getDefaultAddressId());
            main.setDefaultBankAccountId(customer.getDefaultBankAccountId());
            main.setDefaultInvoiceProfileId(customer.getDefaultInvoiceProfileId());
            main.setRemark(customer.getRemark());
            main.setVersion(customer.getVersion());
            vo.setMain(main);
        }
        if (contacts != null) {
            vo.setContacts(contacts.stream().map(contact -> {
                CustomerContactItemDTO item = new CustomerContactItemDTO();
                item.setId(contact.getId());
                item.setContactName(contact.getContactName());
                item.setMobile(contact.getMobile());
                item.setPhone(contact.getPhone());
                item.setEmail(contact.getEmail());
                item.setPositionName(contact.getPositionName());
                item.setDefaultFlag(contact.getDefaultFlag());
                item.setBizStatus(contact.getBizStatus());
                item.setRemark(contact.getRemark());
                item.setVersion(contact.getVersion());
                return item;
            }).toList());
        }
        if (addresses != null) {
            vo.setAddresses(addresses.stream().map(address -> {
                CustomerAddressItemDTO item = new CustomerAddressItemDTO();
                item.setId(address.getId());
                item.setAddressType(address.getAddressType());
                item.setReceiverName(address.getReceiverName());
                item.setReceiverMobile(address.getReceiverMobile());
                item.setProvinceCode(address.getProvinceCode());
                item.setCityCode(address.getCityCode());
                item.setDistrictCode(address.getDistrictCode());
                item.setDetailAddress(address.getDetailAddress());
                item.setPostalCode(address.getPostalCode());
                item.setDefaultFlag(address.getDefaultFlag());
                item.setBizStatus(address.getBizStatus());
                item.setVersion(address.getVersion());
                return item;
            }).toList());
        }
        if (bankAccounts != null) {
            vo.setBankAccounts(bankAccounts.stream().map(bankAccount -> {
                CustomerBankAccountItemDTO item = new CustomerBankAccountItemDTO();
                item.setId(bankAccount.getId());
                item.setAccountName(bankAccount.getAccountName());
                item.setBankName(bankAccount.getBankName());
                item.setAccountNo(bankAccount.getAccountNo());
                item.setAccountUsage(bankAccount.getAccountUsage());
                item.setDefaultFlag(bankAccount.getDefaultFlag());
                item.setBizStatus(bankAccount.getBizStatus());
                item.setRemark(bankAccount.getRemark());
                item.setVersion(bankAccount.getVersion());
                return item;
            }).toList());
        }
        if (invoiceProfiles != null) {
            vo.setInvoiceProfiles(invoiceProfiles.stream().map(profile -> {
                CustomerInvoiceProfileItemDTO item = new CustomerInvoiceProfileItemDTO();
                item.setId(profile.getId());
                item.setInvoiceTitle(profile.getInvoiceTitle());
                item.setTaxNo(profile.getTaxNo());
                item.setAddressPhone(profile.getAddressPhone());
                item.setBankName(profile.getBankName());
                item.setBankAccountNo(profile.getBankAccountNo());
                item.setDefaultFlag(profile.getDefaultFlag());
                item.setBizStatus(profile.getBizStatus());
                item.setRemark(profile.getRemark());
                item.setVersion(profile.getVersion());
                return item;
            }).toList());
        }
        vo.setSectionState(buildSectionState(vo));
        return vo;
    }

    private static CustomerSectionStateVO buildSectionState(CustomerSaveItemVO vo) {
        CustomerSectionStateVO sectionState = defaultSectionState();
        sectionState.setContacts(vo.getContacts().isEmpty() ? 0 : 1);
        sectionState.setAddresses(vo.getAddresses().isEmpty() ? 0 : 1);
        sectionState.setBankAccounts(vo.getBankAccounts().isEmpty() ? 0 : 1);
        sectionState.setInvoiceProfiles(vo.getInvoiceProfiles().isEmpty() ? 0 : 1);
        return sectionState;
    }

    private static CustomerSectionStateVO defaultSectionState() {
        CustomerSectionStateVO sectionState = new CustomerSectionStateVO();
        sectionState.setContacts(0);
        sectionState.setAddresses(0);
        sectionState.setBankAccounts(0);
        sectionState.setInvoiceProfiles(0);
        return sectionState;
    }

    private static CustomerSaveExtPojo toSaveExtPojo(CustomerSaveExtDTO dto) {
        CustomerSaveExtPojo pojo = new CustomerSaveExtPojo();
        if (dto == null) {
            return pojo;
        }
        pojo.setContacts(dto.getContacts());
        pojo.setAddresses(dto.getAddresses());
        pojo.setBankAccounts(dto.getBankAccounts());
        pojo.setInvoiceProfiles(dto.getInvoiceProfiles());
        return pojo;
    }

    private static CustomerSectionStatePojo toSectionStatePojo(CustomerSectionStateDTO dto) {
        CustomerSectionStatePojo pojo = new CustomerSectionStatePojo();
        if (dto == null) {
            return pojo;
        }
        pojo.setContacts(dto.getContacts());
        pojo.setAddresses(dto.getAddresses());
        pojo.setBankAccounts(dto.getBankAccounts());
        pojo.setInvoiceProfiles(dto.getInvoiceProfiles());
        return pojo;
    }

    private static CustomerDraftMetaPojo toDraftMetaPojo(CustomerDraftMetaDTO dto) {
        CustomerDraftMetaPojo pojo = new CustomerDraftMetaPojo();
        if (dto == null) {
            return pojo;
        }
        pojo.setDraftCode(dto.getDraftCode());
        pojo.setDraftTitle(dto.getDraftTitle());
        pojo.setUpdatedTime(dto.getUpdatedTime());
        return pojo;
    }

    private static CustomerSaveExtVO toSaveExtVO(CustomerSaveExtPojo pojo) {
        CustomerSaveExtVO vo = new CustomerSaveExtVO();
        if (pojo == null) {
            return vo;
        }
        vo.setContacts(pojo.getContacts());
        vo.setAddresses(pojo.getAddresses());
        vo.setBankAccounts(pojo.getBankAccounts());
        vo.setInvoiceProfiles(pojo.getInvoiceProfiles());
        return vo;
    }

    private static CustomerSectionStateVO toSectionStateVO(CustomerSectionStatePojo pojo) {
        CustomerSectionStateVO vo = new CustomerSectionStateVO();
        if (pojo == null) {
            return vo;
        }
        vo.setContacts(pojo.getContacts());
        vo.setAddresses(pojo.getAddresses());
        vo.setBankAccounts(pojo.getBankAccounts());
        vo.setInvoiceProfiles(pojo.getInvoiceProfiles());
        return vo;
    }

    private static CustomerDraftMetaVO toDraftMetaVO(CustomerSaveDraftPojo pojo) {
        CustomerDraftMetaVO vo = new CustomerDraftMetaVO();
        if (pojo == null) {
            return vo;
        }
        vo.setDraftCode(pojo.getDraftCode());
        vo.setDraftTitle(pojo.getDraftTitle());
        vo.setUpdatedTime(pojo.getUpdatedTime());
        return vo;
    }
}
