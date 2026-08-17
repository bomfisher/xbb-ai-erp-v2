package xbb.ai.erp.module.sales.application.service.query;

import java.util.List;
import java.util.Map;
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
import xbb.ai.erp.scene.meta.SceneFieldAssembler;
import xbb.ai.erp.scene.meta.SceneTypeEnum;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundDetailVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundListItemVO;
import xbb.ai.erp.module.sales.application.assembler.SalesOutboundAdminAssembler;
import xbb.ai.erp.module.sales.application.field.SalesOutboundFieldFactory;
import xbb.ai.erp.module.sales.application.schema.SalesOutboundListSchemaProvider;
import xbb.ai.erp.module.sales.domain.model.SalesOutbound;
import xbb.ai.erp.module.sales.domain.repository.SalesOutboundRepository;

@Service
public class SalesOutboundQueryAppServiceImpl {
    private final SalesOutboundRepository salesOutboundRepository;
    private final SalesOutboundFieldFactory fieldFactory;
    private final SalesOutboundListSchemaProvider schemaProvider;
    private final ListValueRenderer listValueRenderer;
    private final ListQueryMapUtil listQueryMapUtil = new ListQueryMapUtil();

    public SalesOutboundQueryAppServiceImpl(SalesOutboundRepository salesOutboundRepository, SalesOutboundFieldFactory fieldFactory, SalesOutboundListSchemaProvider schemaProvider, ListValueRenderer listValueRenderer) {
        this.salesOutboundRepository = salesOutboundRepository; this.fieldFactory = fieldFactory; this.schemaProvider = schemaProvider; this.listValueRenderer = listValueRenderer;
    }

    public ListBaseVO<SalesOutboundListItemVO> list(ListBaseDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = listQueryMapUtil.gen(dto, schemaProvider.conditionMetaMap());
        List<SalesOutbound> list = salesOutboundRepository.findByCondition(conditionMap);
        Long total = salesOutboundRepository.count(conditionMap);
        ListBaseVO<SalesOutboundListItemVO> vo = new ListBaseVO<>();
        List<SalesOutboundListItemVO> items = list.stream().map(SalesOutboundAdminAssembler::toListItemVO).toList();
        vo.setList(listValueRenderer.render(dto.getCorpid(), BusinessCodeEnum.SALES_OUTBOUND.getCode(), items));
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    public SaveItemVO<xbb.ai.erp.module.sales.admin.vo.SalesOutboundSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<xbb.ai.erp.module.sales.admin.vo.SalesOutboundSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(SceneFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.CREATE)));
        vo.setData(SalesOutboundAdminAssembler.buildEmptySaveItemVO());
        vo.setData(SalesOutboundAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    public SaveItemVO<xbb.ai.erp.module.sales.admin.vo.SalesOutboundSaveItemVO> updateItem(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        SalesOutbound entity = salesOutboundRepository.findById(dto.getCorpid(), dto.getId());
        SaveItemVO<xbb.ai.erp.module.sales.admin.vo.SalesOutboundSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(SceneFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.UPDATE)));
        vo.setData(SalesOutboundAdminAssembler.toSaveItemVO(entity));
        vo.setData(SalesOutboundAdminAssembler.toSaveItemVO(entity));
        return vo;
    }

    public SalesOutboundDetailVO detail(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        SalesOutbound entity = salesOutboundRepository.findById(dto.getCorpid(), dto.getId());
        return SalesOutboundAdminAssembler.toDetailVO(SalesOutboundAdminAssembler.toSaveItemVO(entity));
    }
}
