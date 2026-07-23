package xbb.ai.erp.module.supplier.infrastructure.persistence.convertor;

import xbb.ai.erp.module.supplier.domain.model.VendorInvoiceProfile;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.VendorInvoiceProfilePO;

public final class VendorInvoiceProfileConvertor {

    private VendorInvoiceProfileConvertor() {
    }

    public static VendorInvoiceProfilePO toPO(VendorInvoiceProfile vendorInvoiceProfile) {
        if (vendorInvoiceProfile == null) {
            return null;
        }
        VendorInvoiceProfilePO po = new VendorInvoiceProfilePO();
        po.setId(vendorInvoiceProfile.getId());
        po.setCorpid(vendorInvoiceProfile.getCorpid());
        po.setVendorId(vendorInvoiceProfile.getVendorId());
        po.setInvoiceTitle(vendorInvoiceProfile.getInvoiceTitle());
        po.setTaxNo(vendorInvoiceProfile.getTaxNo());
        po.setRegisteredAddress(vendorInvoiceProfile.getRegisteredAddress());
        po.setRegisteredPhone(vendorInvoiceProfile.getRegisteredPhone());
        po.setBankName(vendorInvoiceProfile.getBankName());
        po.setBankAccountNo(vendorInvoiceProfile.getBankAccountNo());
        po.setDefaultFlag(vendorInvoiceProfile.getDefaultFlag());
        po.setBizStatus(vendorInvoiceProfile.getBizStatus());
        po.setCreatorId(vendorInvoiceProfile.getCreatorId());
        po.setModifyId(vendorInvoiceProfile.getModifyId());
        po.setDeleted(vendorInvoiceProfile.getDeleted());
        po.setAddTime(vendorInvoiceProfile.getAddTime());
        po.setUpdateTime(vendorInvoiceProfile.getUpdateTime());
        po.setVersion(vendorInvoiceProfile.getVersion());
        return po;
    }

    public static VendorInvoiceProfile toDomain(VendorInvoiceProfilePO po) {
        if (po == null) {
            return null;
        }
        VendorInvoiceProfile vendorInvoiceProfile = new VendorInvoiceProfile();
        vendorInvoiceProfile.setId(po.getId());
        vendorInvoiceProfile.setCorpid(po.getCorpid());
        vendorInvoiceProfile.setVendorId(po.getVendorId());
        vendorInvoiceProfile.setInvoiceTitle(po.getInvoiceTitle());
        vendorInvoiceProfile.setTaxNo(po.getTaxNo());
        vendorInvoiceProfile.setRegisteredAddress(po.getRegisteredAddress());
        vendorInvoiceProfile.setRegisteredPhone(po.getRegisteredPhone());
        vendorInvoiceProfile.setBankName(po.getBankName());
        vendorInvoiceProfile.setBankAccountNo(po.getBankAccountNo());
        vendorInvoiceProfile.setDefaultFlag(po.getDefaultFlag());
        vendorInvoiceProfile.setBizStatus(po.getBizStatus());
        vendorInvoiceProfile.setCreatorId(po.getCreatorId());
        vendorInvoiceProfile.setModifyId(po.getModifyId());
        vendorInvoiceProfile.setDeleted(po.getDeleted());
        vendorInvoiceProfile.setAddTime(po.getAddTime());
        vendorInvoiceProfile.setUpdateTime(po.getUpdateTime());
        vendorInvoiceProfile.setVersion(po.getVersion());
        return vendorInvoiceProfile;
    }
}
