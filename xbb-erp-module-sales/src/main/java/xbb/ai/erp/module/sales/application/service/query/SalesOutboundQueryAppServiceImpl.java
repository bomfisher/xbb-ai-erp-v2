package xbb.ai.erp.module.sales.application.service.query;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.enums.AuditStatusEnum;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.module.common.application.util.ListQueryMapUtil;
import xbb.ai.erp.module.common.application.render.ListValueRenderer;
import xbb.ai.erp.scene.meta.SceneFieldAssembler;
import xbb.ai.erp.scene.meta.SceneTypeEnum;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundDetailVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundListItemVO;
import xbb.ai.erp.module.sales.application.assembler.SalesOutboundAdminAssembler;
import xbb.ai.erp.module.sales.application.field.SalesOutboundFieldFactory;
import xbb.ai.erp.module.sales.application.field.SalesOutboundFormSectionFactory;
import xbb.ai.erp.module.sales.application.schema.SalesOutboundListSchemaProvider;
import xbb.ai.erp.module.sales.domain.model.SalesOutbound;
import xbb.ai.erp.module.sales.domain.repository.SalesOutboundRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesOrderRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesOrderItemRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesOutboundItemRepository;
import xbb.ai.erp.module.sales.domain.model.SalesOrder;
import xbb.ai.erp.module.sales.domain.model.SalesOrderItem;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundItemDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundSelectionFillDTO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundSelectionFillVO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundSourceProductQueryDTO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundSourceProductOptionVO;

@Service
public class SalesOutboundQueryAppServiceImpl {
    private final SalesOutboundRepository salesOutboundRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final SalesOutboundItemRepository salesOutboundItemRepository;
    private final SalesOutboundFieldFactory fieldFactory;
    private final SalesOutboundListSchemaProvider schemaProvider;
    private final ListValueRenderer listValueRenderer;
    private final BizNoGenerator bizNoGenerator;
    private final ListQueryMapUtil listQueryMapUtil = new ListQueryMapUtil();

    public SalesOutboundQueryAppServiceImpl(
        SalesOutboundRepository salesOutboundRepository,
        SalesOrderRepository salesOrderRepository,
        SalesOrderItemRepository salesOrderItemRepository,
        SalesOutboundItemRepository salesOutboundItemRepository,
        SalesOutboundFieldFactory fieldFactory,
        SalesOutboundListSchemaProvider schemaProvider,
        ListValueRenderer listValueRenderer,
        BizNoGenerator bizNoGenerator) {
        this.salesOutboundRepository = salesOutboundRepository;
        this.salesOrderRepository = salesOrderRepository;
        this.salesOrderItemRepository = salesOrderItemRepository;
        this.salesOutboundItemRepository = salesOutboundItemRepository;
        this.fieldFactory = fieldFactory;
        this.schemaProvider = schemaProvider;
        this.listValueRenderer = listValueRenderer;
        this.bizNoGenerator = bizNoGenerator;
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
        AdminParamValidator.requireCorpid(dto);
        SaveItemVO<xbb.ai.erp.module.sales.admin.vo.SalesOutboundSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(buildFormHeadList(SceneTypeEnum.CREATE));
        vo.setFormSections(SalesOutboundFormSectionFactory.getSections(SceneTypeEnum.CREATE));
        vo.setLinkageConfig(buildFormLinkageConfig());
        xbb.ai.erp.module.sales.admin.vo.SalesOutboundSaveItemVO data = SalesOutboundAdminAssembler.buildEmptySaveItemVO();
        data.getMain().setOutboundNo(bizNoGenerator.next(dto.getCorpid(), BusinessCodeEnum.SALES_OUTBOUND.getCode()));
        vo.setData(data);
        return vo;
    }

