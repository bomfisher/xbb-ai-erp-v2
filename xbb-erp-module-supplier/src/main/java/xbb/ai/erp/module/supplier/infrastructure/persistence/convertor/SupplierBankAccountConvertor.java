package xbb.ai.erp.module.supplier.infrastructure.persistence.convertor;

import xbb.ai.erp.module.supplier.domain.model.SupplierBankAccount;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.SupplierBankAccountPO;

public final class SupplierBankAccountConvertor {

    private SupplierBankAccountConvertor() {
    }

    public static SupplierBankAccountPO toPO(SupplierBankAccount supplierBankAccount) {
        if (supplierBankAccount == null) {
            return null;
        }
        SupplierBankAccountPO po = new SupplierBankAccountPO();
        po.setId(supplierBankAccount.getId());
        po.setCorpid(supplierBankAccount.getCorpid());
        po.setSupplierId(supplierBankAccount.getSupplierId());
        po.setAccountName(supplierBankAccount.getAccountName());
        po.setBankName(supplierBankAccount.getBankName());
        po.setAccountNo(supplierBankAccount.getAccountNo());
        po.setAccountUsage(supplierBankAccount.getAccountUsage());
        po.setDefaultFlag(supplierBankAccount.getDefaultFlag());
        po.setBizStatus(supplierBankAccount.getBizStatus());
        po.setRemark(supplierBankAccount.getRemark());
        po.setCreatorId(supplierBankAccount.getCreatorId());
        po.setModifyId(supplierBankAccount.getModifyId());
        po.setDel(supplierBankAccount.getDel());
        po.setAddTime(supplierBankAccount.getAddTime());
        po.setUpdateTime(supplierBankAccount.getUpdateTime());
        po.setVersion(supplierBankAccount.getVersion());
        return po;
    }

    public static SupplierBankAccount toDomain(SupplierBankAccountPO po) {
        if (po == null) {
            return null;
        }
        SupplierBankAccount supplierBankAccount = new SupplierBankAccount();
        supplierBankAccount.setId(po.getId());
        supplierBankAccount.setCorpid(po.getCorpid());
        supplierBankAccount.setSupplierId(po.getSupplierId());
        supplierBankAccount.setAccountName(po.getAccountName());
        supplierBankAccount.setBankName(po.getBankName());
        supplierBankAccount.setAccountNo(po.getAccountNo());
        supplierBankAccount.setAccountUsage(po.getAccountUsage());
        supplierBankAccount.setDefaultFlag(po.getDefaultFlag());
        supplierBankAccount.setBizStatus(po.getBizStatus());
        supplierBankAccount.setRemark(po.getRemark());
        supplierBankAccount.setCreatorId(po.getCreatorId());
        supplierBankAccount.setModifyId(po.getModifyId());
        supplierBankAccount.setDel(po.getDel());
        supplierBankAccount.setAddTime(po.getAddTime());
        supplierBankAccount.setUpdateTime(po.getUpdateTime());
        supplierBankAccount.setVersion(po.getVersion());
        return supplierBankAccount;
    }
}
