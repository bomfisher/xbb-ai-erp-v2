package xbb.ai.erp.module.masterdata.application.service.draft;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.masterdata.admin.dto.SupplierDraftListDTO;
import xbb.ai.erp.module.masterdata.admin.dto.SupplierDraftLoadDTO;
import xbb.ai.erp.module.masterdata.admin.dto.SupplierDraftSaveDTO;
import xbb.ai.erp.module.masterdata.admin.vo.SupplierDraftDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.SupplierDraftListItemVO;
import xbb.ai.erp.module.masterdata.application.pojo.SupplierSaveDraftPojo;
import xbb.ai.erp.module.masterdata.application.port.SupplierDraftRepository;
import xbb.ai.erp.module.masterdata.application.validator.SupplierSaveCommonValidator;
import xbb.ai.erp.module.masterdata.application.validator.SupplierSaveProtocolValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierDraftAppServiceImpl implements SupplierDraftAppService {

    private final SupplierDraftRepository repository;
    private final SupplierSaveProtocolValidator protocolValidator;
    private final SupplierSaveCommonValidator commonValidator;

    @Override
    public DraftSaveVO saveDraft(SupplierDraftSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForDraft(dto);
        SupplierSaveDraftPojo draft = new SupplierSaveDraftPojo();
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
    public List<SupplierDraftListItemVO> draftList(SupplierDraftListDTO dto) {
        return repository.listDrafts(dto.getCorpid(), 10).stream().map(draft -> {
            SupplierDraftListItemVO vo = new SupplierDraftListItemVO();
            vo.setDraftCode(draft.getDraftCode());
            vo.setDraftTitle(draft.getDraftTitle());
            return vo;
        }).toList();
    }

    @Override
    public SupplierDraftDetailVO loadDraft(SupplierDraftLoadDTO dto) {
        SupplierSaveDraftPojo draft = repository.loadDraft(dto.getCorpid(), dto.getDraftCode());
        SupplierDraftDetailVO vo = new SupplierDraftDetailVO();
        if (draft != null) { vo.setDraftCode(draft.getDraftCode()); vo.setMain(draft.getMain()); }
        return vo;
    }
}
