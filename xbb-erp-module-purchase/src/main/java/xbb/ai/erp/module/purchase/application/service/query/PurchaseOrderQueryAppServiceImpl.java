package xbb.ai.erp.module.purchase.application.service.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import xbb.ai.erp.module.common.application.render.ListValueRenderer;
import xbb.ai.erp.module.common.application.util.ListQueryMapUtil;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundListItemVO;
import xbb.ai.erp.module.purchase.application.assembler.PurchaseInboundAdminAssembler;
import xbb.ai.erp.scene.meta.SceneFieldAssembler;
import xbb.ai.erp.scene.meta.SceneTypeEnum;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderListItemVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderBusinessSelectQueryDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderBusinessSelectOptionVO;
import xbb.ai.erp.module.purchase.application.assembler.PurchaseOrderAdminAssembler;
import xbb.ai.erp.module.purchase.application.field.PurchaseOrderFieldFactory;
import xbb.ai.erp.module.purchase.application.field.PurchaseOrderFormSectionFactory;
import xbb.ai.erp.module.purchase.application.schema.PurchaseOrderListSchemaProvider;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrder;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderItemRepository;

@Service
public class PurchaseOrderQueryAppServiceImpl {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final PurchaseOrderFieldFactory fieldFactory;
    private final PurchaseOrderListSchemaProvider schemaProvider;
    private final ListValueRenderer listValueRenderer;
    private final BizNoGenerator bizNoGenerator;
    private final ListQueryMapUtil listQueryMapUtil = new ListQueryMapUtil();

