package xbb.ai.erp.module.customer.infrastructure.persistence.convertor;

import xbb.ai.erp.module.customer.domain.model.CustomerBankAccount;
import xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerBankAccountPO;

public final class CustomerBankAccountConvertor {

    private CustomerBankAccountConvertor() {
    }

    public static CustomerBankAccountPO toPO(CustomerBankAccount bankAccount) {
        if (bankAccount == null) {
            return null;
        }
        CustomerBankAccountPO po = new CustomerBankAccountPO();
        po.setId(bankAccount.getId());
        po.setCorpid(bankAccount.getCorpid());
        po.setCustomerId(bankAccount.getCustomerId());
        po.setAccountName(bankAccount.getAccountName());
        po.setBankName(bankAccount.getBankName());
        po.setAccountNo(bankAccount.getAccountNo());
        po.setAccountUsage(bankAccount.getAccountUsage());
        po.setDefaultFlag(bankAccount.getDefaultFlag());
        po.setBizStatus(bankAccount.getBizStatus());
        po.setRemark(bankAccount.getRemark());
        po.setCreatorId(bankAccount.getCreatorId());
        po.setModifyId(bankAccount.getModifyId());
        po.setVersion(bankAccount.getVersion());
        po.setDel(bankAccount.getDel());
        po.setAddTime(bankAccount.getAddTime());
        po.setUpdateTime(bankAccount.getUpdateTime());
        return po;
    }

    public static CustomerBankAccount toDomain(CustomerBankAccountPO po) {
        if (po == null) {
            return null;
        }
        CustomerBankAccount bankAccount = new CustomerBankAccount();
        bankAccount.setId(po.getId());
        bankAccount.setCorpid(po.getCorpid());
        bankAccount.setCustomerId(po.getCustomerId());
        bankAccount.setAccountName(po.getAccountName());
        bankAccount.setBankName(po.getBankName());
        bankAccount.setAccountNo(po.getAccountNo());
        bankAccount.setAccountUsage(po.getAccountUsage());
        bankAccount.setDefaultFlag(po.getDefaultFlag());
        bankAccount.setBizStatus(po.getBizStatus());
        bankAccount.setRemark(po.getRemark());
        bankAccount.setCreatorId(po.getCreatorId());
        bankAccount.setModifyId(po.getModifyId());
        bankAccount.setVersion(po.getVersion());
        bankAccount.setDel(po.getDel());
        bankAccount.setAddTime(po.getAddTime());
        bankAccount.setUpdateTime(po.getUpdateTime());
        return bankAccount;
    }
}
