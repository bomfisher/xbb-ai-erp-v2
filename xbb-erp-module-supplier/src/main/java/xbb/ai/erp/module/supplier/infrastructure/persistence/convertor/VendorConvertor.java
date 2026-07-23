package xbb.ai.erp.module.supplier.infrastructure.persistence.convertor;

import xbb.ai.erp.module.supplier.domain.model.Vendor;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.VendorPO;

public final class VendorConvertor {

    private VendorConvertor() {
    }

    public static VendorPO toPO(Vendor vendor) {
        if (vendor == null) {
            return null;
        }
        VendorPO po = new VendorPO();
        po.setId(vendor.getId());
        po.setCorpid(vendor.getCorpid());
        po.setVendorCode(vendor.getVendorCode());
        po.setVendorName(vendor.getVendorName());
        po.setVendorShortName(vendor.getVendorShortName());
        po.setVendorCategory(vendor.getVendorCategory());
        po.setMainBusinessCategory(vendor.getMainBusinessCategory());
        po.setOwnerPurchaserId(vendor.getOwnerPurchaserId());
        po.setOwnerPurchaserNameSnapshot(vendor.getOwnerPurchaserNameSnapshot());
        po.setBizStatus(vendor.getBizStatus());
        po.setRefStatus(vendor.getRefStatus());
        po.setDefaultContactId(vendor.getDefaultContactId());
        po.setDefaultAddressId(vendor.getDefaultAddressId());
        po.setDefaultBankAccountId(vendor.getDefaultBankAccountId());
        po.setDefaultInvoiceProfileId(vendor.getDefaultInvoiceProfileId());
        po.setRemark(vendor.getRemark());
        po.setCreatorId(vendor.getCreatorId());
        po.setModifyId(vendor.getModifyId());
        po.setVersion(vendor.getVersion());
        po.setDeleted(vendor.getDeleted());
        po.setAddTime(vendor.getAddTime());
        po.setUpdateTime(vendor.getUpdateTime());
        return po;
    }

    public static Vendor toDomain(VendorPO po) {
        if (po == null) {
            return null;
        }
        Vendor vendor = new Vendor();
        vendor.setId(po.getId());
        vendor.setCorpid(po.getCorpid());
        vendor.setVendorCode(po.getVendorCode());
        vendor.setVendorName(po.getVendorName());
        vendor.setVendorShortName(po.getVendorShortName());
        vendor.setVendorCategory(po.getVendorCategory());
        vendor.setMainBusinessCategory(po.getMainBusinessCategory());
        vendor.setOwnerPurchaserId(po.getOwnerPurchaserId());
        vendor.setOwnerPurchaserNameSnapshot(po.getOwnerPurchaserNameSnapshot());
        vendor.setBizStatus(po.getBizStatus());
        vendor.setRefStatus(po.getRefStatus());
        vendor.setDefaultContactId(po.getDefaultContactId());
        vendor.setDefaultAddressId(po.getDefaultAddressId());
        vendor.setDefaultBankAccountId(po.getDefaultBankAccountId());
        vendor.setDefaultInvoiceProfileId(po.getDefaultInvoiceProfileId());
        vendor.setRemark(po.getRemark());
        vendor.setCreatorId(po.getCreatorId());
        vendor.setModifyId(po.getModifyId());
        vendor.setVersion(po.getVersion());
        vendor.setDeleted(po.getDeleted());
        vendor.setAddTime(po.getAddTime());
        vendor.setUpdateTime(po.getUpdateTime());
        return vendor;
    }
}