    public SaveItemVO<xbb.ai.erp.module.sales.admin.vo.SalesOutboundSaveItemVO> updateItem(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        SalesOutbound entity = salesOutboundRepository.findById(dto.getCorpid(), dto.getId());
        SaveItemVO<xbb.ai.erp.module.sales.admin.vo.SalesOutboundSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(buildFormHeadList(SceneTypeEnum.UPDATE));
        vo.setFormSections(SalesOutboundFormSectionFactory.getSections(SceneTypeEnum.UPDATE));
        vo.setLinkageConfig(buildFormLinkageConfig());
        vo.setData(SalesOutboundAdminAssembler.toSaveItemVO(entity,
            salesOutboundItemRepository.findByCondition(Map.of("corpid", dto.getCorpid(), "salesOutboundId", dto.getId()))));
        return vo;
    }

    public SalesOutboundSelectionFillVO selectionFill(SalesOutboundSelectionFillDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        if (!"main.salesOrderId".equals(dto.getFieldAttr()) || dto.getReferenceId() == null) {
            throw new BizException("销售订单回填字段或来源数据不合法");
        }
        SalesOrder order = salesOrderRepository.findById(dto.getCorpid(), dto.getReferenceId());
        if (order == null || !AuditStatusEnum.allowsDownstream(order.getAuditStatus())) {
            throw new BizException("销售订单不存在或不可用");
        }
        List<SalesOutboundItemDTO> items = salesOrderItemRepository.findByCondition(
                Map.of("corpid", dto.getCorpid(), "salesOrderId", order.getId())).stream()
            .filter(this::hasPendingOutboundQuantity)
            .map(this::toOutboundItem)
            .toList();
        if (items.isEmpty()) {
            throw new BizException("销售订单已全部出库");
        }
        Map<String, Object> patch = new LinkedHashMap<>();
        patch.put("main.salesOrderId", order.getId());
        patch.put("main.customerId", order.getCustomerId());
        patch.put("items", items);
        SalesOutboundSelectionFillVO vo = new SalesOutboundSelectionFillVO();
        vo.setReferenceId(order.getId());
        vo.setPatch(patch);
        return vo;
    }

    public List<SalesOutboundSourceProductOptionVO> sourceProductQuickSearch(SalesOutboundSourceProductQueryDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        if (dto.getSalesOrderId() == null) {
            throw new BizException("请先选择销售订单");
        }
        SalesOrder order = salesOrderRepository.findById(dto.getCorpid(), dto.getSalesOrderId());
        if (order == null) {
            throw new BizException("销售订单不存在或不可用");
        }
        String keyword = dto.getKeyword() == null ? "" : dto.getKeyword().trim();
        return salesOrderItemRepository.findByCondition(Map.of("corpid", dto.getCorpid(), "salesOrderId", order.getId()))
            .stream()
            .filter(item -> dto.getId() == null || dto.getId().equals(item.getSkuId()))
            .filter(this::hasPendingOutboundQuantity)
            .filter(item -> keyword.isBlank() || containsIgnoreCase(item.getSkuName(), keyword))
            .map(this::toSourceProductOption)
            .toList();
    }

    private List<FieldEntity> buildFormHeadList(SceneTypeEnum scene) {
        return SceneFieldAssembler.buildHeadList(fieldFactory.getFields(scene)).stream().map(field -> {
            if ("main.salesOrderId".equals(field.getAttr())) {
                FieldEntity.SelectionFillConfig config = new FieldEntity.SelectionFillConfig();
                config.setEnabled(Boolean.TRUE);
                field.setSelectionFillConfig(config);
            }
            if ("items".equals(field.getAttr())) {
                field.getSubField().stream()
                    .filter(subField -> "skuId".equals(subField.getAttr()) && subField.getProductSelectConfig() != null)
                    .forEach(subField -> subField.getProductSelectConfig().setBusinessCode("SALES_OUTBOUND_SOURCE_PRODUCT"));
            }
            return field;
        }).toList();
    }