    public PurchaseOrderQueryAppServiceImpl(PurchaseOrderRepository purchaseOrderRepository, PurchaseOrderItemRepository purchaseOrderItemRepository, PurchaseOrderFieldFactory fieldFactory, PurchaseOrderListSchemaProvider schemaProvider, ListValueRenderer listValueRenderer, BizNoGenerator bizNoGenerator) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.purchaseOrderItemRepository = purchaseOrderItemRepository;
        this.fieldFactory = fieldFactory;
        this.schemaProvider = schemaProvider;
        this.listValueRenderer = listValueRenderer;
        this.bizNoGenerator = bizNoGenerator;
    }

    public ListBaseVO<PurchaseOrderListItemVO> list(ListBaseDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = listQueryMapUtil.gen(dto, schemaProvider.conditionMetaMap());
        List<PurchaseOrder> list = purchaseOrderRepository.findByCondition(conditionMap);
        Long total = purchaseOrderRepository.count(conditionMap);
        ListBaseVO<PurchaseOrderListItemVO> vo = new ListBaseVO<>();
        List<PurchaseOrderListItemVO> items = list.stream().map(PurchaseOrderAdminAssembler::toListItemVO).toList();
        vo.setList(listValueRenderer.render(dto.getCorpid(), BusinessCodeEnum.PURCHASE_ORDER.getCode(), items));
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    public SaveItemVO<xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderSaveItemVO> addItem(BaseDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        SaveItemVO<xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(SceneFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.CREATE)));
        vo.setFormSections(PurchaseOrderFormSectionFactory.getSections(SceneTypeEnum.CREATE));
        vo.setLinkageConfig(linkageConfig());
        xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderSaveItemVO data = PurchaseOrderAdminAssembler.buildEmptySaveItemVO();
        xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderMainDTO main = new xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderMainDTO();
        main.setOrderNo(bizNoGenerator.next(dto.getCorpid(), BusinessCodeEnum.PURCHASE_ORDER.getCode()));
        data.setMain(main);
        vo.setData(data);
        return vo;
    }

    public SaveItemVO<xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderSaveItemVO> updateItem(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        PurchaseOrder entity = purchaseOrderRepository.findById(dto.getCorpid(), dto.getId());
        SaveItemVO<xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(SceneFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.UPDATE)));
        vo.setFormSections(PurchaseOrderFormSectionFactory.getSections(SceneTypeEnum.UPDATE));
        vo.setLinkageConfig(linkageConfig());
        vo.setData(toSaveItemVO(dto.getCorpid(), entity));
        return vo;
    }

    public PurchaseOrderDetailVO detail(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        PurchaseOrder entity = purchaseOrderRepository.findById(dto.getCorpid(), dto.getId());
        return PurchaseOrderAdminAssembler.toDetailVO(toSaveItemVO(dto.getCorpid(), entity));
    }

    public List<PurchaseOrderBusinessSelectOptionVO> businessSelectQuickSearch(PurchaseOrderBusinessSelectQueryDTO dto) {
        return findBusinessSelectOptions(dto, 0, 5);
    }

    public ListBaseVO<PurchaseOrderBusinessSelectOptionVO> businessSelectDialogSearch(PurchaseOrderBusinessSelectQueryDTO dto) {
        int pageNum = dto.getPageNum() == null || dto.getPageNum() < 1 ? 1 : dto.getPageNum();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? 20 : dto.getPageSize();
        Map<String, Object> conditions = businessSelectConditions(dto, (pageNum - 1) * pageSize, pageSize);
        List<PurchaseOrderBusinessSelectOptionVO> options = purchaseOrderRepository.findByCondition(conditions).stream()
            .map(this::toBusinessSelectOption).toList();
        Long total = purchaseOrderRepository.count(businessSelectConditions(dto, null, null));
        ListBaseVO<PurchaseOrderBusinessSelectOptionVO> vo = new ListBaseVO<>();
        vo.setList(options);
        vo.setPageHelper(new ListBaseVO.PageHelper(pageNum, total == null ? 0 : total.intValue()));
        return vo;
    }

    public PurchaseOrderBusinessSelectOptionVO businessSelectGetById(PurchaseOrderBusinessSelectQueryDTO dto) {
        if (dto.getId() == null) return null;
        AdminParamValidator.requireCorpid(dto);
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(dto.getCorpid(), dto.getId());
        return purchaseOrder == null || !AuditStatusEnum.allowsDownstream(purchaseOrder.getAuditStatus())
                ? null : toBusinessSelectOption(purchaseOrder);
    }

    private List<PurchaseOrderBusinessSelectOptionVO> findBusinessSelectOptions(PurchaseOrderBusinessSelectQueryDTO dto,
                                                                                  Integer offset, Integer pageSize) {
        AdminParamValidator.requireCorpid(dto);
        return purchaseOrderRepository.findByCondition(businessSelectConditions(dto, offset, pageSize)).stream()
                .map(this::toBusinessSelectOption)
                .toList();
    }

    private static Map<String, Object> businessSelectConditions(PurchaseOrderBusinessSelectQueryDTO dto,
                                                                  Integer offset, Integer pageSize) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("corpid", dto.getCorpid());
        conditions.put("supplierId", dto.getSupplierId());
        conditions.put("businessSelectPendingInbound", Boolean.TRUE);
        conditions.put("businessSelectAuditStatuses", List.of(AuditStatusEnum.APPROVED.getCode(),
            AuditStatusEnum.NO_NEED_APPROVED.getCode()));
        if (dto.getKeyword() != null && !dto.getKeyword().trim().isEmpty()) {
            conditions.put("businessSelectKeyword", dto.getKeyword().trim());
        }
        if (offset != null && pageSize != null) {
            conditions.put("offset", offset);
            conditions.put("pageSize", pageSize);
        }
        return conditions;
    }

    private PurchaseOrderBusinessSelectOptionVO toBusinessSelectOption(PurchaseOrder purchaseOrder) {
        PurchaseOrderBusinessSelectOptionVO option = new PurchaseOrderBusinessSelectOptionVO();
        option.setId(purchaseOrder.getId());
        option.setCode(purchaseOrder.getOrderNo());
        option.setName(purchaseOrder.getSupplierName());
        option.setLabel(purchaseOrder.getOrderNo() == null || purchaseOrder.getOrderNo().isBlank()
                ? purchaseOrder.getSupplierName()
                : purchaseOrder.getSupplierName() == null || purchaseOrder.getSupplierName().isBlank()
                        ? purchaseOrder.getOrderNo()
                        : purchaseOrder.getOrderNo() + " " + purchaseOrder.getSupplierName());
        return option;
    }

    private static Map<String, Object> linkageConfig() {
        return Map.of(
            "rowAmount", Map.of(
                "tableAttr", "items",
                "quantityAttr", "qty",
                "unitPriceAttr", "unitPrice",
                "amountAttr", "amount"
            ),
            "aggregateAmount", Map.of(
                "tableAttr", "items",
                "amountAttr", "amount",
                "targetAttr", "main.totalAmount"
            ),
            "itemStock", Map.of(
                "tableAttr", "items",
                "skuAttr", "skuId",
                "warehouseAttr", "warehouseId",
                "stockAttr", "currentStock"
            )
        );
    }

    private xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderSaveItemVO toSaveItemVO(String corpid, PurchaseOrder entity) {
        xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderSaveItemVO vo = PurchaseOrderAdminAssembler.toSaveItemVO(entity);
        if (entity != null && entity.getId() != null) {
            vo.setItems(PurchaseOrderAdminAssembler.toPurchaseOrderItemDTOs(
                    purchaseOrderItemRepository.findByCondition(Map.of("corpid", corpid, "purchaseOrderId", entity.getId()))));
        }
        return vo;
    }
}
