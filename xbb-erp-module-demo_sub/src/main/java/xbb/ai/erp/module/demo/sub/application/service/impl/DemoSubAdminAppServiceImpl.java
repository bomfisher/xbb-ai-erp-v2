package xbb.ai.erp.module.demo.sub.application.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.DraftSaveVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.demo.sub.admin.dto.*;
import xbb.ai.erp.module.demo.sub.admin.vo.*;
import xbb.ai.erp.module.demo.sub.application.service.DemoSubAdminAppService;
import xbb.ai.erp.module.demo.sub.application.service.draft.DemoSubDraftAppService;
import xbb.ai.erp.module.demo.sub.application.service.query.DemoSubQueryAppServiceImpl;
import xbb.ai.erp.module.demo.sub.application.service.save.DemoSubSaveAppServiceImpl;

@Service
@RequiredArgsConstructor
public class DemoSubAdminAppServiceImpl implements DemoSubAdminAppService {
  private final DemoSubQueryAppServiceImpl queryService;
  private final DemoSubSaveAppServiceImpl saveService;
  private final DemoSubDraftAppService draftService;
  private final xbb.ai.erp.module.demo.sub.application.port.DemoLookupPort demoLookupPort;

  @Override
  public ListBaseVO<DemoSubListItemVO> list(ListBaseDTO dto) {
    return queryService.list(dto);
  }

  @Override
  public SaveItemVO<DemoSubSaveItemVO> addItem(BaseDTO dto) {
    return queryService.addItem(dto);
  }

  @Override
  public SaveItemVO<DemoSubSaveItemVO> updateItem(IdBaseDTO dto) {
    return queryService.updateItem(dto);
  }

  @Override
  public DemoSubSelectionFillVO selectionFill(DemoSubSelectionFillDTO dto) {
    if (!"main.dataId".equals(dto.getFieldAttr()) || dto.getReferenceId() == null) {
      throw new xbb.ai.erp.base.common.exception.BizException("回填来源字段或关联数据不能为空");
    }
    String parentName = demoLookupPort.findNamesByIds(dto.getCorpid(), java.util.Set.of(dto.getReferenceId()))
        .get(dto.getReferenceId());
    if (parentName == null) {
      throw new xbb.ai.erp.base.common.exception.BizException("关联DEMO不存在或不可用");
    }
    DemoSubSelectionFillVO vo = new DemoSubSelectionFillVO();
    vo.setReferenceId(dto.getReferenceId());
    vo.setPatch(java.util.Map.of("main.parentName", parentName));
    return vo;
  }

  @Override
  public DraftSaveVO saveDraft(DemoSubDraftSaveDTO dto) {
    return draftService.saveDraft(dto);
  }

  @Override
  public BaseVO saveAndSubmit(DemoSubSubmitSaveDTO dto) {
    return saveService.saveAndSubmit(dto);
  }

  @Override
  public List<DemoSubDraftListItemVO> draftList(DemoSubDraftListDTO dto) {
    return draftService.draftList(dto);
  }

  @Override
  public DemoSubDraftDetailVO loadDraft(DemoSubDraftLoadDTO dto) {
    return draftService.loadDraft(dto);
  }

  @Override
  public Long save(DemoSubSaveDTO dto) {
    return saveService.save(dto);
  }

  @Override
  public DemoSubDetailVO detail(IdBaseDTO dto) {
    return queryService.detail(dto);
  }

  @Override
  public void delete(BatchBaseDTO dto) {
    saveService.delete(dto);
  }
}