    private Map<String, Object> buildFormLinkageConfig() {
        Map<String, Object> customerClearPatch = new LinkedHashMap<>();
        customerClearPatch.put("main.salesOrderId", null);
        customerClearPatch.put("main.customerName", null);
        customerClearPatch.put("items", List.of());
        Map<String, Object> orderClearPatch = new LinkedHashMap<>();
        orderClearPatch.put("items", List.of());
        return Map.of(
            "rowAmount", Map.of("tableAttr", "items", "quantityAttr", "qty", "unitPriceAttr", "unitPrice", "amountAttr", "amount"),
            "aggregateAmount", Map.of("tableAttr", "items", "amountAttr", "amount", "targetAttr", "main.totalAmount"),
            "itemStock", Map.of("tableAttr", "items", "skuAttr", "skuId", "warehouseAttr", "warehouseId", "stockAttr", "stockQty"),
            "selectionQueryContexts", List.of(
                Map.of("fieldAttr", "main.salesOrderId", "contextAttr", "main.customerId", "paramName", "customerId"),
                Map.of("fieldAttr", "skuId", "contextAttr", "main.salesOrderId", "paramName", "salesOrderId")),
            "clearRules", List.of(
                Map.of("fieldAttr", "main.customerId", "patch", customerClearPatch),
                Map.of("fieldAttr", "main.salesOrderId", "patch", orderClearPatch))
        );
    }

    private boolean hasPendingOutboundQuantity(SalesOrderItem item) {
        return item.getQty() != null && item.getQty().compareTo(item.getDeliveredQty() == null ? BigDecimal.ZERO : item.getDeliveredQty()) > 0;
    }

    private SalesOutboundItemDTO toOutboundItem(SalesOrderItem orderItem) {
        SalesOutboundItemDTO item = new SalesOutboundItemDTO();
        item.setSalesOrderItemId(orderItem.getId());
        item.setSkuId(orderItem.getSkuId());
        item.setSkuName(orderItem.getSkuName());
        item.setWarehouseId(orderItem.getWarehouseId());
        item.setUnitName(orderItem.getUnitName());
        item.setQty(orderItem.getQty().subtract(orderItem.getDeliveredQty() == null ? BigDecimal.ZERO : orderItem.getDeliveredQty()));
        item.setUnitPrice(orderItem.getUnitPrice());
        return item;
    }

    private SalesOutboundSourceProductOptionVO toSourceProductOption(SalesOrderItem orderItem) {
        SalesOutboundItemDTO item = toOutboundItem(orderItem);
        SalesOutboundSourceProductOptionVO option = new SalesOutboundSourceProductOptionVO();
        option.setId(orderItem.getSkuId());
        option.setCode(String.valueOf(orderItem.getSkuId()));
        option.setName(orderItem.getSkuName());
        option.setLabel(orderItem.getSkuName() + "（可出库：" + item.getQty() + "）");
        option.setRemainingQty(item.getQty());
        Map<String, Object> linePatch = new LinkedHashMap<>();
        linePatch.put("productSourceType", "SALES_ORDER_ITEM");
        linePatch.put("salesOrderItemId", item.getSalesOrderItemId());
        linePatch.put("skuId", item.getSkuId());
        linePatch.put("skuName", item.getSkuName());
        linePatch.put("warehouseId", item.getWarehouseId());
        linePatch.put("unitName", item.getUnitName());
        linePatch.put("qty", item.getQty());
        linePatch.put("unitPrice", item.getUnitPrice());
        option.setLinePatch(linePatch);
        return option;
    }

    private boolean containsIgnoreCase(String value, String keyword) {
        return value != null && value.toLowerCase(java.util.Locale.ROOT).contains(keyword.toLowerCase(java.util.Locale.ROOT));
    }

    public SalesOutboundDetailVO detail(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        SalesOutbound entity = salesOutboundRepository.findById(dto.getCorpid(), dto.getId());
        return SalesOutboundAdminAssembler.toDetailVO(SalesOutboundAdminAssembler.toSaveItemVO(entity));
    }
}
