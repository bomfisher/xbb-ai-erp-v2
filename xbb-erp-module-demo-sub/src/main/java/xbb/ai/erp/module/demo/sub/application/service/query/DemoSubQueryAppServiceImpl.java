package xbb.ai.erp.module.demo.sub.application.service.query;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.common.application.util.ListQueryMapUtil;
import xbb.ai.erp.module.common.application.render.ListValueRenderer;
import xbb.ai.erp.module.demo.sub.admin.vo.*;
import xbb.ai.erp.module.demo.sub.application.assembler.DemoSubAdminAssembler;
import xbb.ai.erp.module.demo.sub.application.assembler.DemoSubFieldAssembler;
import xbb.ai.erp.module.demo.sub.application.field.DemoSubFieldFactory;
import xbb.ai.erp.module.demo.sub.application.port.DemoLookupPort;
import xbb.ai.erp.module.demo.sub.application.schema.DemoSubListQueryAdapter;
import xbb.ai.erp.module.demo.sub.application.schema.DemoSubListSchemaProvider;
import xbb.ai.erp.module.demo.sub.domain.model.DemoSub;
import xbb.ai.erp.module.demo.sub.domain.repository.DemoSubRepository;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Service
@RequiredArgsConstructor
public class DemoSubQueryAppServiceImpl {
  private final DemoSubRepository repository;
  private final DemoSubListQueryAdapter queryAdapter;
  private final DemoSubFieldFactory fieldFactory;
  private final DemoLookupPort demoLookupPort;
  private final DemoSubListSchemaProvider schemaProvider;
  private final ListValueRenderer listValueRenderer;
  private final ListQueryMapUtil listQueryMapUtil = new ListQueryMapUtil();

  public ListBaseVO<DemoSubListItemVO> list(ListBaseDTO dto) {
    AdminParamValidator.requireCorpid(dto);
    Map<String, Object> conditions = listQueryMapUtil.gen(dto, schemaProvider.conditionMetaMap());
    List<DemoSub> rows = repository.findByCondition(conditions);
    Long total = repository.count(conditions);
    ListBaseVO<DemoSubListItemVO> vo = new ListBaseVO<>();
    List<DemoSubListItemVO> list =
        rows.stream().map(row -> DemoSubAdminAssembler.toListItemVO(row, null)).toList();
    vo.setList(listValueRenderer.render(dto.getCorpid(), BusinessCodeEnum.DEMO_SUB.getCode(), list));
    vo.setPageHelper(
        new ListBaseVO.PageHelper(dto.getPageNum(), total == null ? 0 : total.intValue()));
    return vo;
  }

  public SaveItemVO<DemoSubSaveItemVO> addItem(BaseDTO dto) {
    AdminParamValidator.requireCorpid(dto);
    SaveItemVO<DemoSubSaveItemVO> vo = new SaveItemVO<>();
    vo.setHeadList(
        DemoSubFieldAssembler.buildHeadList(
            fieldFactory.getFields(SceneTypeEnum.CREATE), dto.getCorpid()));
    vo.setData(DemoSubAdminAssembler.buildEmptySaveItemVO());
    return vo;
  }

  public SaveItemVO<DemoSubSaveItemVO> updateItem(IdBaseDTO dto) {
    AdminParamValidator.validateIdQuery(dto);
    SaveItemVO<DemoSubSaveItemVO> vo = new SaveItemVO<>();
    vo.setHeadList(
        DemoSubFieldAssembler.buildHeadList(
            fieldFactory.getFields(SceneTypeEnum.UPDATE), dto.getCorpid()));
    vo.setData(
        DemoSubAdminAssembler.toSaveItemVO(repository.findById(dto.getCorpid(), dto.getId())));
    return vo;
  }

  public DemoSubDetailVO detail(IdBaseDTO dto) {
    return DemoSubAdminAssembler.toDetailVO(updateItem(dto).getData());
  }
}
