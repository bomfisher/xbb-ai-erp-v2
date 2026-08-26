package xbb.ai.erp.module.settlement.application.service.draft;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.settlement.admin.dto.PayableDraftListDTO;
import xbb.ai.erp.module.settlement.admin.dto.PayableDraftLoadDTO;
import xbb.ai.erp.module.settlement.admin.dto.PayableDraftSaveDTO;
import xbb.ai.erp.module.settlement.admin.vo.PayableDraftDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.PayableDraftListItemVO;
import xbb.ai.erp.module.settlement.application.pojo.PayableSaveDraftPojo;
import xbb.ai.erp.module.settlement.application.port.PayableDraftRepository;
import xbb.ai.erp.module.settlement.application.validator.PayableSaveCommonValidator;
import xbb.ai.erp.module.settlement.application.validator.PayableSaveProtocolValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PayableDraftAppServiceImpl implements PayableDraftAppService {

    private final PayableDraftRepository repository;
    private final PayableSaveProtocolValidator protocolValidator;
    private final PayableSaveCommonValidator commonValidator;

    @Override
    public DraftSaveVO saveDraft(PayableDraftSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForDraft(dto);
        PayableSaveDraftPojo draft = new PayableSaveDraftPojo();
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
    public List<PayableDraftListItemVO> draftList(PayableDraftListDTO dto) {
        return repository.listDrafts(dto.getCorpid(), 10).stream().map(draft -> {
            PayableDraftListItemVO vo = new PayableDraftListItemVO();
            vo.setDraftCode(draft.getDraftCode());
            vo.setDraftTitle(draft.getDraftTitle());
            return vo;
        }).toList();
    }

    @Override
    public PayableDraftDetailVO loadDraft(PayableDraftLoadDTO dto) {
        PayableSaveDraftPojo draft = repository.loadDraft(dto.getCorpid(), dto.getDraftCode());
        PayableDraftDetailVO vo = new PayableDraftDetailVO();
        if (draft != null) { vo.setDraftCode(draft.getDraftCode()); vo.setMain(draft.getMain()); }
        return vo;
    }
}
