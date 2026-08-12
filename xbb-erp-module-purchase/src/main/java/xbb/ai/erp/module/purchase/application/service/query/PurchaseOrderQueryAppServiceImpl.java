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
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderListItemVO;
import xbb.ai.erp.module.purchase.application.assembler.PurchaseOrderAdminAssembler;
import xbb.ai.erp.module.purchase.application.field.PurchaseOrderFieldFactory;
import xbb.ai.erp.module.purchase.application.schema.PurchaseOrderListSchemaProvider;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrder;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderRepository;

@Service
public class PurchaseOrderQueryAppServiceImpl {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderFieldFactory fieldFactory;
    private final PurchaseOrderListSchemaProvider schemaProvider;
    private final ListQueryMapUtil listQueryMapUtil = new ListQueryMapUtil();

    public PurchaseOrderQueryAppServiceImpl(PurchaseOrderRepository purchaseOrderRepository, PurchaseOrderFieldFactory fieldFactory, PurchaseOrderListSchemaProvider schemaProvider) {
        this.purchaseOrderRepository = purchaseOrderRepository; this.fieldFactory = fieldFactory; this.schemaProvider = schemaProvider;
    }

    public ListBaseVO<PurchaseOrderListItemVO> list(ListBaseDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = listQueryMapUtil.gen(dto, schemaProvider.conditionMetaMap());
        List<PurchaseOrder> list = purchaseOrderRepository.findByCondition(conditionMap);
        Long total = purchaseOrderRepository.count(conditionMap);
        ListBaseVO<PurchaseOrderListItemVO> vo = new ListBaseVO<>();
        vo.setList(list.stream().map(PurchaseOrderAdminAssembler::toListItemVO).toList());
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    public SaveItemVO<xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(SceneFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.CREATE)));
        vo.setData(PurchaseOrderAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    public SaveItemVO<xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderSaveItemVO> updateItem(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        PurchaseOrder entity = purchaseOrderRepository.findById(dto.getCorpid(), dto.getId());
        SaveItemVO<xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(SceneFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.UPDATE)));
        vo.setData(PurchaseOrderAdminAssembler.toSaveItemVO(entity));
        return vo;
    }

    public PurchaseOrderDetailVO detail(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        PurchaseOrder entity = purchaseOrderRepository.findById(dto.getCorpid(), dto.getId());
        return PurchaseOrderAdminAssembler.toDetailVO(PurchaseOrderAdminAssembler.toSaveItemVO(entity));
    }
}
