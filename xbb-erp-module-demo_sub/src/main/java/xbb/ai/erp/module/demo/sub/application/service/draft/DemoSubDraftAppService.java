package xbb.ai.erp.module.demo.sub.application.service.draft;

import java.util.List;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.module.demo.sub.admin.dto.*;
import xbb.ai.erp.module.demo.sub.admin.vo.*;

public interface DemoSubDraftAppService {
  DraftSaveVO saveDraft(DemoSubDraftSaveDTO dto);

  List<DemoSubDraftListItemVO> draftList(DemoSubDraftListDTO dto);

  DemoSubDraftDetailVO loadDraft(DemoSubDraftLoadDTO dto);
}
