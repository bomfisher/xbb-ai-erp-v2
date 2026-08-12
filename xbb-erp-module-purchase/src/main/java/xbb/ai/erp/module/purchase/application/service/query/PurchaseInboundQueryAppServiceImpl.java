package xbb.ai.erp.module.purchase.application.service.query;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.common.application.util.ListQueryMapUtil;
import xbb.ai.erp.scene.meta.SceneFieldAssembler;
import xbb.ai.erp.scene.meta.SceneTypeEnum;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundListItemVO;
import xbb.ai.erp.module.purchase.application.assembler.PurchaseInboundAdminAssembler;
import xbb.ai.erp.module.purchase.application.field.PurchaseInboundFieldFactory;
import xbb.ai.erp.module.purchase.application.schema.PurchaseInboundListSchemaProvider;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInbound;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInboundRepository;

@Service
public class PurchaseInboundQueryAppServiceImpl {
    private final PurchaseInboundRepository purchaseInboundRepository;
    private final PurchaseInboundFieldFactory fieldFactory;
    private final PurchaseInboundListSchemaProvider schemaProvider;
    private final ListQueryMapUtil listQueryMapUtil = new ListQueryMapUtil();

    public PurchaseInboundQueryAppServiceImpl(PurchaseInboundRepository purchaseInboundRepository, PurchaseInboundFieldFactory fieldFactory, PurchaseInboundListSchemaProvider schemaProvider) {
        this.purchaseInboundRepository = purchaseInboundRepository; this.fieldFactory = fieldFactory; this.schemaProvider = schemaProvider;
    }

    public ListBaseVO<PurchaseInboundListItemVO> list(ListBaseDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = listQueryMapUtil.gen(dto, schemaProvider.conditionMetaMap());
        List<PurchaseInbound> list = purchaseInboundRepository.findByCondition(conditionMap);
        Long total = purchaseInboundRepository.count(conditionMap);
        ListBaseVO<PurchaseInboundListItemVO> vo = new ListBaseVO<>();
        vo.setList(list.stream().map(PurchaseInboundAdminAssembler::toListItemVO).toList());
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    public SaveItemVO<xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(SceneFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.CREATE)));
        vo.setData(PurchaseInboundAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    public SaveItemVO<xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundSaveItemVO> updateItem(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        PurchaseInbound entity = purchaseInboundRepository.findById(dto.getCorpid(), dto.getId());
        SaveItemVO<xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(SceneFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.UPDATE)));
        vo.setData(PurchaseInboundAdminAssembler.toSaveItemVO(entity));
        return vo;
    }

    public PurchaseInboundDetailVO detail(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        PurchaseInbound entity = purchaseInboundRepository.findById(dto.getCorpid(), dto.getId());
        return PurchaseInboundAdminAssembler.toDetailVO(PurchaseInboundAdminAssembler.toSaveItemVO(entity));
    }
}
