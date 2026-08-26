package xbb.ai.erp.module.sales.application.service.draft;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceDraftListDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceDraftLoadDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceDraftSaveDTO;
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceDraftDetailVO;
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceDraftListItemVO;
import xbb.ai.erp.module.sales.application.pojo.SalesInvoiceSaveDraftPojo;
import xbb.ai.erp.module.sales.application.port.SalesInvoiceDraftRepository;
import xbb.ai.erp.module.sales.application.validator.SalesInvoiceSaveCommonValidator;
import xbb.ai.erp.module.sales.application.validator.SalesInvoiceSaveProtocolValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesInvoiceDraftAppServiceImpl implements SalesInvoiceDraftAppService {

    private final SalesInvoiceDraftRepository repository;
    private final SalesInvoiceSaveProtocolValidator protocolValidator;
    private final SalesInvoiceSaveCommonValidator commonValidator;

    @Override
    public DraftSaveVO saveDraft(SalesInvoiceDraftSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForDraft(dto);
        SalesInvoiceSaveDraftPojo draft = new SalesInvoiceSaveDraftPojo();
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
    public List<SalesInvoiceDraftListItemVO> draftList(SalesInvoiceDraftListDTO dto) {
        return repository.listDrafts(dto.getCorpid(), 10).stream().map(draft -> {
            SalesInvoiceDraftListItemVO vo = new SalesInvoiceDraftListItemVO();
            vo.setDraftCode(draft.getDraftCode());
            vo.setDraftTitle(draft.getDraftTitle());
            return vo;
        }).toList();
    }

    @Override
    public SalesInvoiceDraftDetailVO loadDraft(SalesInvoiceDraftLoadDTO dto) {
        SalesInvoiceSaveDraftPojo draft = repository.loadDraft(dto.getCorpid(), dto.getDraftCode());
        SalesInvoiceDraftDetailVO vo = new SalesInvoiceDraftDetailVO();
        if (draft != null) { vo.setDraftCode(draft.getDraftCode()); vo.setMain(draft.getMain()); }
        return vo;
    }
}
