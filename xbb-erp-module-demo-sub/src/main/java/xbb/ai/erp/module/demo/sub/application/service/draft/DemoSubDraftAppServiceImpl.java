package xbb.ai.erp.module.demo.sub.application.service.draft;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.demo.sub.admin.dto.*;
import xbb.ai.erp.module.demo.sub.admin.vo.*;
import xbb.ai.erp.module.demo.sub.application.pojo.DemoSubSaveDraftPojo;
import xbb.ai.erp.module.demo.sub.application.port.DemoSubDraftRepository;
import xbb.ai.erp.module.demo.sub.application.validator.*;

@Service
@RequiredArgsConstructor
public class DemoSubDraftAppServiceImpl implements DemoSubDraftAppService {
  private final DemoSubDraftRepository repository;
  private final DemoSubSaveProtocolValidator protocolValidator = new DemoSubSaveProtocolValidator();
  private final DemoSubSaveCommonValidator commonValidator = new DemoSubSaveCommonValidator();

  public DraftSaveVO saveDraft(DemoSubDraftSaveDTO dto) {
    protocolValidator.validate(dto);
    commonValidator.validateForDraft(dto);
    DemoSubSaveDraftPojo draft = new DemoSubSaveDraftPojo();
    draft.setCorpid(dto.getCorpid());
    draft.setMain(dto.getMain());
    if (dto.getDraftMeta() != null) {
      draft.setDraftCode(dto.getDraftMeta().getDraftCode());
      draft.setDraftTitle(dto.getDraftMeta().getDraftTitle());
    }
    String code = repository.saveDraft(draft);
    if (dto.getDraftMeta() != null) dto.getDraftMeta().setDraftCode(code);
    DraftSaveVO vo = new DraftSaveVO();
    vo.setDraftCode(code);
    return vo;
  }

  public List<DemoSubDraftListItemVO> draftList(DemoSubDraftListDTO dto) {
    return repository.listDrafts(dto.getCorpid(), 10).stream()
        .map(
            draft -> {
              DemoSubDraftListItemVO vo = new DemoSubDraftListItemVO();
              vo.setDraftCode(draft.getDraftCode());
              vo.setDraftTitle(draft.getDraftTitle());
              return vo;
            })
        .toList();
  }

  public DemoSubDraftDetailVO loadDraft(DemoSubDraftLoadDTO dto) {
    DemoSubSaveDraftPojo draft = repository.loadDraft(dto.getCorpid(), dto.getDraftCode());
    DemoSubDraftDetailVO vo = new DemoSubDraftDetailVO();
    if (draft != null) {
      vo.setDraftCode(draft.getDraftCode());
      vo.setMain(draft.getMain());
    }
    return vo;
  }
}
