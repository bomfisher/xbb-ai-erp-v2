package xbb.ai.erp.module.masterdata.application.service.draft;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountDraftListDTO;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountDraftLoadDTO;
import xbb.ai.erp.module.masterdata.admin.dto.FundAccountDraftSaveDTO;
import xbb.ai.erp.module.masterdata.admin.vo.FundAccountDraftDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.FundAccountDraftListItemVO;
import xbb.ai.erp.module.masterdata.application.pojo.FundAccountSaveDraftPojo;
import xbb.ai.erp.module.masterdata.application.port.FundAccountDraftRepository;
import xbb.ai.erp.module.masterdata.application.validator.FundAccountSaveCommonValidator;
import xbb.ai.erp.module.masterdata.application.validator.FundAccountSaveProtocolValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FundAccountDraftAppServiceImpl implements FundAccountDraftAppService {

    private final FundAccountDraftRepository repository;
    private final FundAccountSaveProtocolValidator protocolValidator;
    private final FundAccountSaveCommonValidator commonValidator;

    @Override
    public DraftSaveVO saveDraft(FundAccountDraftSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForDraft(dto);
        FundAccountSaveDraftPojo draft = new FundAccountSaveDraftPojo();
        draft.setCorpid(dto.getCorpid());
        draft.setMain(dto.getMain());
        draft.setDraftCode(dto.getDraftMeta().getDraftCode());
        draft.setDraftTitle(dto.getDraftMeta().getDraftTitle());
        String code = repository.saveDraft(draft);
        dto.getDraftMeta().setDraftCode(code);
        DraftSaveVO vo = new DraftSaveVO();
        vo.setDraftCode(code);
        return vo;
    }

    @Override
    public List<FundAccountDraftListItemVO> draftList(FundAccountDraftListDTO dto) {
        return repository.listDrafts(dto.getCorpid(), 10).stream().map(draft -> {
            FundAccountDraftListItemVO vo = new FundAccountDraftListItemVO();
            vo.setDraftCode(draft.getDraftCode());
            vo.setDraftTitle(draft.getDraftTitle());
            return vo;
        }).toList();
    }

    @Override
    public FundAccountDraftDetailVO loadDraft(FundAccountDraftLoadDTO dto) {
        FundAccountSaveDraftPojo draft = repository.loadDraft(dto.getCorpid(), dto.getDraftCode());
        FundAccountDraftDetailVO vo = new FundAccountDraftDetailVO();
        if (draft != null) { vo.setDraftCode(draft.getDraftCode()); vo.setMain(draft.getMain()); }
        return vo;
    }
}
