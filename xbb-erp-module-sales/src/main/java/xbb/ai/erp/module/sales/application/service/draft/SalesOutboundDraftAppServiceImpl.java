package xbb.ai.erp.module.sales.application.service.draft;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundDraftListDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundDraftLoadDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundDraftSaveDTO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundDraftDetailVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundDraftListItemVO;
import xbb.ai.erp.module.sales.application.pojo.SalesOutboundSaveDraftPojo;
import xbb.ai.erp.module.sales.application.port.SalesOutboundDraftRepository;
import xbb.ai.erp.module.sales.application.validator.SalesOutboundSaveCommonValidator;
import xbb.ai.erp.module.sales.application.validator.SalesOutboundSaveProtocolValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesOutboundDraftAppServiceImpl implements SalesOutboundDraftAppService {

    private final SalesOutboundDraftRepository repository;
    private final SalesOutboundSaveProtocolValidator protocolValidator;
    private final SalesOutboundSaveCommonValidator commonValidator;

    @Override
    public DraftSaveVO saveDraft(SalesOutboundDraftSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForDraft(dto);
        SalesOutboundSaveDraftPojo draft = new SalesOutboundSaveDraftPojo();
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
    public List<SalesOutboundDraftListItemVO> draftList(SalesOutboundDraftListDTO dto) {
        return repository.listDrafts(dto.getCorpid(), 10).stream().map(draft -> {
            SalesOutboundDraftListItemVO vo = new SalesOutboundDraftListItemVO();
            vo.setDraftCode(draft.getDraftCode());
            vo.setDraftTitle(draft.getDraftTitle());
            return vo;
        }).toList();
    }

    @Override
    public SalesOutboundDraftDetailVO loadDraft(SalesOutboundDraftLoadDTO dto) {
        SalesOutboundSaveDraftPojo draft = repository.loadDraft(dto.getCorpid(), dto.getDraftCode());
        SalesOutboundDraftDetailVO vo = new SalesOutboundDraftDetailVO();
        if (draft != null) { vo.setDraftCode(draft.getDraftCode()); vo.setMain(draft.getMain()); }
        return vo;
    }
}
