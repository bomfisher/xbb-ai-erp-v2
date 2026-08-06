package xbb.ai.erp.module.supplier.infrastructure.persistence.convertor;

import xbb.ai.erp.module.supplier.domain.model.SupplierContact;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.SupplierContactPO;

public final class SupplierContactConvertor {

    private SupplierContactConvertor() {
    }

    public static SupplierContactPO toPO(SupplierContact supplierContact) {
        if (supplierContact == null) {
            return null;
        }
        SupplierContactPO po = new SupplierContactPO();
        po.setId(supplierContact.getId());
        po.setCorpid(supplierContact.getCorpid());
        po.setSupplierId(supplierContact.getSupplierId());
        po.setContactName(supplierContact.getContactName());
        po.setMobile(supplierContact.getMobile());
        po.setPhone(supplierContact.getPhone());
        po.setEmail(supplierContact.getEmail());
        po.setPositionName(supplierContact.getPositionName());
        po.setDefaultFlag(supplierContact.getDefaultFlag());
        po.setBizStatus(supplierContact.getBizStatus());
        po.setRemark(supplierContact.getRemark());
        po.setCreatorId(supplierContact.getCreatorId());
        po.setModifyId(supplierContact.getModifyId());
        po.setDel(supplierContact.getDel());
        po.setAddTime(supplierContact.getAddTime());
        po.setUpdateTime(supplierContact.getUpdateTime());
        po.setVersion(supplierContact.getVersion());
        return po;
    }

    public static SupplierContact toDomain(SupplierContactPO po) {
        if (po == null) {
            return null;
        }
        SupplierContact supplierContact = new SupplierContact();
        supplierContact.setId(po.getId());
        supplierContact.setCorpid(po.getCorpid());
        supplierContact.setSupplierId(po.getSupplierId());
        supplierContact.setContactName(po.getContactName());
        supplierContact.setMobile(po.getMobile());
        supplierContact.setPhone(po.getPhone());
        supplierContact.setEmail(po.getEmail());
        supplierContact.setPositionName(po.getPositionName());
        supplierContact.setDefaultFlag(po.getDefaultFlag());
        supplierContact.setBizStatus(po.getBizStatus());
        supplierContact.setRemark(po.getRemark());
        supplierContact.setCreatorId(po.getCreatorId());
        supplierContact.setModifyId(po.getModifyId());
        supplierContact.setDel(po.getDel());
        supplierContact.setAddTime(po.getAddTime());
        supplierContact.setUpdateTime(po.getUpdateTime());
        supplierContact.setVersion(po.getVersion());
        return supplierContact;
    }
}
