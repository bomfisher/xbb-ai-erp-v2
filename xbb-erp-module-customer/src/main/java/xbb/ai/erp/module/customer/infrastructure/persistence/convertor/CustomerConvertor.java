package xbb.ai.erp.module.customer.infrastructure.persistence.convertor;

import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerPO;

public final class CustomerConvertor {

    private CustomerConvertor() {
    }

    public static CustomerPO toPO(Customer customer) {
        if (customer == null) {
            return null;
        }
        CustomerPO po = new CustomerPO();
        po.setId(customer.getId());
        po.setCorpid(customer.getCorpid());
        po.setCustomerCode(customer.getCustomerCode());
        po.setCustomerName(customer.getCustomerName());
        po.setCustomerShortName(customer.getCustomerShortName());
        po.setCustomerCategory(customer.getCustomerCategory());
        po.setRegionCode(customer.getRegionCode());
        po.setOwnerSalesId(customer.getOwnerSalesId());
        po.setOwnerSalesNameSnapshot(customer.getOwnerSalesNameSnapshot());
        po.setBizStatus(customer.getBizStatus());
        po.setRefStatus(customer.getRefStatus());
        po.setDefaultContactId(customer.getDefaultContactId());
        po.setDefaultAddressId(customer.getDefaultAddressId());
        po.setDefaultBankAccountId(customer.getDefaultBankAccountId());
        po.setDefaultInvoiceProfileId(customer.getDefaultInvoiceProfileId());
        po.setRemark(customer.getRemark());
        po.setCreatorId(customer.getCreatorId());
        po.setModifyId(customer.getModifyId());
        po.setVersion(customer.getVersion());
        po.setDel(customer.getDel());
        po.setAddTime(customer.getAddTime());
        po.setUpdateTime(customer.getUpdateTime());
        return po;
    }

    public static Customer toDomain(CustomerPO po) {
        if (po == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(po.getId());
        customer.setCorpid(po.getCorpid());
        customer.setCustomerCode(po.getCustomerCode());
        customer.setCustomerName(po.getCustomerName());
        customer.setCustomerShortName(po.getCustomerShortName());
        customer.setCustomerCategory(po.getCustomerCategory());
        customer.setRegionCode(po.getRegionCode());
        customer.setOwnerSalesId(po.getOwnerSalesId());
        customer.setOwnerSalesNameSnapshot(po.getOwnerSalesNameSnapshot());
        customer.setBizStatus(po.getBizStatus());
        customer.setRefStatus(po.getRefStatus());
        customer.setDefaultContactId(po.getDefaultContactId());
        customer.setDefaultAddressId(po.getDefaultAddressId());
        customer.setDefaultBankAccountId(po.getDefaultBankAccountId());
        customer.setDefaultInvoiceProfileId(po.getDefaultInvoiceProfileId());
        customer.setRemark(po.getRemark());
        customer.setCreatorId(po.getCreatorId());
        customer.setModifyId(po.getModifyId());
        customer.setVersion(po.getVersion());
        customer.setDel(po.getDel());
        customer.setAddTime(po.getAddTime());
        customer.setUpdateTime(po.getUpdateTime());
        return customer;
    }
}
