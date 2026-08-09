package xbb.ai.erp.module.demo.sub.application.service;

import java.util.List;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.demo.sub.admin.dto.DemoSubDraftListDTO;
import xbb.ai.erp.module.demo.sub.admin.dto.DemoSubDraftLoadDTO;
import xbb.ai.erp.module.demo.sub.admin.dto.DemoSubDraftSaveDTO;
import xbb.ai.erp.module.demo.sub.admin.dto.DemoSubSaveDTO;
import xbb.ai.erp.module.demo.sub.admin.dto.DemoSubSubmitSaveDTO;
import xbb.ai.erp.module.demo.sub.admin.vo.DemoSubDetailVO;
import xbb.ai.erp.module.demo.sub.admin.vo.DemoSubDraftDetailVO;
import xbb.ai.erp.module.demo.sub.admin.vo.DemoSubDraftListItemVO;
import xbb.ai.erp.module.demo.sub.admin.vo.DemoSubListItemVO;
import xbb.ai.erp.module.demo.sub.admin.vo.DemoSubSaveItemVO;

public interface DemoSubAdminAppService {
  ListBaseVO<DemoSubListItemVO> list(ListBaseDTO dto);

  SaveItemVO<DemoSubSaveItemVO> addItem(BaseDTO dto);

  SaveItemVO<DemoSubSaveItemVO> updateItem(IdBaseDTO dto);

  DraftSaveVO saveDraft(DemoSubDraftSaveDTO dto);

  BaseVO saveAndSubmit(DemoSubSubmitSaveDTO dto);

  List<DemoSubDraftListItemVO> draftList(DemoSubDraftListDTO dto);

  DemoSubDraftDetailVO loadDraft(DemoSubDraftLoadDTO dto);

  Long save(DemoSubSaveDTO dto);

  DemoSubDetailVO detail(IdBaseDTO dto);

  void delete(BatchBaseDTO dto);
}
