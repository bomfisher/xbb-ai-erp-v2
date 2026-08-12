package xbb.ai.erp.module.masterdata.infrastructure.persistence.convertor;

import xbb.ai.erp.module.masterdata.domain.model.CustomerContact;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.po.CustomerContactPO;

public final class CustomerContactConvertor {
    private CustomerContactConvertor() {}

    public static CustomerContactPO toPO(CustomerContact contact) {
        CustomerContactPO po = new CustomerContactPO();
        po.setId(contact.getId());
        po.setCorpid(contact.getCorpid());
        po.setCustomerId(contact.getCustomerId());
        po.setContactName(contact.getName());
        po.setMobile(contact.getMobile());
        po.setDefaultFlag(contact.getDefaultFlag());
        po.setCreatorId(contact.getCreatorId());
        po.setModifyId(contact.getModifyId());
        return po;
    }

    public static CustomerContact toDomain(CustomerContactPO po) {
        CustomerContact contact = new CustomerContact();
        contact.setId(po.getId());
        contact.setCorpid(po.getCorpid());
        contact.setCustomerId(po.getCustomerId());
        contact.setName(po.getContactName());
        contact.setMobile(po.getMobile());
        contact.setDefaultFlag(po.getDefaultFlag());
        contact.setCreatorId(po.getCreatorId());
        contact.setModifyId(po.getModifyId());
        return contact;
    }
}
