package xbb.ai.erp.module.sales.application.service.query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceDetailVO;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceBusinessSelectQueryDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceSourceQueryDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceLineDTO;
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceBusinessSelectOptionVO;
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceListItemVO;
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceSourceDocumentVO;
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceSourcePreviewVO;
import xbb.ai.erp.module.sales.application.assembler.SalesInvoiceAdminAssembler;
import xbb.ai.erp.module.sales.application.field.SalesInvoiceFieldFactory;
import xbb.ai.erp.module.sales.application.field.SalesInvoiceFormSectionFactory;
import xbb.ai.erp.module.sales.application.schema.SalesInvoiceListSchemaProvider;
import xbb.ai.erp.module.sales.domain.model.SalesInvoice;
import xbb.ai.erp.module.sales.domain.repository.SalesInvoiceRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesInvoiceLineRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesInvoiceLineSourceRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesOrderRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesOrderItemRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesOutboundRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesOutboundItemRepository;
import xbb.ai.erp.module.sales.domain.model.SalesOrder;
import xbb.ai.erp.module.sales.domain.model.SalesOrderItem;
import xbb.ai.erp.module.sales.domain.model.SalesOutbound;
import xbb.ai.erp.module.sales.domain.model.SalesOutboundItem;
import xbb.ai.erp.module.sales.domain.model.SalesInvoiceLine;
import xbb.ai.erp.module.sales.domain.model.SalesInvoiceLineSource;
import xbb.ai.erp.base.common.enums.AuditStatusEnum;
import xbb.ai.erp.module.sales.admin.SalesInvoiceSourceTypeEnum;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceMainDTO;

@Service
public class SalesInvoiceQueryAppServiceImpl {
    private final SalesInvoiceRepository salesInvoiceRepository;
    private final SalesInvoiceLineRepository salesInvoiceLineRepository;
    private final SalesInvoiceLineSourceRepository salesInvoiceLineSourceRepository;
    private final SalesInvoiceFieldFactory fieldFactory;
    private final SalesInvoiceListSchemaProvider schemaProvider;
    private final ListValueRenderer listValueRenderer;
    private final BizNoGenerator bizNoGenerator;
    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final SalesOutboundRepository salesOutboundRepository;
    private final SalesOutboundItemRepository salesOutboundItemRepository;
    private final ListQueryMapUtil listQueryMapUtil = new ListQueryMapUtil();

    @Autowired
    public SalesInvoiceQueryAppServiceImpl(SalesInvoiceRepository salesInvoiceRepository,
                                           SalesInvoiceLineRepository salesInvoiceLineRepository,
                                           SalesInvoiceLineSourceRepository salesInvoiceLineSourceRepository,
                                           SalesInvoiceFieldFactory fieldFactory,
                                           SalesInvoiceListSchemaProvider schemaProvider,
                                           ListValueRenderer listValueRenderer,
                                           BizNoGenerator bizNoGenerator,
                                           SalesOrderRepository salesOrderRepository,
                                           SalesOrderItemRepository salesOrderItemRepository,
                                           SalesOutboundRepository salesOutboundRepository,
                                           SalesOutboundItemRepository salesOutboundItemRepository) {
        this.salesInvoiceRepository = salesInvoiceRepository;
        this.salesInvoiceLineRepository = salesInvoiceLineRepository;
        this.salesInvoiceLineSourceRepository = salesInvoiceLineSourceRepository;
        this.fieldFactory = fieldFactory;
        this.schemaProvider = schemaProvider;
        this.listValueRenderer = listValueRenderer;
        this.bizNoGenerator = bizNoGenerator;
        this.salesOrderRepository = salesOrderRepository;
        this.salesOrderItemRepository = salesOrderItemRepository;
        this.salesOutboundRepository = salesOutboundRepository;
        this.salesOutboundItemRepository = salesOutboundItemRepository;
    }

    public SalesInvoiceQueryAppServiceImpl(SalesInvoiceRepository salesInvoiceRepository,
                                           SalesInvoiceLineRepository salesInvoiceLineRepository,
                                           SalesInvoiceFieldFactory fieldFactory,
                                           SalesInvoiceListSchemaProvider schemaProvider,
                                           ListValueRenderer listValueRenderer,
                                           BizNoGenerator bizNoGenerator) {
        this(salesInvoiceRepository, salesInvoiceLineRepository, null, fieldFactory, schemaProvider,
            listValueRenderer, bizNoGenerator, null, null, null, null);
    }

