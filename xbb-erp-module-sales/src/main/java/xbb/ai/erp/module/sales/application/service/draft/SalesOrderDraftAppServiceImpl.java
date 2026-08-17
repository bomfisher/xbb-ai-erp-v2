package xbb.ai.erp.module.sales.application.service.draft;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderDraftListDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderDraftLoadDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderDraftSaveDTO;
import xbb.ai.erp.module.sales.admin.vo.SalesOrderDraftDetailVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOrderDraftListItemVO;
import xbb.ai.erp.module.sales.application.pojo.SalesOrderSaveDraftPojo;
import xbb.ai.erp.module.sales.application.port.SalesOrderDraftRepository;
import xbb.ai.erp.module.sales.application.validator.SalesOrderSaveCommonValidator;
import xbb.ai.erp.module.sales.application.validator.SalesOrderSaveProtocolValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesOrderDraftAppServiceImpl implements SalesOrderDraftAppService {

    private final SalesOrderDraftRepository repository;
    private final SalesOrderSaveProtocolValidator protocolValidator;
    private final SalesOrderSaveCommonValidator commonValidator;

    @Override
    public DraftSaveVO saveDraft(SalesOrderDraftSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForDraft(dto);
        SalesOrderSaveDraftPojo draft = new SalesOrderSaveDraftPojo();
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
    public List<SalesOrderDraftListItemVO> draftList(SalesOrderDraftListDTO dto) {
        return repository.listDrafts(dto.getCorpid(), 10).stream().map(draft -> {
            SalesOrderDraftListItemVO vo = new SalesOrderDraftListItemVO();
            vo.setDraftCode(draft.getDraftCode());
            vo.setDraftTitle(draft.getDraftTitle());
            return vo;
        }).toList();
    }

    @Override
    public SalesOrderDraftDetailVO loadDraft(SalesOrderDraftLoadDTO dto) {
        SalesOrderSaveDraftPojo draft = repository.loadDraft(dto.getCorpid(), dto.getDraftCode());
        SalesOrderDraftDetailVO vo = new SalesOrderDraftDetailVO();
        if (draft != null) { vo.setDraftCode(draft.getDraftCode()); vo.setMain(draft.getMain()); }
        return vo;
    }
}
