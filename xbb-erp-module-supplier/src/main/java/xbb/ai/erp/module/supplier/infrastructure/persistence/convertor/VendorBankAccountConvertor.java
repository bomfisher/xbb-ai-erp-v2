package xbb.ai.erp.module.supplier.infrastructure.persistence.convertor;

import xbb.ai.erp.module.supplier.domain.model.VendorBankAccount;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.VendorBankAccountPO;

public final class VendorBankAccountConvertor {

    private VendorBankAccountConvertor() {
    }

    public static VendorBankAccountPO toPO(VendorBankAccount vendorBankAccount) {
        if (vendorBankAccount == null) {
            return null;
        }
        VendorBankAccountPO po = new VendorBankAccountPO();
        po.setId(vendorBankAccount.getId());
        po.setCorpid(vendorBankAccount.getCorpid());
        po.setVendorId(vendorBankAccount.getVendorId());
        po.setAccountName(vendorBankAccount.getAccountName());
        po.setBankName(vendorBankAccount.getBankName());
        po.setBankAccountNo(vendorBankAccount.getBankAccountNo());
        po.setAccountUsage(vendorBankAccount.getAccountUsage());
        po.setDefaultFlag(vendorBankAccount.getDefaultFlag());
        po.setBizStatus(vendorBankAccount.getBizStatus());
        po.setCreatorId(vendorBankAccount.getCreatorId());
        po.setModifyId(vendorBankAccount.getModifyId());
        po.setDeleted(vendorBankAccount.getDeleted());
        po.setAddTime(vendorBankAccount.getAddTime());
        po.setUpdateTime(vendorBankAccount.getUpdateTime());
        po.setVersion(vendorBankAccount.getVersion());
        return po;
    }

    public static VendorBankAccount toDomain(VendorBankAccountPO po) {
        if (po == null) {
            return null;
        }
        VendorBankAccount vendorBankAccount = new VendorBankAccount();
        vendorBankAccount.setId(po.getId());
        vendorBankAccount.setCorpid(po.getCorpid());
        vendorBankAccount.setVendorId(po.getVendorId());
        vendorBankAccount.setAccountName(po.getAccountName());
        vendorBankAccount.setBankName(po.getBankName());
        vendorBankAccount.setBankAccountNo(po.getBankAccountNo());
        vendorBankAccount.setAccountUsage(po.getAccountUsage());
        vendorBankAccount.setDefaultFlag(po.getDefaultFlag());
        vendorBankAccount.setBizStatus(po.getBizStatus());
        vendorBankAccount.setCreatorId(po.getCreatorId());
        vendorBankAccount.setModifyId(po.getModifyId());
        vendorBankAccount.setDeleted(po.getDeleted());
        vendorBankAccount.setAddTime(po.getAddTime());
        vendorBankAccount.setUpdateTime(po.getUpdateTime());
        vendorBankAccount.setVersion(po.getVersion());
        return vendorBankAccount;
    }
}
