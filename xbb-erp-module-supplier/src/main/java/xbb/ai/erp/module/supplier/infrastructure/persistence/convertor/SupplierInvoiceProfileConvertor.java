package xbb.ai.erp.module.supplier.infrastructure.persistence.convertor;

import xbb.ai.erp.module.supplier.domain.model.SupplierInvoiceProfile;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.SupplierInvoiceProfilePO;

public final class SupplierInvoiceProfileConvertor {

    private SupplierInvoiceProfileConvertor() {
    }

    public static SupplierInvoiceProfilePO toPO(SupplierInvoiceProfile supplierInvoiceProfile) {
        if (supplierInvoiceProfile == null) {
            return null;
        }
        SupplierInvoiceProfilePO po = new SupplierInvoiceProfilePO();
        po.setId(supplierInvoiceProfile.getId());
        po.setCorpid(supplierInvoiceProfile.getCorpid());
        po.setSupplierId(supplierInvoiceProfile.getSupplierId());
        po.setInvoiceTitle(supplierInvoiceProfile.getInvoiceTitle());
        po.setTaxNo(supplierInvoiceProfile.getTaxNo());
        po.setAddressPhone(supplierInvoiceProfile.getAddressPhone());
        po.setBankName(supplierInvoiceProfile.getBankName());
        po.setBankAccountNo(supplierInvoiceProfile.getBankAccountNo());
        po.setDefaultFlag(supplierInvoiceProfile.getDefaultFlag());
        po.setBizStatus(supplierInvoiceProfile.getBizStatus());
        po.setRemark(supplierInvoiceProfile.getRemark());
        po.setCreatorId(supplierInvoiceProfile.getCreatorId());
        po.setModifyId(supplierInvoiceProfile.getModifyId());
        po.setDel(supplierInvoiceProfile.getDel());
        po.setAddTime(supplierInvoiceProfile.getAddTime());
        po.setUpdateTime(supplierInvoiceProfile.getUpdateTime());
        po.setVersion(supplierInvoiceProfile.getVersion());
        return po;
    }

    public static SupplierInvoiceProfile toDomain(SupplierInvoiceProfilePO po) {
        if (po == null) {
            return null;
        }
        SupplierInvoiceProfile supplierInvoiceProfile = new SupplierInvoiceProfile();
        supplierInvoiceProfile.setId(po.getId());
        supplierInvoiceProfile.setCorpid(po.getCorpid());
        supplierInvoiceProfile.setSupplierId(po.getSupplierId());
        supplierInvoiceProfile.setInvoiceTitle(po.getInvoiceTitle());
        supplierInvoiceProfile.setTaxNo(po.getTaxNo());
        supplierInvoiceProfile.setAddressPhone(po.getAddressPhone());
        supplierInvoiceProfile.setBankName(po.getBankName());
        supplierInvoiceProfile.setBankAccountNo(po.getBankAccountNo());
        supplierInvoiceProfile.setDefaultFlag(po.getDefaultFlag());
        supplierInvoiceProfile.setBizStatus(po.getBizStatus());
        supplierInvoiceProfile.setRemark(po.getRemark());
        supplierInvoiceProfile.setCreatorId(po.getCreatorId());
        supplierInvoiceProfile.setModifyId(po.getModifyId());
        supplierInvoiceProfile.setDel(po.getDel());
        supplierInvoiceProfile.setAddTime(po.getAddTime());
        supplierInvoiceProfile.setUpdateTime(po.getUpdateTime());
        supplierInvoiceProfile.setVersion(po.getVersion());
        return supplierInvoiceProfile;
    }
}
