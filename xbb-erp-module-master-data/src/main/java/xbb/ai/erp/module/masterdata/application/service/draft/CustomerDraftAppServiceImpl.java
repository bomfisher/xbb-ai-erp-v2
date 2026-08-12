package xbb.ai.erp.module.masterdata.application.service.draft;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerDraftListDTO;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerDraftLoadDTO;
import xbb.ai.erp.module.masterdata.admin.dto.CustomerDraftSaveDTO;
import xbb.ai.erp.module.masterdata.admin.vo.CustomerDraftDetailVO;
import xbb.ai.erp.module.masterdata.admin.vo.CustomerDraftListItemVO;
import xbb.ai.erp.module.masterdata.application.pojo.CustomerSaveDraftPojo;
import xbb.ai.erp.module.masterdata.application.port.CustomerDraftRepository;
import xbb.ai.erp.module.masterdata.application.validator.CustomerSaveCommonValidator;
import xbb.ai.erp.module.masterdata.application.validator.CustomerSaveProtocolValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerDraftAppServiceImpl implements CustomerDraftAppService {

    private final CustomerDraftRepository repository;
    private final CustomerSaveProtocolValidator protocolValidator;
    private final CustomerSaveCommonValidator commonValidator;

    @Override
    public DraftSaveVO saveDraft(CustomerDraftSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForDraft(dto);
        CustomerSaveDraftPojo draft = new CustomerSaveDraftPojo();
        draft.setCorpid(dto.getCorpid());
        draft.setMain(dto.getMain());
        draft.setContacts(dto.getContacts());
        draft.setDraftCode(dto.getDraftMeta().getDraftCode());
        draft.setDraftTitle(dto.getDraftMeta().getDraftTitle());
        String code = repository.saveDraft(draft);
        dto.getDraftMeta().setDraftCode(code);
        DraftSaveVO vo = new DraftSaveVO();
        vo.setDraftCode(code);
        return vo;
    }

    @Override
    public List<CustomerDraftListItemVO> draftList(CustomerDraftListDTO dto) {
        return repository.listDrafts(dto.getCorpid(), 10).stream().map(draft -> {
            CustomerDraftListItemVO vo = new CustomerDraftListItemVO();
            vo.setDraftCode(draft.getDraftCode());
            vo.setDraftTitle(draft.getDraftTitle());
            return vo;
        }).toList();
    }

    @Override
    public CustomerDraftDetailVO loadDraft(CustomerDraftLoadDTO dto) {
        CustomerSaveDraftPojo draft = repository.loadDraft(dto.getCorpid(), dto.getDraftCode());
        CustomerDraftDetailVO vo = new CustomerDraftDetailVO();
        if (draft != null) { vo.setDraftCode(draft.getDraftCode()); vo.setMain(draft.getMain()); vo.setContacts(draft.getContacts()); }
        return vo;
    }
}
