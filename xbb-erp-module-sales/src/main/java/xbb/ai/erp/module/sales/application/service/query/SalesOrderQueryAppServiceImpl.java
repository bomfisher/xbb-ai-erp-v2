package xbb.ai.erp.module.sales.application.service.query;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.base.common.enums.ApprovalStatusEnum;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.common.application.util.ListQueryMapUtil;
import xbb.ai.erp.module.common.application.render.ListValueRenderer;
import xbb.ai.erp.scene.meta.SceneFieldAssembler;
import xbb.ai.erp.scene.meta.SceneTypeEnum;
import xbb.ai.erp.module.sales.admin.vo.SalesOrderDetailVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOrderListItemVO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderBusinessSelectQueryDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderItemStockQueryDTO;
import xbb.ai.erp.module.sales.admin.vo.SalesOrderBusinessSelectOptionVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOrderItemStockVO;
import xbb.ai.erp.module.sales.application.assembler.SalesOrderAdminAssembler;
import xbb.ai.erp.module.sales.application.field.SalesOrderFieldFactory;
import xbb.ai.erp.module.sales.application.field.SalesOrderFormSectionFactory;
import xbb.ai.erp.module.sales.application.schema.SalesOrderListSchemaProvider;
import xbb.ai.erp.module.sales.domain.model.SalesOrder;
import xbb.ai.erp.module.sales.domain.repository.SalesOrderRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesOrderItemRepository;
import xbb.ai.erp.module.inventory.contract.InventoryStockQueryApi;

@Service
public class SalesOrderQueryAppServiceImpl {
    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final SalesOrderFieldFactory fieldFactory;
    private final SalesOrderListSchemaProvider schemaProvider;
    private final ListValueRenderer listValueRenderer;
    private final InventoryStockQueryApi inventoryStockQueryApi;
    private final BizNoGenerator bizNoGenerator;
    private final ListQueryMapUtil listQueryMapUtil = new ListQueryMapUtil();

    public SalesOrderQueryAppServiceImpl(
        SalesOrderRepository salesOrderRepository,
        SalesOrderItemRepository salesOrderItemRepository,
        SalesOrderFieldFactory fieldFactory,
        SalesOrderListSchemaProvider schemaProvider,
        ListValueRenderer listValueRenderer,
        InventoryStockQueryApi inventoryStockQueryApi,
        BizNoGenerator bizNoGenerator) {
        this.salesOrderRepository = salesOrderRepository;
        this.salesOrderItemRepository = salesOrderItemRepository;
        this.fieldFactory = fieldFactory;
        this.schemaProvider = schemaProvider;
        this.listValueRenderer = listValueRenderer;
        this.inventoryStockQueryApi = inventoryStockQueryApi;
        this.bizNoGenerator = bizNoGenerator;
    }

