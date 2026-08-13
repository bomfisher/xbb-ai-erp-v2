package xbb.ai.erp.module.purchase.application.service.draft;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderDraftListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderDraftLoadDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderDraftSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDraftDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDraftListItemVO;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseOrderSaveDraftPojo;
import xbb.ai.erp.module.purchase.application.port.PurchaseOrderDraftRepository;
import xbb.ai.erp.module.purchase.application.validator.PurchaseOrderSaveCommonValidator;
import xbb.ai.erp.module.purchase.application.validator.PurchaseOrderSaveProtocolValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseOrderDraftAppServiceImpl implements PurchaseOrderDraftAppService {

    private final PurchaseOrderDraftRepository repository;
    private final PurchaseOrderSaveProtocolValidator protocolValidator;
    private final PurchaseOrderSaveCommonValidator commonValidator;

    @Override
    public DraftSaveVO saveDraft(PurchaseOrderDraftSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForDraft(dto);
        PurchaseOrderSaveDraftPojo draft = new PurchaseOrderSaveDraftPojo();
        draft.setCorpid(dto.getCorpid());
        draft.setMain(dto.getMain());
        draft.setItems(dto.getItems());
        draft.setDraftCode(dto.getDraftMeta().getDraftCode());
        draft.setDraftTitle(dto.getDraftMeta().getDraftTitle());
        String code = repository.saveDraft(draft);
        dto.getDraftMeta().setDraftCode(code);
        DraftSaveVO vo = new DraftSaveVO();
        vo.setDraftCode(code);
        return vo;
    }

    @Override
    public List<PurchaseOrderDraftListItemVO> draftList(PurchaseOrderDraftListDTO dto) {
        return repository.listDrafts(dto.getCorpid(), 10).stream().map(draft -> {
            PurchaseOrderDraftListItemVO vo = new PurchaseOrderDraftListItemVO();
            vo.setDraftCode(draft.getDraftCode());
            vo.setDraftTitle(draft.getDraftTitle());
            return vo;
        }).toList();
    }

    @Override
    public PurchaseOrderDraftDetailVO loadDraft(PurchaseOrderDraftLoadDTO dto) {
        PurchaseOrderSaveDraftPojo draft = repository.loadDraft(dto.getCorpid(), dto.getDraftCode());
        PurchaseOrderDraftDetailVO vo = new PurchaseOrderDraftDetailVO();
        if (draft != null) { vo.setDraftCode(draft.getDraftCode()); vo.setMain(draft.getMain()); vo.setItems(draft.getItems()); }
        return vo;
    }
}
