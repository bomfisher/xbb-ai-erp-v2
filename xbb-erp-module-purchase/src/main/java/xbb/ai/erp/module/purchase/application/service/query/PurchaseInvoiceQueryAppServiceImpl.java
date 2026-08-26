package xbb.ai.erp.module.purchase.application.service.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceListDTO;
import xbb.ai.erp.module.purchase.admin.PurchaseInvoiceFieldEnum;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceSaveItemVO;
import xbb.ai.erp.module.purchase.application.assembler.PurchaseInvoiceAdminAssembler;
import xbb.ai.erp.module.common.application.render.ListValueRenderer;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInvoice;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInvoiceRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInboundRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInvoiceLineRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInvoiceLineSourceRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderItemRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInboundItemRepository;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrderItem;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInboundItem;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInvoiceLine;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInvoiceLineSource;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceLineDTO;
import xbb.ai.erp.module.purchase.admin.PurchaseInvoiceSourceTypeEnum;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceSourceQueryDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceSourceDocumentVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceSourcePreviewVO;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrder;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInbound;
import xbb.ai.erp.scene.meta.SceneFieldAssembler;
import xbb.ai.erp.scene.meta.SceneTypeEnum;
import xbb.ai.erp.module.purchase.application.field.PurchaseInvoiceFormSectionFactory;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceBusinessSelectQueryDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceBusinessSelectOptionVO;
import xbb.ai.erp.base.common.enums.AuditStatusEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PurchaseInvoiceQueryAppServiceImpl {

    private final PurchaseInvoiceRepository purchaseInvoiceRepository;
    private final ListValueRenderer listValueRenderer;
    private final BizNoGenerator bizNoGenerator;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseInboundRepository purchaseInboundRepository;
    private final PurchaseInvoiceLineRepository lineRepository;
    private final PurchaseInvoiceLineSourceRepository sourceRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final PurchaseInboundItemRepository purchaseInboundItemRepository;

    @Autowired
    public PurchaseInvoiceQueryAppServiceImpl(PurchaseInvoiceRepository purchaseInvoiceRepository,
        ListValueRenderer listValueRenderer, BizNoGenerator bizNoGenerator,
        PurchaseOrderRepository purchaseOrderRepository, PurchaseInboundRepository purchaseInboundRepository,
        PurchaseInvoiceLineRepository lineRepository, PurchaseInvoiceLineSourceRepository sourceRepository,
        PurchaseOrderItemRepository purchaseOrderItemRepository, PurchaseInboundItemRepository purchaseInboundItemRepository) {
        this.purchaseInvoiceRepository = purchaseInvoiceRepository;
        this.listValueRenderer = listValueRenderer;
        this.bizNoGenerator = bizNoGenerator;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.purchaseInboundRepository = purchaseInboundRepository;
        this.lineRepository = lineRepository;
        this.sourceRepository = sourceRepository;
        this.purchaseOrderItemRepository = purchaseOrderItemRepository;
        this.purchaseInboundItemRepository = purchaseInboundItemRepository;
    }

    public ListBaseVO<PurchaseInvoiceListItemVO> list(PurchaseInvoiceListDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("corpid", dto.getCorpid());
        conditionMap.put("id", dto.getId());
        conditionMap.put("corpid", dto.getCorpid());
        conditionMap.put("invoiceNo", dto.getInvoiceNo());
        conditionMap.put("supplierInvoiceNo", dto.getSupplierInvoiceNo());
        conditionMap.put("supplierId", dto.getSupplierId());
        conditionMap.put("invoiceDate", dto.getInvoiceDate());
        conditionMap.put("dueDate", dto.getDueDate());
        conditionMap.put("paymentTerm", dto.getPaymentTerm());
        conditionMap.put("untaxedAmount", dto.getUntaxedAmount());
        conditionMap.put("taxAmount", dto.getTaxAmount());
        conditionMap.put("amount", dto.getAmount());
        conditionMap.put("invoiceType", dto.getInvoiceType());
        conditionMap.put("status", dto.getStatus());
        conditionMap.put("auditStatus", dto.getAuditStatus());
        conditionMap.put("originalInvoiceId", dto.getOriginalInvoiceId());
        conditionMap.put("pageNum", dto.getPageNum());
        conditionMap.put("pageSize", dto.getPageSize());
        conditionMap.put("offset", dto.getOffset());
        conditionMap.put("conditions", dto.getConditions());
        List<PurchaseInvoice> list = purchaseInvoiceRepository.findByCondition(conditionMap);
        Long total = purchaseInvoiceRepository.count(conditionMap);
        ListBaseVO<PurchaseInvoiceListItemVO> vo = new ListBaseVO<>();
        List<PurchaseInvoiceListItemVO> items = list.stream().map(PurchaseInvoiceAdminAssembler::toListItemVO).toList();
        vo.setList(listValueRenderer.render(dto.getCorpid(), BusinessCodeEnum.PURCHASE_INVOICE.getCode(), items));
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum() == null ? 1 : dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    public List<PurchaseInvoiceBusinessSelectOptionVO> businessSelectQuickSearch(
        PurchaseInvoiceBusinessSelectQueryDTO dto) {
        return findBusinessSelectOptions(dto, 0, 5);
    }

    public ListBaseVO<PurchaseInvoiceBusinessSelectOptionVO> businessSelectDialogSearch(
        PurchaseInvoiceBusinessSelectQueryDTO dto) {
        int pageNum = dto.getPageNum() == null || dto.getPageNum() < 1 ? 1 : dto.getPageNum();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? 20 : dto.getPageSize();
        Map<String, Object> conditions = businessSelectConditions(dto, (pageNum - 1) * pageSize, pageSize);
        List<PurchaseInvoiceBusinessSelectOptionVO> options = purchaseInvoiceRepository.findByCondition(conditions)
            .stream().map(this::toBusinessSelectOption).toList();
        Long total = purchaseInvoiceRepository.count(businessSelectConditions(dto, null, null));
        ListBaseVO<PurchaseInvoiceBusinessSelectOptionVO> vo = new ListBaseVO<>();
        vo.setList(options);
        vo.setPageHelper(new ListBaseVO.PageHelper(pageNum, total == null ? 0 : total.intValue()));
        return vo;
    }

    public PurchaseInvoiceBusinessSelectOptionVO businessSelectGetById(
        PurchaseInvoiceBusinessSelectQueryDTO dto) {
        if (dto.getId() == null) {
            return null;
        }
        AdminParamValidator.requireCorpid(dto);
        PurchaseInvoice invoice = purchaseInvoiceRepository.findById(dto.getCorpid(), dto.getId());
        return invoice == null || !"POSTED".equals(invoice.getStatus())
            || !AuditStatusEnum.allowsDownstream(invoice.getAuditStatus())
            ? null : toBusinessSelectOption(invoice);
    }

    public SaveItemVO<PurchaseInvoiceSaveItemVO> addItem(BaseDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        SaveItemVO<PurchaseInvoiceSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(java.util.Arrays.stream(PurchaseInvoiceFieldEnum.values())
            .filter(field -> field.supports(SceneTypeEnum.CREATE))
            .map(field -> SceneFieldAssembler.build(field.toSceneFieldMeta()))
            .toList());
        vo.setFormSections(PurchaseInvoiceFormSectionFactory.getSections(SceneTypeEnum.CREATE));
        vo.setLinkageConfig(invoiceAmountLinkageConfig());
        PurchaseInvoiceSaveItemVO data = PurchaseInvoiceAdminAssembler.buildEmptySaveItemVO();
        data.setMain(new xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceMainDTO());
        data.getMain().setInvoiceNo(bizNoGenerator.next(dto.getCorpid(), BusinessCodeEnum.PURCHASE_INVOICE.getCode()));
        data.getMain().setInvoiceType("PURCHASE");
        data.setSourceSelection(new xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceSourceSelectionDTO());
        data.setLines(List.of());
        vo.setData(data);
        return vo;
    }

    public SaveItemVO<PurchaseInvoiceSaveItemVO> updateItem(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        PurchaseInvoice entity = purchaseInvoiceRepository.findById(dto.getCorpid(), dto.getId());
        SaveItemVO<PurchaseInvoiceSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(java.util.Arrays.stream(PurchaseInvoiceFieldEnum.values())
            .filter(field -> field.supports(SceneTypeEnum.UPDATE))
            .map(field -> SceneFieldAssembler.build(field.toSceneFieldMeta()))
            .toList());
        vo.setFormSections(PurchaseInvoiceFormSectionFactory.getSections(SceneTypeEnum.UPDATE));
        vo.setLinkageConfig(invoiceAmountLinkageConfig());
        List<PurchaseInvoiceLine> lines = lineRepository.findByInvoiceId(dto.getCorpid(), dto.getId());
        List<PurchaseInvoiceLineSource> sources = sourceRepository.findByInvoiceLineIds(dto.getCorpid(),
            lines.stream().map(PurchaseInvoiceLine::getId).toList());
        vo.setData(PurchaseInvoiceAdminAssembler.toSaveItemVO(entity, lines, sources));
        return vo;
    }

    public PurchaseInvoiceDetailVO detail(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        PurchaseInvoice entity = purchaseInvoiceRepository.findById(dto.getCorpid(), dto.getId());
        List<PurchaseInvoiceLine> lines = lineRepository.findByInvoiceId(dto.getCorpid(), dto.getId());
        List<PurchaseInvoiceLineSource> sources = sourceRepository.findByInvoiceLineIds(dto.getCorpid(),
            lines.stream().map(PurchaseInvoiceLine::getId).toList());
        return PurchaseInvoiceAdminAssembler.toDetailVO(PurchaseInvoiceAdminAssembler.toSaveItemVO(entity, lines, sources));
    }

    public List<PurchaseInvoiceSourceDocumentVO> sourceDocuments(PurchaseInvoiceSourceQueryDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        if (!PurchaseInvoiceSourceTypeEnum.isSupported(dto.getSourceType())
            || PurchaseInvoiceSourceTypeEnum.MANUAL.name().equals(dto.getSourceType())) {
            throw new xbb.ai.erp.base.common.exception.BizException("不支持的采购发票来源类型");
        }
        String keyword = dto.getKeyword() == null ? "" : dto.getKeyword().trim();
        if (PurchaseInvoiceSourceTypeEnum.PURCHASE_ORDER.name().equals(dto.getSourceType())) {
            return purchaseOrderRepository.findByCondition(Map.of("corpid", dto.getCorpid())).stream()
                .filter(item -> item.getAuditStatus() != null && item.getAuditStatus() >= 2)
                .filter(item -> dto.getSupplierId() == null || dto.getSupplierId().equals(item.getSupplierId()))
                .filter(item -> keyword.isEmpty() || item.getOrderNo().contains(keyword))
                .map(item -> sourceDocument(item.getId(), item.getOrderNo(), item.getSupplierId(), dto.getSourceType()))
                .toList();
        }
        return purchaseInboundRepository.findByCondition(Map.of("corpid", dto.getCorpid())).stream()
            .filter(item -> item.getAuditStatus() != null && item.getAuditStatus() >= 2)
            .filter(item -> dto.getSupplierId() == null || dto.getSupplierId().equals(item.getSupplierId()))
            .filter(item -> keyword.isEmpty() || item.getInboundNo().contains(keyword))
            .map(item -> sourceDocument(item.getId(), item.getInboundNo(), item.getSupplierId(), dto.getSourceType()))
            .toList();
    }

    public PurchaseInvoiceSourcePreviewVO sourcePreview(PurchaseInvoiceSourceQueryDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        if (dto.getSourceId() == null) {
            throw new xbb.ai.erp.base.common.exception.BizException("请选择来源单据");
        }
        PurchaseInvoiceSourcePreviewVO result = new PurchaseInvoiceSourcePreviewVO();
        result.setSourceType(dto.getSourceType());
        result.setSourceId(dto.getSourceId());
        if (PurchaseInvoiceSourceTypeEnum.PURCHASE_ORDER.name().equals(dto.getSourceType())) {
            PurchaseOrder order = purchaseOrderRepository.findById(dto.getCorpid(), dto.getSourceId());
            if (order == null) {
                throw new xbb.ai.erp.base.common.exception.BizException("采购订单不存在或未审核");
            }
            result.setSupplierId(order.getSupplierId());
            result.setLines(purchaseOrderLines(dto, order.getId()));
        } else if (PurchaseInvoiceSourceTypeEnum.PURCHASE_INBOUND.name().equals(dto.getSourceType())) {
            PurchaseInbound inbound = purchaseInboundRepository.findById(dto.getCorpid(), dto.getSourceId());
            if (inbound == null) {
                throw new xbb.ai.erp.base.common.exception.BizException("采购入库单不存在或未审核");
            }
            result.setSupplierId(inbound.getSupplierId());
            result.setLines(purchaseInboundLines(dto, inbound.getId()));
        } else {
            throw new xbb.ai.erp.base.common.exception.BizException("不支持的采购发票来源类型");
        }
        return result;
    }

    private PurchaseInvoiceSourceDocumentVO sourceDocument(Long id, String documentNo, Long supplierId,
        String sourceType) {
        PurchaseInvoiceSourceDocumentVO result = new PurchaseInvoiceSourceDocumentVO();
        result.setId(id);
        result.setDocumentNo(documentNo);
        result.setSupplierId(supplierId);
        result.setSourceType(sourceType);
        return result;
    }

    private static Map<String, Object> invoiceAmountLinkageConfig() {
        return Map.of("invoiceAmount", Map.of("tableAttr", "lines", "quantityAttr", "quantity",
            "unitPriceAttr", "unitPrice", "taxRateAttr", "taxRate", "untaxedAmountAttr", "untaxedAmount",
            "taxAmountAttr", "taxAmount", "amountAttr", "amount", "untaxedTargetAttr", "main.untaxedAmount",
            "taxTargetAttr", "main.taxAmount", "amountTargetAttr", "main.amount"));
    }

    private List<PurchaseInvoiceLineDTO> purchaseOrderLines(PurchaseInvoiceSourceQueryDTO dto, Long orderId) {
        if (purchaseOrderItemRepository == null) return List.of();
        return purchaseOrderItemRepository.findByCondition(Map.of("corpid", dto.getCorpid(), "purchaseOrderId", orderId))
            .stream().map(item -> toLine(item.getSkuId(), item.getSkuName(), item.getSpecification(), item.getUnitName(),
                item.getQty(), item.getUnitPrice(), item.getTaxRate(), "PURCHASE_ORDER", orderId, item.getId())).toList();
    }

    private List<PurchaseInvoiceLineDTO> purchaseInboundLines(PurchaseInvoiceSourceQueryDTO dto, Long inboundId) {
        if (purchaseInboundItemRepository == null) return List.of();
        return purchaseInboundItemRepository.findByCondition(Map.of("corpid", dto.getCorpid(), "purchaseInboundId", inboundId))
            .stream().map(item -> toLine(item.getSkuId(), item.getSkuName(), null, item.getUnitName(), item.getQty(),
                item.getUnitPrice(), java.math.BigDecimal.ZERO, "PURCHASE_INBOUND", inboundId, item.getId())).toList();
    }

    private PurchaseInvoiceLineDTO toLine(Long productId, String productName, String specification, String unitName,
        java.math.BigDecimal quantity, java.math.BigDecimal unitPrice, java.math.BigDecimal taxRate,
        String sourceType, Long sourceId, Long sourceLineId) {
        PurchaseInvoiceLineDTO line = new PurchaseInvoiceLineDTO();
        line.setProductId(productId); line.setProductName(productName); line.setSpecification(specification);
        line.setUnitName(unitName); line.setQuantity(quantity); line.setUnitPrice(unitPrice); line.setTaxRate(taxRate);
        line.setSourceType(sourceType); line.setSourceId(sourceId); line.setSourceLineId(sourceLineId);
        return line;
    }

    private List<PurchaseInvoiceBusinessSelectOptionVO> findBusinessSelectOptions(
        PurchaseInvoiceBusinessSelectQueryDTO dto, Integer offset, Integer pageSize) {
        AdminParamValidator.requireCorpid(dto);
        return purchaseInvoiceRepository.findByCondition(businessSelectConditions(dto, offset, pageSize)).stream()
            .map(this::toBusinessSelectOption)
            .toList();
    }

    private static Map<String, Object> businessSelectConditions(PurchaseInvoiceBusinessSelectQueryDTO dto,
                                                                  Integer offset, Integer pageSize) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("corpid", dto.getCorpid());
        conditions.put("supplierId", dto.getSupplierId());
        conditions.put("status", "POSTED");
        conditions.put("businessSelectOnlyOpen", Boolean.TRUE);
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

    private PurchaseInvoiceBusinessSelectOptionVO toBusinessSelectOption(PurchaseInvoice invoice) {
        PurchaseInvoiceBusinessSelectOptionVO option = new PurchaseInvoiceBusinessSelectOptionVO();
        option.setId(invoice.getId());
        option.setCode(invoice.getInvoiceNo());
        option.setName(invoice.getInvoiceNo());
        option.setLabel(invoice.getInvoiceNo());
        return option;
    }
}
