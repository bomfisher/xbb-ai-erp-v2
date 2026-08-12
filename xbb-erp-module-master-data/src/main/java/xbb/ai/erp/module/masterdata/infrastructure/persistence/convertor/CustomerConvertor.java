package xbb.ai.erp.module.masterdata.infrastructure.persistence.convertor;

import xbb.ai.erp.module.masterdata.domain.model.Customer;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.po.CustomerPO;

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
        po.setDefaultContactId(customer.getDefaultContactId());
        po.setAddress(customer.getAddress());
        po.setEnabled(customer.getEnabled());
        po.setRemark(customer.getRemark());
        po.setCreatorId(customer.getCreatorId());
        po.setModifyId(customer.getModifyId());
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
        customer.setDefaultContactId(po.getDefaultContactId());
        customer.setAddress(po.getAddress());
        customer.setEnabled(po.getEnabled());
        customer.setRemark(po.getRemark());
        customer.setCreatorId(po.getCreatorId());
        customer.setModifyId(po.getModifyId());
        return customer;
    }
}
