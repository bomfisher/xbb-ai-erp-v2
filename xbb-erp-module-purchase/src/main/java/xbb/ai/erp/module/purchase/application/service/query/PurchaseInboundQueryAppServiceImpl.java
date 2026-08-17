package xbb.ai.erp.module.purchase.application.service.query;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
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
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.module.common.application.render.ListValueRenderer;
import xbb.ai.erp.module.common.application.util.ListQueryMapUtil;
import xbb.ai.erp.scene.meta.SceneFieldAssembler;
import xbb.ai.erp.scene.meta.SceneTypeEnum;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundListItemVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundSelectionFillDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundItemDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundSelectionFillVO;
import xbb.ai.erp.module.purchase.application.assembler.PurchaseInboundAdminAssembler;
import xbb.ai.erp.module.purchase.application.field.PurchaseInboundFieldFactory;
import xbb.ai.erp.module.purchase.application.schema.PurchaseInboundListSchemaProvider;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInbound;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInboundRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInboundItemRepository;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrder;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrderItem;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderItemRepository;

@Service
public class PurchaseInboundQueryAppServiceImpl {
    private final PurchaseInboundRepository purchaseInboundRepository;
    private final PurchaseInboundItemRepository purchaseInboundItemRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final PurchaseInboundFieldFactory fieldFactory;
    private final PurchaseInboundListSchemaProvider schemaProvider;
    private final ListValueRenderer listValueRenderer;
    private final ListQueryMapUtil listQueryMapUtil = new ListQueryMapUtil();

    public PurchaseInboundQueryAppServiceImpl(PurchaseInboundRepository purchaseInboundRepository, PurchaseInboundItemRepository purchaseInboundItemRepository, PurchaseOrderRepository purchaseOrderRepository, PurchaseOrderItemRepository purchaseOrderItemRepository, PurchaseInboundFieldFactory fieldFactory, PurchaseInboundListSchemaProvider schemaProvider, ListValueRenderer listValueRenderer) {
        this.purchaseInboundRepository = purchaseInboundRepository;
        this.purchaseInboundItemRepository = purchaseInboundItemRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.purchaseOrderItemRepository = purchaseOrderItemRepository;
        this.fieldFactory = fieldFactory;
        this.schemaProvider = schemaProvider;
        this.listValueRenderer = listValueRenderer;
    }

