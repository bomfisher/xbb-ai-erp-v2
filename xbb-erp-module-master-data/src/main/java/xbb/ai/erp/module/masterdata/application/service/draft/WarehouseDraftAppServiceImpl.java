package xbb.ai.erp.module.masterdata.application.service.draft;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.masterdata.admin.dto.WarehouseDraftListDTO;
import xbb.ai.erp.module.masterdata.admin.dto.WarehouseDraftLoadDTO;
import xbb.ai.erp.module.masterdata.admin.dto.WarehouseDraftSaveDTO;
import xbb.ai.erp.module.masterdata.admin.vo.WarehouseDraftDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.WarehouseDraftListItemVO;
import xbb.ai.erp.module.masterdata.application.pojo.WarehouseSaveDraftPojo;
import xbb.ai.erp.module.masterdata.application.port.WarehouseDraftRepository;
import xbb.ai.erp.module.masterdata.application.validator.WarehouseSaveCommonValidator;
import xbb.ai.erp.module.masterdata.application.validator.WarehouseSaveProtocolValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseDraftAppServiceImpl implements WarehouseDraftAppService {

    private final WarehouseDraftRepository repository;
    private final WarehouseSaveProtocolValidator protocolValidator;
    private final WarehouseSaveCommonValidator commonValidator;

    @Override
    public DraftSaveVO saveDraft(WarehouseDraftSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForDraft(dto);
        WarehouseSaveDraftPojo draft = new WarehouseSaveDraftPojo();
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
    public List<WarehouseDraftListItemVO> draftList(WarehouseDraftListDTO dto) {
        return repository.listDrafts(dto.getCorpid(), 10).stream().map(draft -> {
            WarehouseDraftListItemVO vo = new WarehouseDraftListItemVO();
            vo.setDraftCode(draft.getDraftCode());
            vo.setDraftTitle(draft.getDraftTitle());
            return vo;
        }).toList();
    }

    @Override
    public WarehouseDraftDetailVO loadDraft(WarehouseDraftLoadDTO dto) {
        WarehouseSaveDraftPojo draft = repository.loadDraft(dto.getCorpid(), dto.getDraftCode());
        WarehouseDraftDetailVO vo = new WarehouseDraftDetailVO();
        if (draft != null) { vo.setDraftCode(draft.getDraftCode()); vo.setMain(draft.getMain()); }
        return vo;
    }
}
