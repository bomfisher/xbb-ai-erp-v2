package xbb.ai.erp.module.demo.application.service.draft;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.demo.admin.dto.DemoDraftListDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoDraftLoadDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoDraftSaveDTO;
import xbb.ai.erp.module.demo.admin.vo.DemoDraftDetailVO;
import xbb.ai.erp.module.demo.admin.vo.DemoDraftListItemVO;
import xbb.ai.erp.module.demo.application.pojo.DemoSaveDraftPojo;
import xbb.ai.erp.module.demo.application.port.DemoDraftRepository;
import xbb.ai.erp.module.demo.application.validator.DemoSaveCommonValidator;
import xbb.ai.erp.module.demo.application.validator.DemoSaveProtocolValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DemoDraftAppServiceImpl implements DemoDraftAppService {

    private final DemoDraftRepository repository;
    private final DemoSaveProtocolValidator protocolValidator;
    private final DemoSaveCommonValidator commonValidator;

    @Override
    public DraftSaveVO saveDraft(DemoDraftSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForDraft(dto);
        DemoSaveDraftPojo draft = new DemoSaveDraftPojo();
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
    public List<DemoDraftListItemVO> draftList(DemoDraftListDTO dto) {
        return repository.listDrafts(dto.getCorpid(), 10).stream().map(draft -> {
            DemoDraftListItemVO vo = new DemoDraftListItemVO();
            vo.setDraftCode(draft.getDraftCode());
            vo.setDraftTitle(draft.getDraftTitle());
            return vo;
        }).toList();
    }

    @Override
    public DemoDraftDetailVO loadDraft(DemoDraftLoadDTO dto) {
        DemoSaveDraftPojo draft = repository.loadDraft(dto.getCorpid(), dto.getDraftCode());
        DemoDraftDetailVO vo = new DemoDraftDetailVO();
        if (draft != null) { vo.setDraftCode(draft.getDraftCode()); vo.setMain(draft.getMain()); }
        return vo;
    }
}
