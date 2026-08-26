package xbb.ai.erp.module.masterdata.infrastructure.persistence.convertor;

import xbb.ai.erp.module.masterdata.domain.model.FundAccount;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.po.FundAccountPO;

public final class FundAccountConvertor {

    private FundAccountConvertor() {
    }

    public static FundAccountPO toPO(FundAccount fundAccount) {
        if (fundAccount == null) {
            return null;
        }
        FundAccountPO po = new FundAccountPO();
        po.setId(fundAccount.getId());
        po.setCorpid(fundAccount.getCorpid());
        po.setAccountCode(fundAccount.getAccountCode());
        po.setAccountName(fundAccount.getAccountName());
        po.setCurrency(fundAccount.getCurrency());
        po.setBankAccountNo(fundAccount.getBankAccountNo());
        po.setAccountHolder(fundAccount.getAccountHolder());
        po.setBankName(fundAccount.getBankName());
        po.setAccountType(fundAccount.getAccountType());
        po.setDefaultFlag(fundAccount.getDefaultFlag());
        po.setEnabled(fundAccount.getEnabled());
        po.setRemark(fundAccount.getRemark());
        po.setCreatorId(fundAccount.getCreatorId());
        po.setModifyId(fundAccount.getModifyId());
        return po;
    }

    public static FundAccount toDomain(FundAccountPO po) {
        if (po == null) {
            return null;
        }
        FundAccount fundAccount = new FundAccount();
        fundAccount.setId(po.getId());
        fundAccount.setCorpid(po.getCorpid());
        fundAccount.setAccountCode(po.getAccountCode());
        fundAccount.setAccountName(po.getAccountName());
        fundAccount.setCurrency(po.getCurrency());
        fundAccount.setBankAccountNo(po.getBankAccountNo());
        fundAccount.setAccountHolder(po.getAccountHolder());
        fundAccount.setBankName(po.getBankName());
        fundAccount.setAccountType(po.getAccountType());
        fundAccount.setDefaultFlag(po.getDefaultFlag());
        fundAccount.setEnabled(po.getEnabled());
        fundAccount.setRemark(po.getRemark());
        fundAccount.setCreatorId(po.getCreatorId());
        fundAccount.setModifyId(po.getModifyId());
        return fundAccount;
    }
}
