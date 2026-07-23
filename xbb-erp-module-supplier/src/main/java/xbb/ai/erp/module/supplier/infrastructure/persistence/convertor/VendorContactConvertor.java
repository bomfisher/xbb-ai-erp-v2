package xbb.ai.erp.module.supplier.infrastructure.persistence.convertor;

import xbb.ai.erp.module.supplier.domain.model.VendorContact;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.VendorContactPO;

public final class VendorContactConvertor {

    private VendorContactConvertor() {
    }

    public static VendorContactPO toPO(VendorContact vendorContact) {
        if (vendorContact == null) {
            return null;
        }
        VendorContactPO po = new VendorContactPO();
        po.setId(vendorContact.getId());
        po.setCorpid(vendorContact.getCorpid());
        po.setVendorId(vendorContact.getVendorId());
        po.setContactName(vendorContact.getContactName());
        po.setMobile(vendorContact.getMobile());
        po.setPhone(vendorContact.getPhone());
        po.setEmail(vendorContact.getEmail());
        po.setPositionName(vendorContact.getPositionName());
        po.setDefaultFlag(vendorContact.getDefaultFlag());
        po.setBizStatus(vendorContact.getBizStatus());
        po.setCreatorId(vendorContact.getCreatorId());
        po.setModifyId(vendorContact.getModifyId());
        po.setDeleted(vendorContact.getDeleted());
        po.setAddTime(vendorContact.getAddTime());
        po.setUpdateTime(vendorContact.getUpdateTime());
        po.setVersion(vendorContact.getVersion());
        return po;
    }

    public static VendorContact toDomain(VendorContactPO po) {
        if (po == null) {
            return null;
        }
        VendorContact vendorContact = new VendorContact();
        vendorContact.setId(po.getId());
        vendorContact.setCorpid(po.getCorpid());
        vendorContact.setVendorId(po.getVendorId());
        vendorContact.setContactName(po.getContactName());
        vendorContact.setMobile(po.getMobile());
        vendorContact.setPhone(po.getPhone());
        vendorContact.setEmail(po.getEmail());
        vendorContact.setPositionName(po.getPositionName());
        vendorContact.setDefaultFlag(po.getDefaultFlag());
        vendorContact.setBizStatus(po.getBizStatus());
        vendorContact.setCreatorId(po.getCreatorId());
        vendorContact.setModifyId(po.getModifyId());
        vendorContact.setDeleted(po.getDeleted());
        vendorContact.setAddTime(po.getAddTime());
        vendorContact.setUpdateTime(po.getUpdateTime());
        vendorContact.setVersion(po.getVersion());
        return vendorContact;
    }
}
