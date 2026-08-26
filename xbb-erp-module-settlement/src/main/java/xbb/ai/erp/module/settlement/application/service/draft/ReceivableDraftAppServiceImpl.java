package xbb.ai.erp.module.settlement.application.service.draft;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableDraftListDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableDraftLoadDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableDraftSaveDTO;
import xbb.ai.erp.module.settlement.admin.vo.ReceivableDraftDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceivableDraftListItemVO;
import xbb.ai.erp.module.settlement.application.pojo.ReceivableSaveDraftPojo;
import xbb.ai.erp.module.settlement.application.port.ReceivableDraftRepository;
import xbb.ai.erp.module.settlement.application.validator.ReceivableSaveCommonValidator;
import xbb.ai.erp.module.settlement.application.validator.ReceivableSaveProtocolValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceivableDraftAppServiceImpl implements ReceivableDraftAppService {

    private final ReceivableDraftRepository repository;
    private final ReceivableSaveProtocolValidator protocolValidator;
    private final ReceivableSaveCommonValidator commonValidator;

    @Override
    public DraftSaveVO saveDraft(ReceivableDraftSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForDraft(dto);
        ReceivableSaveDraftPojo draft = new ReceivableSaveDraftPojo();
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
    public List<ReceivableDraftListItemVO> draftList(ReceivableDraftListDTO dto) {
        return repository.listDrafts(dto.getCorpid(), 10).stream().map(draft -> {
            ReceivableDraftListItemVO vo = new ReceivableDraftListItemVO();
            vo.setDraftCode(draft.getDraftCode());
            vo.setDraftTitle(draft.getDraftTitle());
            return vo;
        }).toList();
    }

    @Override
    public ReceivableDraftDetailVO loadDraft(ReceivableDraftLoadDTO dto) {
        ReceivableSaveDraftPojo draft = repository.loadDraft(dto.getCorpid(), dto.getDraftCode());
        ReceivableDraftDetailVO vo = new ReceivableDraftDetailVO();
        if (draft != null) { vo.setDraftCode(draft.getDraftCode()); vo.setMain(draft.getMain()); }
        return vo;
    }
}
