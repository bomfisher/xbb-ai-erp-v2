package xbb.ai.erp.module.masterdata.infrastructure.persistence.convertor;

import xbb.ai.erp.module.masterdata.domain.model.Supplier;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.po.SupplierPO;

public final class SupplierConvertor {

    private SupplierConvertor() {
    }

    public static SupplierPO toPO(Supplier supplier) {
        if (supplier == null) {
            return null;
        }
        SupplierPO po = new SupplierPO();
        po.setId(supplier.getId());
        po.setCorpid(supplier.getCorpid());
        po.setSupplierCode(supplier.getSupplierCode());
        po.setSupplierName(supplier.getSupplierName());
        po.setDefaultContactId(supplier.getDefaultContactId());
        po.setMobile(supplier.getMobile());
        po.setAddress(supplier.getAddress());
        po.setEnabled(supplier.getEnabled());
        po.setRemark(supplier.getRemark());
        po.setCreatorId(supplier.getCreatorId());
        po.setModifyId(supplier.getModifyId());
        return po;
    }

    public static Supplier toDomain(SupplierPO po) {
        if (po == null) {
            return null;
        }
        Supplier supplier = new Supplier();
        supplier.setId(po.getId());
        supplier.setCorpid(po.getCorpid());
        supplier.setSupplierCode(po.getSupplierCode());
        supplier.setSupplierName(po.getSupplierName());
        supplier.setDefaultContactId(po.getDefaultContactId());
        supplier.setMobile(po.getMobile());
        supplier.setAddress(po.getAddress());
        supplier.setEnabled(po.getEnabled());
        supplier.setRemark(po.getRemark());
        supplier.setCreatorId(po.getCreatorId());
        supplier.setModifyId(po.getModifyId());
        return supplier;
    }
}
