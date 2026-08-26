package xbb.ai.erp.module.masterdata.application.service.save;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountSaveDTO;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountSubmitSaveDTO;
import xbb.ai.erp.module.masterdata.application.assembler.FundAccountAdminAssembler;
import xbb.ai.erp.module.masterdata.application.validator.FundAccountValidator;
import xbb.ai.erp.module.masterdata.application.port.FundAccountDraftRepository;
import xbb.ai.erp.module.masterdata.application.validator.FundAccountSaveProtocolValidator;
import xbb.ai.erp.module.masterdata.application.validator.FundAccountSaveCommonValidator;
import xbb.ai.erp.module.masterdata.application.validator.FundAccountSaveBusinessValidator;
import xbb.ai.erp.module.masterdata.domain.model.FundAccount;
import xbb.ai.erp.module.masterdata.domain.repository.FundAccountRepository;

@Service
@RequiredArgsConstructor
public class FundAccountSaveAppServiceImpl {

    private final FundAccountRepository fundAccountRepository;

    private final FundAccountDraftRepository draftRepository;
    private final FundAccountSaveProtocolValidator protocolValidator;
    private final FundAccountSaveCommonValidator commonValidator;
    private final FundAccountSaveBusinessValidator businessValidator;

    @Transactional
    public BaseVO saveAndSubmit(FundAccountSubmitSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForSubmit(dto);
        businessValidator.validateForSubmit(dto);
        save(dto);
        if (dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) draftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
        return new BaseVO();
    }

    public Long save(FundAccountSaveDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        FundAccountValidator.validateSave(dto);
        FundAccount entity = FundAccountAdminAssembler.toFundAccount(dto);
        if (entity.getId() == null) {
            return fundAccountRepository.insert(entity);
        }
        fundAccountRepository.update(entity);
        return entity.getId();
    }

    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() != null && !dto.getIdList().isEmpty()) {
            fundAccountRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
        }
    }
}