    public ListBaseVO<SalesOrderListItemVO> list(ListBaseDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = listQueryMapUtil.gen(dto, schemaProvider.conditionMetaMap());
        conditionMap.put("businessVisibleApprovalStatuses", List.of(ApprovalStatusEnum.APPROVED.getCode(),
            ApprovalStatusEnum.NO_NEED_APPROVED.getCode()));
        List<SalesOrder> list = salesOrderRepository.findByCondition(conditionMap);
        Long total = salesOrderRepository.count(conditionMap);
        ListBaseVO<SalesOrderListItemVO> vo = new ListBaseVO<>();
        List<SalesOrderListItemVO> items = list.stream().map(SalesOrderAdminAssembler::toListItemVO).toList();
        vo.setList(listValueRenderer.render(dto.getCorpid(), BusinessCodeEnum.SALES_ORDER.getCode(), items));
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    public SaveItemVO<xbb.ai.erp.module.sales.admin.vo.SalesOrderSaveItemVO> addItem(BaseDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        SaveItemVO<xbb.ai.erp.module.sales.admin.vo.SalesOrderSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(SceneFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.CREATE)));
        vo.setFormSections(SalesOrderFormSectionFactory.getSections(SceneTypeEnum.CREATE));
        vo.setLinkageConfig(linkageConfig());
        xbb.ai.erp.module.sales.admin.vo.SalesOrderSaveItemVO data = SalesOrderAdminAssembler.buildEmptySaveItemVO();
        data.getMain().setOrderNo(bizNoGenerator.next(dto.getCorpid(), BusinessCodeEnum.SALES_ORDER.getCode()));
        vo.setData(data);
        return vo;
    }

    public SaveItemVO<xbb.ai.erp.module.sales.admin.vo.SalesOrderSaveItemVO> updateItem(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        SalesOrder entity = salesOrderRepository.findById(dto.getCorpid(), dto.getId());
        SaveItemVO<xbb.ai.erp.module.sales.admin.vo.SalesOrderSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(SceneFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.UPDATE)));
        vo.setFormSections(SalesOrderFormSectionFactory.getSections(SceneTypeEnum.UPDATE));
        vo.setLinkageConfig(linkageConfig());
        vo.setData(toSaveItemVO(dto.getCorpid(), entity));
        return vo;
    }

    public SalesOrderDetailVO detail(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        SalesOrder entity = salesOrderRepository.findById(dto.getCorpid(), dto.getId());
        return SalesOrderAdminAssembler.toDetailVO(toSaveItemVO(dto.getCorpid(), entity));
    }

    public List<SalesOrderBusinessSelectOptionVO> businessSelectQuickSearch(SalesOrderBusinessSelectQueryDTO dto) {
        return findBusinessSelectOptions(dto, 0, 5);
    }

    public ListBaseVO<SalesOrderBusinessSelectOptionVO> businessSelectDialogSearch(SalesOrderBusinessSelectQueryDTO dto) {
        int pageNum = dto.getPageNum() == null || dto.getPageNum() < 1 ? 1 : dto.getPageNum();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? 20 : dto.getPageSize();
        Map<String, Object> conditions = businessSelectConditions(dto, (pageNum - 1) * pageSize, pageSize);
        List<SalesOrderBusinessSelectOptionVO> options = salesOrderRepository.findByCondition(conditions).stream()
            .map(this::toBusinessSelectOption).toList();
        Long total = salesOrderRepository.count(businessSelectConditions(dto, null, null));
        ListBaseVO<SalesOrderBusinessSelectOptionVO> vo = new ListBaseVO<>();
        vo.setList(options);
        vo.setPageHelper(new ListBaseVO.PageHelper(pageNum, total == null ? 0 : total.intValue()));
        return vo;
    }

    public SalesOrderBusinessSelectOptionVO businessSelectGetById(SalesOrderBusinessSelectQueryDTO dto) {
        if (dto.getId() == null) {
            return null;
        }
        AdminParamValidator.requireCorpid(dto);
        SalesOrder salesOrder = salesOrderRepository.findById(dto.getCorpid(), dto.getId());
        return salesOrder == null || !ApprovalStatusEnum.allowsDownstream(salesOrder.getAuditStatus())
            ? null : toBusinessSelectOption(salesOrder);
    }

    public SalesOrderItemStockVO queryItemStock(SalesOrderItemStockQueryDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        if (dto.getSkuId() == null) {
            throw new BizException("产品不能为空");
        }
        SalesOrderItemStockVO vo = new SalesOrderItemStockVO();
        vo.setStockQty(inventoryStockQueryApi.queryInstantQty(dto.getCorpid(), dto.getSkuId(), dto.getWarehouseId()));
        return vo;
    }

    private List<SalesOrderBusinessSelectOptionVO> findBusinessSelectOptions(SalesOrderBusinessSelectQueryDTO dto,
                                                                               Integer offset, Integer pageSize) {
        AdminParamValidator.requireCorpid(dto);
        return salesOrderRepository.findByCondition(businessSelectConditions(dto, offset, pageSize)).stream()
            .map(this::toBusinessSelectOption)
            .toList();
    }

    private static Map<String, Object> businessSelectConditions(SalesOrderBusinessSelectQueryDTO dto,
                                                                  Integer offset, Integer pageSize) {
        Map<String, Object> conditions = new java.util.HashMap<>();
        conditions.put("corpid", dto.getCorpid());
        conditions.put("customerId", dto.getCustomerId());
        conditions.put("businessSelectAuditStatuses", List.of(ApprovalStatusEnum.APPROVED.getCode(),
            ApprovalStatusEnum.NO_NEED_APPROVED.getCode()));
        if (dto.getKeyword() != null && !dto.getKeyword().trim().isEmpty()) {
            conditions.put("businessSelectKeyword", dto.getKeyword().trim());
        }
        if (offset != null && pageSize != null) {
            conditions.put("offset", offset);
            conditions.put("pageSize", pageSize);
        }
        return conditions;
    }

    private SalesOrderBusinessSelectOptionVO toBusinessSelectOption(SalesOrder salesOrder) {
        SalesOrderBusinessSelectOptionVO option = new SalesOrderBusinessSelectOptionVO();
        option.setId(salesOrder.getId());
        option.setCode(salesOrder.getOrderNo());
        option.setName(salesOrder.getOrderNo());
        option.setLabel(salesOrder.getOrderNo());
        return option;
    }

    private static Map<String, Object> linkageConfig() {
        return Map.of(
            "warehouseSync", Map.of("headerAttr", "main.warehouseId", "tableAttr", "items", "itemAttr", "warehouseId", "message", "是否将快捷选择仓库同步到所有产品行？"),
            "itemStock", Map.of("tableAttr", "items", "skuAttr", "skuId", "warehouseAttr", "warehouseId", "stockAttr", "stockQty"),
            "rowAmount", Map.of("tableAttr", "items", "quantityAttr", "qty", "unitPriceAttr", "unitPrice", "amountAttr", "amount"),
            "aggregateAmount", Map.of("tableAttr", "items", "amountAttr", "amount", "targetAttr", "main.totalAmount")
        );
    }

    private xbb.ai.erp.module.sales.admin.vo.SalesOrderSaveItemVO toSaveItemVO(String corpid, SalesOrder salesOrder) {
        if (salesOrder == null || salesOrder.getId() == null) {
            return SalesOrderAdminAssembler.toSaveItemVO(salesOrder);
        }
        xbb.ai.erp.module.sales.admin.vo.SalesOrderSaveItemVO vo = SalesOrderAdminAssembler.toSaveItemVO(
            salesOrder,
            salesOrderItemRepository.findByCondition(Map.of("corpid", corpid, "salesOrderId", salesOrder.getId()))
        );
        vo.getItems().forEach(item -> item.setStockQty(
            inventoryStockQueryApi.queryInstantQty(corpid, item.getSkuId(), item.getWarehouseId())));
        return vo;
    }
}
