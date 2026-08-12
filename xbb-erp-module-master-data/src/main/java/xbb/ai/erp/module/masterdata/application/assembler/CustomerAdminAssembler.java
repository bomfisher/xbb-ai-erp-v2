package xbb.ai.erp.module.masterdata.application.assembler;

import java.util.ArrayList;
import java.util.List;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerContactDTO;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerMainDTO;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerSaveDTO;
import xbb.ai.erp.module.masterdata.admin.vo.CustomerDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.CustomerListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.CustomerSaveItemVO;
import xbb.ai.erp.module.masterdata.domain.model.Customer;
import xbb.ai.erp.module.masterdata.domain.model.CustomerContact;

public final class CustomerAdminAssembler {

    private CustomerAdminAssembler() {
    }

    public static CustomerSaveItemVO buildEmptySaveItemVO() {
        CustomerSaveItemVO vo = new CustomerSaveItemVO();
        vo.setContacts(new ArrayList<>(List.of(new CustomerContactDTO())));
        return vo;
    }

    public static Customer toCustomer(CustomerSaveDTO dto) {
        Customer customer = new Customer();
        CustomerMainDTO main = dto.getMain();
        if (main != null) {
            customer.setId(main.getId());
            customer.setCorpid(main.getCorpid());
            customer.setCustomerCode(main.getCustomerCode());
            customer.setCustomerName(main.getCustomerName());
            customer.setDefaultContactId(main.getDefaultContactId());
            customer.setAddress(main.getAddress());
            customer.setEnabled(main.getEnabled());
            customer.setRemark(main.getRemark());
            customer.setCreatorId(main.getCreatorId());
            customer.setModifyId(main.getModifyId());
        }
        customer.setCorpid(dto.getCorpid());
        return customer;
    }

    public static CustomerListItemVO toListItemVO(Customer customer) {
        CustomerListItemVO vo = new CustomerListItemVO();
        vo.setId(customer.getId());
        vo.setCustomerCode(customer.getCustomerCode());
        vo.setCustomerName(customer.getCustomerName());
        vo.setAddress(customer.getAddress());
        vo.setEnabled(customer.getEnabled());
        vo.setRemark(customer.getRemark());
        vo.setCreatorId(customer.getCreatorId());
        vo.setModifyId(customer.getModifyId());
        return vo;
    }

    public static CustomerSaveItemVO toSaveItemVO(Customer customer) {
        return toSaveItemVO(customer, List.of());
    }

    public static CustomerSaveItemVO toSaveItemVO(Customer customer, List<CustomerContact> contacts) {
        CustomerSaveItemVO vo = new CustomerSaveItemVO();
        if (customer == null) {
            return vo;
        }
        CustomerMainDTO main = new CustomerMainDTO();
        main.setId(customer.getId());
        main.setCorpid(customer.getCorpid());
        main.setCustomerCode(customer.getCustomerCode());
        main.setCustomerName(customer.getCustomerName());
        main.setDefaultContactId(customer.getDefaultContactId());
        main.setAddress(customer.getAddress());
        main.setEnabled(customer.getEnabled());
        main.setRemark(customer.getRemark());
        main.setCreatorId(customer.getCreatorId());
        main.setModifyId(customer.getModifyId());
        vo.setMain(main);
        vo.setContacts(contacts.stream().map(CustomerAdminAssembler::toContactDTO).toList());
        return vo;
    }

    public static List<CustomerContact> toContacts(CustomerSaveDTO dto, Long customerId) {
        if (dto.getContacts() == null) return List.of();
        return dto.getContacts().stream().filter(CustomerAdminAssembler::hasContactValue).map(item -> {
            CustomerContact contact = new CustomerContact();
            contact.setId(item.getId());
            contact.setCorpid(dto.getCorpid());
            contact.setCustomerId(customerId);
            contact.setName(item.getName());
            contact.setMobile(item.getMobile());
            contact.setDefaultFlag(item.getDefaultFlag() == null ? 0 : item.getDefaultFlag());
            contact.setCreatorId(dto.getUserId());
            contact.setModifyId(dto.getUserId());
            return contact;
        }).toList();
    }

    private static boolean hasContactValue(CustomerContactDTO contact) {
        return (contact.getName() != null && !contact.getName().isBlank())
            || (contact.getMobile() != null && !contact.getMobile().isBlank());
    }

    private static CustomerContactDTO toContactDTO(CustomerContact contact) {
        CustomerContactDTO dto = new CustomerContactDTO();
        dto.setId(contact.getId());
        dto.setName(contact.getName());
        dto.setMobile(contact.getMobile());
        dto.setDefaultFlag(contact.getDefaultFlag());
        return dto;
    }

    public static CustomerDetailVO toDetailVO(CustomerSaveItemVO saveItemVO) {
        CustomerDetailVO detailVO = new CustomerDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }
}