    public ListBaseVO<SalesInvoiceListItemVO> list(ListBaseDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = listQueryMapUtil.gen(dto, schemaProvider.conditionMetaMap());
        List<SalesInvoice> list = salesInvoiceRepository.findByCondition(conditionMap);
        Long total = salesInvoiceRepository.count(conditionMap);
        ListBaseVO<SalesInvoiceListItemVO> vo = new ListBaseVO<>();
        List<SalesInvoiceListItemVO> items = list.stream().map(SalesInvoiceAdminAssembler::toListItemVO).toList();
        vo.setList(listValueRenderer.render(dto.getCorpid(), BusinessCodeEnum.SALES_INVOICE.getCode(), items));
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    public SaveItemVO<xbb.ai.erp.module.sales.admin.vo.SalesInvoiceSaveItemVO> addItem(BaseDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        SaveItemVO<xbb.ai.erp.module.sales.admin.vo.SalesInvoiceSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(SceneFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.CREATE)));
        vo.setFormSections(SalesInvoiceFormSectionFactory.getSections(SceneTypeEnum.CREATE));
        vo.setLinkageConfig(invoiceAmountLinkageConfig());
        xbb.ai.erp.module.sales.admin.vo.SalesInvoiceSaveItemVO data = SalesInvoiceAdminAssembler.buildEmptySaveItemVO();
        SalesInvoiceMainDTO main = new SalesInvoiceMainDTO();
        main.setInvoiceNo(bizNoGenerator.next(dto.getCorpid(), BusinessCodeEnum.SALES_INVOICE.getCode()));
        main.setInvoiceType("SALE");
        data.setMain(main);
        data.setLines(List.of());
        vo.setData(data);
        return vo;
    }

    public SaveItemVO<xbb.ai.erp.module.sales.admin.vo.SalesInvoiceSaveItemVO> updateItem(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        SalesInvoice entity = salesInvoiceRepository.findById(dto.getCorpid(), dto.getId());
        SaveItemVO<xbb.ai.erp.module.sales.admin.vo.SalesInvoiceSaveItemVO> vo = new SaveItemVO<>();
        vo.setHeadList(SceneFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.UPDATE)));
        vo.setFormSections(SalesInvoiceFormSectionFactory.getSections(SceneTypeEnum.UPDATE));
        vo.setLinkageConfig(invoiceAmountLinkageConfig());
        if (entity == null) {
            throw new xbb.ai.erp.base.common.exception.BizException("销售发票不存在");
        }
        vo.setData(toSaveItemVO(dto.getCorpid(), entity, dto.getId()));
        return vo;
    }

    public SalesInvoiceDetailVO detail(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        SalesInvoice entity = salesInvoiceRepository.findById(dto.getCorpid(), dto.getId());
        return SalesInvoiceAdminAssembler.toDetailVO(toSaveItemVO(dto.getCorpid(), entity, dto.getId()));
    }

    public List<SalesInvoiceBusinessSelectOptionVO> businessSelectQuickSearch(
        SalesInvoiceBusinessSelectQueryDTO dto) {
        return findBusinessSelectOptions(dto, 0, 5);
    }

    public ListBaseVO<SalesInvoiceBusinessSelectOptionVO> businessSelectDialogSearch(
        SalesInvoiceBusinessSelectQueryDTO dto) {
        int pageNum = dto.getPageNum() == null || dto.getPageNum() < 1 ? 1 : dto.getPageNum();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? 20 : dto.getPageSize();
        Map<String, Object> conditions = businessSelectConditions(dto, (pageNum - 1) * pageSize, pageSize);
        List<SalesInvoiceBusinessSelectOptionVO> options = salesInvoiceRepository.findByCondition(conditions).stream()
            .map(this::toBusinessSelectOption)
            .toList();
        Long total = salesInvoiceRepository.count(businessSelectConditions(dto, null, null));
        ListBaseVO<SalesInvoiceBusinessSelectOptionVO> vo = new ListBaseVO<>();
        vo.setList(options);
        vo.setPageHelper(new ListBaseVO.PageHelper(pageNum, total == null ? 0 : total.intValue()));
        return vo;
    }

    public SalesInvoiceBusinessSelectOptionVO businessSelectGetById(SalesInvoiceBusinessSelectQueryDTO dto) {
        if (dto.getId() == null) {
            return null;
        }
        AdminParamValidator.requireCorpid(dto);
        SalesInvoice salesInvoice = salesInvoiceRepository.findById(dto.getCorpid(), dto.getId());
        return salesInvoice == null || !AuditStatusEnum.allowsDownstream(salesInvoice.getAuditStatus())
            ? null : toBusinessSelectOption(salesInvoice);
    }

    public List<SalesInvoiceSourceDocumentVO> sourceDocuments(SalesInvoiceSourceQueryDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        if (!SalesInvoiceSourceTypeEnum.isDocumentSource(dto.getSourceType())) {
            throw new xbb.ai.erp.base.common.exception.BizException("不支持的发票来源类型");
        }
        return SalesInvoiceSourceTypeEnum.SALES_OUTBOUND.name().equals(dto.getSourceType())
            ? salesOutboundRepository.findByCondition(Map.of("corpid", dto.getCorpid())).stream()
                .filter(item -> AuditStatusEnum.allowsDownstream(item.getAuditStatus()))
                .filter(item -> dto.getCustomerId() == null || dto.getCustomerId().equals(item.getCustomerId()))
                .map(item -> sourceDocument(item.getId(), item.getOutboundNo(), item.getCustomerId(), dto.getSourceType())).toList()
            : salesOrderRepository.findByCondition(Map.of("corpid", dto.getCorpid())).stream()
                .filter(item -> AuditStatusEnum.allowsDownstream(item.getAuditStatus()))
                .filter(item -> dto.getCustomerId() == null || dto.getCustomerId().equals(item.getCustomerId()))
                .map(item -> sourceDocument(item.getId(), item.getOrderNo(), item.getCustomerId(), SalesInvoiceSourceTypeEnum.SALES_ORDER.name())).toList();
    }

    public SalesInvoiceSourcePreviewVO sourcePreview(SalesInvoiceSourceQueryDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        if (dto.getSourceId() == null) {
            throw new xbb.ai.erp.base.common.exception.BizException("请选择来源单据");
        }
        SalesInvoiceSourcePreviewVO result = new SalesInvoiceSourcePreviewVO();
        result.setSourceType(dto.getSourceType());
        result.setSourceId(dto.getSourceId());
        if (SalesInvoiceSourceTypeEnum.SALES_OUTBOUND.name().equals(dto.getSourceType())) {
            SalesOutbound outbound = salesOutboundRepository.findById(dto.getCorpid(), dto.getSourceId());
            if (outbound == null || !AuditStatusEnum.allowsDownstream(outbound.getAuditStatus())) {
                throw new xbb.ai.erp.base.common.exception.BizException("销售出库单不存在或未审核");
            }
            result.setCustomerId(outbound.getCustomerId());
            result.setLines(salesOutboundItemRepository
                .findByCondition(Map.of("corpid", dto.getCorpid(), "salesOutboundId", dto.getSourceId()))
                .stream()
                .map(item -> outboundLine(item, dto.getCorpid(), dto.getSourceId()))
                .filter(item -> item.getQuantity() != null && item.getQuantity().signum() > 0)
                .toList());
        } else if (SalesInvoiceSourceTypeEnum.SALES_ORDER.name().equals(dto.getSourceType())) {
            SalesOrder order = salesOrderRepository.findById(dto.getCorpid(), dto.getSourceId());
            if (order == null || !AuditStatusEnum.allowsDownstream(order.getAuditStatus())) {
                throw new xbb.ai.erp.base.common.exception.BizException("销售订单不存在或未审核");
            }
            result.setCustomerId(order.getCustomerId());
            result.setLines(salesOrderItemRepository
                .findByCondition(Map.of("corpid", dto.getCorpid(), "salesOrderId", dto.getSourceId()))
                .stream()
                .map(item -> orderLine(item, dto.getCorpid(), dto.getSourceId()))
                .filter(item -> item.getQuantity() != null && item.getQuantity().signum() > 0)
                .toList());
        } else {
            throw new xbb.ai.erp.base.common.exception.BizException("不支持的发票来源类型");
        }
        return result;
    }

    private SalesInvoiceSourceDocumentVO sourceDocument(Long id, String documentNo, Long customerId,
                                                        String sourceType) {
        SalesInvoiceSourceDocumentVO result = new SalesInvoiceSourceDocumentVO();
        result.setId(id);
        result.setDocumentNo(documentNo);
        result.setCustomerId(customerId);
        result.setSourceType(sourceType);
        return result;
    }

    private SalesInvoiceLineDTO outboundLine(SalesOutboundItem item, String corpid, Long sourceId) {
        SalesInvoiceLineDTO result = new SalesInvoiceLineDTO();
        result.setProductId(item.getSkuId());
        result.setProductName(item.getSkuName());
        BigDecimal availableQuantity = item.getQty().subtract(salesInvoiceLineSourceRepository
            .sumPostedQuantity(corpid, SalesInvoiceSourceTypeEnum.SALES_OUTBOUND.name(), item.getId(), null));
        if (item.getSalesOrderItemId() != null) {
            SalesOrderItem orderItem = salesOrderItemRepository.findById(corpid, item.getSalesOrderItemId());
            if (orderItem != null) {
                BigDecimal orderAvailableQuantity = orderItem.getQty().subtract(salesInvoiceLineSourceRepository
                    .sumPostedQuantityBySalesOrderItem(corpid, orderItem.getId(), null));
                availableQuantity = availableQuantity.min(orderAvailableQuantity);
            }
        }
        result.setQuantity(availableQuantity.max(BigDecimal.ZERO));
        result.setUnitPrice(item.getUnitPrice());
        result.setTaxRate(java.math.BigDecimal.ZERO);
        result.setSourceType(SalesInvoiceSourceTypeEnum.SALES_OUTBOUND.name());
        result.setSourceId(sourceId);
        result.setSourceLineId(item.getId());
        return result;
    }

    private SalesInvoiceLineDTO orderLine(SalesOrderItem item, String corpid, Long sourceId) {
        SalesInvoiceLineDTO result = new SalesInvoiceLineDTO();
        result.setProductId(item.getSkuId());
        result.setProductName(item.getSkuName());
        result.setSpecification(item.getSpecification());
        BigDecimal availableQuantity = item.getQty().subtract(salesInvoiceLineSourceRepository
            .sumPostedQuantityBySalesOrderItem(corpid, item.getId(), null));
        result.setQuantity(availableQuantity.max(BigDecimal.ZERO));
        result.setUnitPrice(item.getUnitPrice());
        result.setTaxRate(item.getTaxRate());
        result.setSourceType(SalesInvoiceSourceTypeEnum.SALES_ORDER.name());
        result.setSourceId(sourceId);
        result.setSourceLineId(item.getId());
        return result;
    }

    private xbb.ai.erp.module.sales.admin.vo.SalesInvoiceSaveItemVO toSaveItemVO(String corpid,
                                                                                   SalesInvoice invoice,
                                                                                   Long invoiceId) {
        List<SalesInvoiceLine> lines = salesInvoiceLineRepository.findByCondition(
            Map.of("corpid", corpid, "salesInvoiceId", invoiceId));
        List<SalesInvoiceLineSource> sources = salesInvoiceLineSourceRepository
            .findByInvoiceLineIds(corpid, lines.stream().map(SalesInvoiceLine::getId).toList());
        return SalesInvoiceAdminAssembler.toSaveItemVO(invoice, lines, sources);
    }

    private static Map<String, Object> invoiceAmountLinkageConfig() {
        return Map.of(
            "invoiceAmount", Map.of(
                "tableAttr", "lines",
                "quantityAttr", "quantity",
                "unitPriceAttr", "unitPrice",
                "taxRateAttr", "taxRate",
                "untaxedAmountAttr", "untaxedAmount",
                "taxAmountAttr", "taxAmount",
                "amountAttr", "amount",
                "untaxedTargetAttr", "main.untaxedAmount",
                "taxTargetAttr", "main.taxAmount",
                "amountTargetAttr", "main.amount")
        );
    }

    private List<SalesInvoiceBusinessSelectOptionVO> findBusinessSelectOptions(SalesInvoiceBusinessSelectQueryDTO dto,
                                                                                 Integer offset, Integer pageSize) {
        AdminParamValidator.requireCorpid(dto);
        return salesInvoiceRepository.findByCondition(businessSelectConditions(dto, offset, pageSize)).stream()
            .map(this::toBusinessSelectOption)
            .toList();
    }

    private static Map<String, Object> businessSelectConditions(SalesInvoiceBusinessSelectQueryDTO dto,
                                                                  Integer offset, Integer pageSize) {
        Map<String, Object> conditions = new java.util.HashMap<>();
        conditions.put("corpid", dto.getCorpid());
        conditions.put("customerId", dto.getCustomerId());
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

    private SalesInvoiceBusinessSelectOptionVO toBusinessSelectOption(SalesInvoice salesInvoice) {
        SalesInvoiceBusinessSelectOptionVO option = new SalesInvoiceBusinessSelectOptionVO();
        option.setId(salesInvoice.getId());
        option.setCode(salesInvoice.getInvoiceNo());
        option.setName(salesInvoice.getInvoiceNo());
        option.setLabel(salesInvoice.getInvoiceNo());
        return option;
    }
}
