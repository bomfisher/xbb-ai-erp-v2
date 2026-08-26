package xbb.ai.erp.module.purchase.application.service.draft;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceDraftListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceDraftLoadDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceDraftSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceDraftDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceDraftListItemVO;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseInvoiceSaveDraftPojo;
import xbb.ai.erp.module.purchase.application.port.PurchaseInvoiceDraftRepository;
import xbb.ai.erp.module.purchase.application.validator.PurchaseInvoiceSaveCommonValidator;
import xbb.ai.erp.module.purchase.application.validator.PurchaseInvoiceSaveProtocolValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseInvoiceDraftAppServiceImpl implements PurchaseInvoiceDraftAppService {

    private final PurchaseInvoiceDraftRepository repository;
    private final PurchaseInvoiceSaveProtocolValidator protocolValidator;
    private final PurchaseInvoiceSaveCommonValidator commonValidator;

    @Override
    public DraftSaveVO saveDraft(PurchaseInvoiceDraftSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForDraft(dto);
        PurchaseInvoiceSaveDraftPojo draft = new PurchaseInvoiceSaveDraftPojo();
        draft.setCorpid(dto.getCorpid());
        draft.setMain(dto.getMain());
        draft.setSourceSelection(dto.getSourceSelection());
        draft.setDraftCode(dto.getDraftMeta().getDraftCode());
        draft.setDraftTitle(dto.getDraftMeta().getDraftTitle());
        String code = repository.saveDraft(draft);
        dto.getDraftMeta().setDraftCode(code);
        DraftSaveVO vo = new DraftSaveVO();
        vo.setDraftCode(code);
        return vo;
    }

    @Override
    public List<PurchaseInvoiceDraftListItemVO> draftList(PurchaseInvoiceDraftListDTO dto) {
        return repository.listDrafts(dto.getCorpid(), 10).stream().map(draft -> {
            PurchaseInvoiceDraftListItemVO vo = new PurchaseInvoiceDraftListItemVO();
            vo.setDraftCode(draft.getDraftCode());
            vo.setDraftTitle(draft.getDraftTitle());
            return vo;
        }).toList();
    }

    @Override
    public PurchaseInvoiceDraftDetailVO loadDraft(PurchaseInvoiceDraftLoadDTO dto) {
        PurchaseInvoiceSaveDraftPojo draft = repository.loadDraft(dto.getCorpid(), dto.getDraftCode());
        PurchaseInvoiceDraftDetailVO vo = new PurchaseInvoiceDraftDetailVO();
        if (draft != null) {
            vo.setDraftCode(draft.getDraftCode());
            vo.setMain(draft.getMain());
            vo.setSourceSelection(draft.getSourceSelection());
        }
        return vo;
    }
}
