package xbb.ai.erp.module.supplier.infrastructure.persistence.convertor;

import xbb.ai.erp.module.supplier.domain.model.Supplier;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.SupplierPO;

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
        po.setSupplierShortName(supplier.getSupplierShortName());
        po.setSupplierCategory(supplier.getSupplierCategory());
        po.setMainBusinessCategory(supplier.getMainBusinessCategory());
        po.setOwnerPurchaserId(supplier.getOwnerPurchaserId());
        po.setOwnerPurchaserNameSnapshot(supplier.getOwnerPurchaserNameSnapshot());
        po.setBizStatus(supplier.getBizStatus());
        po.setRefStatus(supplier.getRefStatus());
        po.setDefaultContactId(supplier.getDefaultContactId());
        po.setDefaultAddressId(supplier.getDefaultAddressId());
        po.setDefaultBankAccountId(supplier.getDefaultBankAccountId());
        po.setDefaultInvoiceProfileId(supplier.getDefaultInvoiceProfileId());
        po.setRemark(supplier.getRemark());
        po.setCreatorId(supplier.getCreatorId());
        po.setModifyId(supplier.getModifyId());
        po.setVersion(supplier.getVersion());
        po.setDel(supplier.getDel());
        po.setAddTime(supplier.getAddTime());
        po.setUpdateTime(supplier.getUpdateTime());
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
        supplier.setSupplierShortName(po.getSupplierShortName());
        supplier.setSupplierCategory(po.getSupplierCategory());
        supplier.setMainBusinessCategory(po.getMainBusinessCategory());
        supplier.setOwnerPurchaserId(po.getOwnerPurchaserId());
        supplier.setOwnerPurchaserNameSnapshot(po.getOwnerPurchaserNameSnapshot());
        supplier.setBizStatus(po.getBizStatus());
        supplier.setRefStatus(po.getRefStatus());
        supplier.setDefaultContactId(po.getDefaultContactId());
        supplier.setDefaultAddressId(po.getDefaultAddressId());
        supplier.setDefaultBankAccountId(po.getDefaultBankAccountId());
        supplier.setDefaultInvoiceProfileId(po.getDefaultInvoiceProfileId());
        supplier.setRemark(po.getRemark());
        supplier.setCreatorId(po.getCreatorId());
        supplier.setModifyId(po.getModifyId());
        supplier.setVersion(po.getVersion());
        supplier.setDel(po.getDel());
        supplier.setAddTime(po.getAddTime());
        supplier.setUpdateTime(po.getUpdateTime());
        return supplier;
    }
}
