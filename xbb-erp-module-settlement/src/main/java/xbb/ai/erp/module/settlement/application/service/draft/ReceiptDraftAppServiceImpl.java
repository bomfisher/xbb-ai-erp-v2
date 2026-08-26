package xbb.ai.erp.module.settlement.application.service.draft;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptDraftListDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptDraftLoadDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceiptDraftSaveDTO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptDraftDetailVO;
import xbb.ai.erp.module.settlement.admin.vo.ReceiptDraftListItemVO;
import xbb.ai.erp.module.settlement.application.pojo.ReceiptSaveDraftPojo;
import xbb.ai.erp.module.settlement.application.port.ReceiptDraftRepository;
import xbb.ai.erp.module.settlement.application.validator.ReceiptSaveCommonValidator;
import xbb.ai.erp.module.settlement.application.validator.ReceiptSaveProtocolValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptDraftAppServiceImpl implements ReceiptDraftAppService {

    private final ReceiptDraftRepository repository;
    private final ReceiptSaveProtocolValidator protocolValidator;
    private final ReceiptSaveCommonValidator commonValidator;

    @Override
    public DraftSaveVO saveDraft(ReceiptDraftSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForDraft(dto);
        ReceiptSaveDraftPojo draft = new ReceiptSaveDraftPojo();
        draft.setCorpid(dto.getCorpid());
        draft.setMain(dto.getMain());
        draft.setPayments(dto.getPayments());
        draft.setWriteOffs(dto.getWriteOffs());
        draft.setDraftCode(dto.getDraftMeta().getDraftCode());
        draft.setDraftTitle(dto.getDraftMeta().getDraftTitle());
        String code = repository.saveDraft(draft);
        dto.getDraftMeta().setDraftCode(code);
        DraftSaveVO vo = new DraftSaveVO();
        vo.setDraftCode(code);
        return vo;
    }

    @Override
    public List<ReceiptDraftListItemVO> draftList(ReceiptDraftListDTO dto) {
        return repository.listDrafts(dto.getCorpid(), 10).stream().map(draft -> {
            ReceiptDraftListItemVO vo = new ReceiptDraftListItemVO();
            vo.setDraftCode(draft.getDraftCode());
            vo.setDraftTitle(draft.getDraftTitle());
            return vo;
        }).toList();
    }

    @Override
    public ReceiptDraftDetailVO loadDraft(ReceiptDraftLoadDTO dto) {
        ReceiptSaveDraftPojo draft = repository.loadDraft(dto.getCorpid(), dto.getDraftCode());
        ReceiptDraftDetailVO vo = new ReceiptDraftDetailVO();
        if (draft != null) {
            vo.setDraftCode(draft.getDraftCode());
            vo.setMain(draft.getMain());
            vo.setPayments(draft.getPayments());
            vo.setWriteOffs(draft.getWriteOffs());
        }
        return vo;
    }
}
