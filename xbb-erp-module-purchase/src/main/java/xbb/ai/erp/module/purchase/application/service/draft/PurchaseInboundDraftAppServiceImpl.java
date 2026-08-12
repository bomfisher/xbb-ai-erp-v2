package xbb.ai.erp.module.purchase.application.service.draft;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundDraftListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundDraftLoadDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundDraftSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundDraftDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundDraftListItemVO;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseInboundSaveDraftPojo;
import xbb.ai.erp.module.purchase.application.port.PurchaseInboundDraftRepository;
import xbb.ai.erp.module.purchase.application.validator.PurchaseInboundSaveCommonValidator;
import xbb.ai.erp.module.purchase.application.validator.PurchaseInboundSaveProtocolValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseInboundDraftAppServiceImpl implements PurchaseInboundDraftAppService {

    private final PurchaseInboundDraftRepository repository;
    private final PurchaseInboundSaveProtocolValidator protocolValidator;
    private final PurchaseInboundSaveCommonValidator commonValidator;

    @Override
    public DraftSaveVO saveDraft(PurchaseInboundDraftSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForDraft(dto);
        PurchaseInboundSaveDraftPojo draft = new PurchaseInboundSaveDraftPojo();
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
    public List<PurchaseInboundDraftListItemVO> draftList(PurchaseInboundDraftListDTO dto) {
        return repository.listDrafts(dto.getCorpid(), 10).stream().map(draft -> {
            PurchaseInboundDraftListItemVO vo = new PurchaseInboundDraftListItemVO();
            vo.setDraftCode(draft.getDraftCode());
            vo.setDraftTitle(draft.getDraftTitle());
            return vo;
        }).toList();
    }

    @Override
    public PurchaseInboundDraftDetailVO loadDraft(PurchaseInboundDraftLoadDTO dto) {
        PurchaseInboundSaveDraftPojo draft = repository.loadDraft(dto.getCorpid(), dto.getDraftCode());
        PurchaseInboundDraftDetailVO vo = new PurchaseInboundDraftDetailVO();
        if (draft != null) { vo.setDraftCode(draft.getDraftCode()); vo.setMain(draft.getMain()); }
        return vo;
    }
}
