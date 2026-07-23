package xbb.ai.erp.module.customer.infrastructure.persistence.convertor;

import xbb.ai.erp.module.customer.domain.model.CustomerContact;
import xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerContactPO;

public final class CustomerContactConvertor {

    private CustomerContactConvertor() {
    }

    public static CustomerContactPO toPO(CustomerContact customerContact) {
        if (customerContact == null) {
            return null;
        }
        CustomerContactPO po = new CustomerContactPO();
        po.setId(customerContact.getId());
        po.setCorpid(customerContact.getCorpid());
        po.setCustomerId(customerContact.getCustomerId());
        po.setContactName(customerContact.getContactName());
        po.setMobile(customerContact.getMobile());
        po.setPhone(customerContact.getPhone());
        po.setEmail(customerContact.getEmail());
        po.setPositionName(customerContact.getPositionName());
        po.setDefaultFlag(customerContact.getDefaultFlag());
        po.setBizStatus(customerContact.getBizStatus());
        po.setRemark(customerContact.getRemark());
        po.setCreatorId(customerContact.getCreatorId());
        po.setModifyId(customerContact.getModifyId());
        po.setVersion(customerContact.getVersion());
        po.setDel(customerContact.getDel());
        po.setAddTime(customerContact.getAddTime());
        po.setUpdateTime(customerContact.getUpdateTime());
        return po;
    }

    public static CustomerContact toDomain(CustomerContactPO po) {
        if (po == null) {
            return null;
        }
        CustomerContact customerContact = new CustomerContact();
        customerContact.setId(po.getId());
        customerContact.setCorpid(po.getCorpid());
        customerContact.setCustomerId(po.getCustomerId());
        customerContact.setContactName(po.getContactName());
        customerContact.setMobile(po.getMobile());
        customerContact.setPhone(po.getPhone());
        customerContact.setEmail(po.getEmail());
        customerContact.setPositionName(po.getPositionName());
        customerContact.setDefaultFlag(po.getDefaultFlag());
        customerContact.setBizStatus(po.getBizStatus());
        customerContact.setRemark(po.getRemark());
        customerContact.setCreatorId(po.getCreatorId());
        customerContact.setModifyId(po.getModifyId());
        customerContact.setVersion(po.getVersion());
        customerContact.setDel(po.getDel());
        customerContact.setAddTime(po.getAddTime());
        customerContact.setUpdateTime(po.getUpdateTime());
        return customerContact;
    }
}