    public ListBaseVO<PurchaseInboundListItemVO> list(ListBaseDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = listQueryMapUtil.gen(dto, schemaProvider.conditionMetaMap());
        List<PurchaseInbound> list = purchaseInboundRepository.findByCondition(conditionMap);
        Long total = purchaseInboundRepository.count(conditionMap);
        ListBaseVO<PurchaseInboundListItemVO> vo = new ListBaseVO<>();
        List<PurchaseInboundListItemVO> items = list.stream().map(PurchaseInboundAdminAssembler::toListItemVO).toList();
        vo.setList(listValueRenderer.render(dto.getCorpid(), BusinessCodeEnum.PURCHASE_INBOUND.getCode(), items));
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    public SaveItemVO<xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(buildFormHeadList(SceneTypeEnum.CREATE));
        vo.setLinkageConfig(buildFormLinkageConfig());
        vo.setData(PurchaseInboundAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    public SaveItemVO<xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundSaveItemVO> updateItem(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        PurchaseInbound entity = purchaseInboundRepository.findById(dto.getCorpid(), dto.getId());
        SaveItemVO<xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(buildFormHeadList(SceneTypeEnum.UPDATE));
        vo.setLinkageConfig(buildFormLinkageConfig());
        vo.setData(toSaveItemVO(dto.getCorpid(), entity));
        return vo;
    }

    public PurchaseInboundDetailVO detail(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        PurchaseInbound entity = purchaseInboundRepository.findById(dto.getCorpid(), dto.getId());
        return PurchaseInboundAdminAssembler.toDetailVO(toSaveItemVO(dto.getCorpid(), entity));
    }

    public PurchaseInboundSelectionFillVO selectionFill(PurchaseInboundSelectionFillDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        if (!"main.purchaseOrderId".equals(dto.getFieldAttr()) || dto.getReferenceId() == null) {
            throw new BizException("采购订单回填字段或来源数据不合法");
        }
        PurchaseOrder order = purchaseOrderRepository.findById(dto.getCorpid(), dto.getReferenceId());
        if (order == null) {
            throw new BizException("采购订单不存在或不可用");
        }
        List<PurchaseInboundItemDTO> items = purchaseOrderItemRepository.findByCondition(
                Map.of("corpid", dto.getCorpid(), "purchaseOrderId", order.getId())).stream()
            .filter(this::hasPendingInboundQuantity)
            .map(item -> toInboundItem(item, null))
            .toList();
        if (items.isEmpty()) {
            throw new BizException("采购订单已全部入库");
        }
        Map<String, Object> patch = new LinkedHashMap<>();
        patch.put("main.purchaseOrderId", order.getId());
        patch.put("main.supplierId", order.getSupplierId());
        patch.put("main.supplierName", order.getSupplierName());
        patch.put("items", items);
        PurchaseInboundSelectionFillVO vo = new PurchaseInboundSelectionFillVO();
        vo.setReferenceId(order.getId());
        vo.setPatch(patch);
        return vo;
    }

    private List<FieldEntity> buildFormHeadList(SceneTypeEnum scene) {
        return SceneFieldAssembler.buildHeadList(fieldFactory.getFields(scene)).stream().map(field -> {
            if ("main.purchaseOrderId".equals(field.getAttr())) {
                FieldEntity.SelectionFillConfig config = new FieldEntity.SelectionFillConfig();
                config.setEnabled(Boolean.TRUE);
                field.setSelectionFillConfig(config);
            }
            return field;
        }).toList();
    }

    private Map<String, Object> buildFormLinkageConfig() {
        Map<String, Object> clearPatch = new LinkedHashMap<>();
        clearPatch.put("main.purchaseOrderId", null);
        clearPatch.put("main.supplierName", null);
        clearPatch.put("items", List.of());
        Map<String, Object> purchaseOrderClearPatch = new LinkedHashMap<>();
        purchaseOrderClearPatch.put("items", List.of());
        return Map.of(
            "rowAmount", Map.of("tableAttr", "items", "quantityAttr", "qty", "unitPriceAttr", "unitPrice", "amountAttr", "amount"),
            "aggregateAmount", Map.of("tableAttr", "items", "amountAttr", "amount", "targetAttr", "main.totalAmount"),
            "warehouseSync", Map.of("headerAttr", "main.warehouseId", "tableAttr", "items", "itemAttr", "warehouseId", "message", "表头仓库已修改，是否同步修改分录仓库？"),
            "selectionQueryContexts", List.of(Map.of("fieldAttr", "main.purchaseOrderId", "contextAttr", "main.supplierId", "paramName", "supplierId")),
            "clearRules", List.of(
                Map.of("fieldAttr", "main.supplierId", "patch", clearPatch),
                Map.of("fieldAttr", "main.purchaseOrderId", "patch", purchaseOrderClearPatch))
        );
    }

    private boolean hasPendingInboundQuantity(PurchaseOrderItem item) {
        return item.getQty() != null && item.getQty().compareTo(item.getInboundQty() == null ? BigDecimal.ZERO : item.getInboundQty()) > 0;
    }

    private PurchaseInboundItemDTO toInboundItem(PurchaseOrderItem orderItem, Long warehouseId) {
        PurchaseInboundItemDTO item = new PurchaseInboundItemDTO();
        item.setPurchaseOrderItemId(orderItem.getId());
        item.setSkuId(orderItem.getSkuId());
        item.setSkuName(orderItem.getSkuName());
        item.setUnitName(orderItem.getUnitName());
        item.setWarehouseId(warehouseId);
        item.setQty(orderItem.getQty().subtract(orderItem.getInboundQty() == null ? BigDecimal.ZERO : orderItem.getInboundQty()));
        item.setUnitPrice(orderItem.getUnitPrice());
        item.setCostUnit(orderItem.getUnitPrice());
        return item;
    }

    private xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundSaveItemVO toSaveItemVO(String corpid, PurchaseInbound entity) {
        xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundSaveItemVO vo = PurchaseInboundAdminAssembler.toSaveItemVO(entity);
        if (entity != null && entity.getId() != null) {
            vo.setItems(PurchaseInboundAdminAssembler.toPurchaseInboundItemDTOs(
                purchaseInboundItemRepository.findByCondition(Map.of("corpid", corpid, "purchaseInboundId", entity.getId()))));
        }
        return vo;
    }
}
