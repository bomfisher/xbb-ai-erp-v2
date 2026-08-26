package xbb.ai.erp.module.masterdata.application.assembler;

import java.util.Objects;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountMainDTO;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountSaveDTO;
import xbb.ai.erp.module.masterdata.admin.vo.FundAccountDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.FundAccountListItemVO;
import xbb.ai.erp.module.masterdata.admin.vo.FundAccountSaveItemVO;
import xbb.ai.erp.module.masterdata.domain.model.FundAccount;

public final class FundAccountAdminAssembler {

    private FundAccountAdminAssembler() {
    }

    public static FundAccountSaveItemVO buildEmptySaveItemVO() {
        return new FundAccountSaveItemVO();
    }

    public static FundAccount toFundAccount(FundAccountSaveDTO dto) {
        FundAccount fundAccount = new FundAccount();
        FundAccountMainDTO main = dto.getMain();
        if (main != null) {
            fundAccount.setId(main.getId());
            fundAccount.setCorpid(main.getCorpid());
            fundAccount.setAccountCode(main.getAccountCode());
            fundAccount.setAccountName(main.getAccountName());
            fundAccount.setCurrency(main.getCurrency());
            fundAccount.setBankAccountNo(main.getBankAccountNo());
            fundAccount.setAccountHolder(main.getAccountHolder());
            fundAccount.setBankName(main.getBankName());
            fundAccount.setAccountType(main.getAccountType());
            fundAccount.setDefaultFlag(main.getDefaultFlag());
            fundAccount.setEnabled(main.getEnabled());
            fundAccount.setRemark(main.getRemark());
            fundAccount.setCreatorId(main.getCreatorId());
            fundAccount.setModifyId(main.getModifyId());
            if (Objects.isNull(main.getId())) {
                fundAccount.setCreatorId(dto.getUserId());
            }
            fundAccount.setModifyId(dto.getUserId());
        }
        fundAccount.setCorpid(dto.getCorpid());
        return fundAccount;
    }

    public static FundAccountListItemVO toListItemVO(FundAccount fundAccount) {
        FundAccountListItemVO vo = new FundAccountListItemVO();
        vo.setId(Objects.isNull(fundAccount.getId()) ? "" : Objects.toString(fundAccount.getId()));
        vo.setAccountCode(fundAccount.getAccountCode());
        vo.setAccountName(fundAccount.getAccountName());
        vo.setCurrency(Objects.isNull(fundAccount.getCurrency()) ? "" : Objects.toString(fundAccount.getCurrency()));
        vo.setBankAccountNo(fundAccount.getBankAccountNo());
        vo.setAccountHolder(fundAccount.getAccountHolder());
        vo.setBankName(fundAccount.getBankName());
        vo.setAccountType(Objects.isNull(fundAccount.getAccountType()) ? "" : Objects.toString(fundAccount.getAccountType()));
        vo.setDefaultFlag(Objects.isNull(fundAccount.getDefaultFlag()) ? "" : Objects.toString(fundAccount.getDefaultFlag()));
        vo.setEnabled(Objects.isNull(fundAccount.getEnabled()) ? "" : Objects.toString(fundAccount.getEnabled()));
        vo.setRemark(fundAccount.getRemark());
        vo.setCreatorId(fundAccount.getCreatorId());
        vo.setModifyId(fundAccount.getModifyId());
        return vo;
    }

    public static FundAccountSaveItemVO toSaveItemVO(FundAccount fundAccount) {
        FundAccountSaveItemVO vo = new FundAccountSaveItemVO();
        if (fundAccount == null) {
            return vo;
        }
        FundAccountMainDTO main = new FundAccountMainDTO();
        main.setId(fundAccount.getId());
        main.setCorpid(fundAccount.getCorpid());
        main.setAccountCode(fundAccount.getAccountCode());
        main.setAccountName(fundAccount.getAccountName());
        main.setCurrency(fundAccount.getCurrency());
        main.setBankAccountNo(fundAccount.getBankAccountNo());
        main.setAccountHolder(fundAccount.getAccountHolder());
        main.setBankName(fundAccount.getBankName());
        main.setAccountType(fundAccount.getAccountType());
        main.setDefaultFlag(fundAccount.getDefaultFlag());
        main.setEnabled(fundAccount.getEnabled());
        main.setRemark(fundAccount.getRemark());
        main.setCreatorId(fundAccount.getCreatorId());
        main.setModifyId(fundAccount.getModifyId());
        vo.setMain(main);
        return vo;
    }

    public static FundAccountDetailVO toDetailVO(FundAccountSaveItemVO saveItemVO) {
        FundAccountDetailVO detailVO = new FundAccountDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }
}
