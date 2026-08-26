package xbb.ai.erp.module.sales.application.service.save;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceSaveDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceSubmitSaveDTO;
import xbb.ai.erp.module.sales.application.assembler.SalesInvoiceAdminAssembler;
import xbb.ai.erp.module.sales.application.validator.SalesInvoiceValidator;
import xbb.ai.erp.module.sales.application.port.SalesInvoiceDraftRepository;
import xbb.ai.erp.module.sales.application.validator.SalesInvoiceSaveProtocolValidator;
import xbb.ai.erp.module.sales.application.validator.SalesInvoiceSaveCommonValidator;
import xbb.ai.erp.module.sales.application.validator.SalesInvoiceSaveBusinessValidator;
import xbb.ai.erp.module.sales.domain.model.SalesInvoice;
import xbb.ai.erp.module.sales.domain.repository.SalesInvoiceRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesInvoiceLineRepository;
import xbb.ai.erp.module.sales.domain.model.SalesInvoiceLine;
import xbb.ai.erp.module.sales.domain.model.SalesInvoiceLineSource;
import xbb.ai.erp.module.sales.domain.repository.SalesInvoiceLineSourceRepository;
import xbb.ai.erp.module.sales.admin.SalesInvoiceSourceTypeEnum;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceLineDTO;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.base.common.enums.AuditStatusEnum;
import xbb.ai.erp.base.common.enums.InvoiceStatusEnum;
import xbb.ai.erp.module.settlement.contract.InvoiceReceivableApi;
import xbb.ai.erp.module.settlement.contract.InvoiceReceivableCommand;
import xbb.ai.erp.module.system.contract.BooleanConfigKeyEnum;
import xbb.ai.erp.module.system.contract.BusinessConfigQueryApi;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import xbb.ai.erp.module.sales.domain.model.SalesOrder;
import xbb.ai.erp.module.sales.domain.model.SalesOrderItem;
import xbb.ai.erp.module.sales.domain.model.SalesOutboundItem;
import xbb.ai.erp.module.sales.domain.repository.SalesOrderItemRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesOrderRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesOutboundItemRepository;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SalesInvoiceSaveAppServiceImpl {

    private final SalesInvoiceRepository salesInvoiceRepository;
    private final SalesInvoiceLineRepository salesInvoiceLineRepository;
    private final SalesInvoiceLineSourceRepository salesInvoiceLineSourceRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final SalesOutboundItemRepository salesOutboundItemRepository;
    private final BizNoGenerator bizNoGenerator;
    private final InvoiceReceivableApi invoiceReceivableApi;
    private final BusinessConfigQueryApi businessConfigQueryApi;

    private final SalesInvoiceDraftRepository draftRepository;
    private final SalesInvoiceSaveProtocolValidator protocolValidator;
    private final SalesInvoiceSaveCommonValidator commonValidator;
    private final SalesInvoiceSaveBusinessValidator businessValidator;

    public SalesInvoiceSaveAppServiceImpl(SalesInvoiceRepository salesInvoiceRepository,
                                          SalesInvoiceLineRepository salesInvoiceLineRepository,
                                          SalesInvoiceLineSourceRepository salesInvoiceLineSourceRepository,
                                          BizNoGenerator bizNoGenerator,
                                          InvoiceReceivableApi invoiceReceivableApi,
                                          BusinessConfigQueryApi businessConfigQueryApi,
                                          SalesInvoiceDraftRepository draftRepository,
                                          SalesInvoiceSaveProtocolValidator protocolValidator,
                                          SalesInvoiceSaveCommonValidator commonValidator,
                                          SalesInvoiceSaveBusinessValidator businessValidator) {
        this(salesInvoiceRepository, salesInvoiceLineRepository, salesInvoiceLineSourceRepository, null, null, null,
            bizNoGenerator, invoiceReceivableApi, businessConfigQueryApi, draftRepository, protocolValidator,
            commonValidator, businessValidator);
    }

    @Transactional
    public BaseVO saveAndSubmit(SalesInvoiceSubmitSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForSubmit(dto);
        businessValidator.validateForSubmit(dto);
        Long invoiceId = save(dto);
        List<Long> existingLineIds = salesInvoiceLineRepository.findByCondition(
            Map.of("corpid", dto.getCorpid(), "salesInvoiceId", invoiceId)).stream().map(SalesInvoiceLine::getId).toList();
        List<SalesInvoiceLineSource> removedSources = salesInvoiceLineSourceRepository
            .findByInvoiceLineIds(dto.getCorpid(), existingLineIds);
        if (!existingLineIds.isEmpty()) {
            salesInvoiceLineSourceRepository.removeByInvoiceLineIds(dto.getCorpid(), existingLineIds);
            salesInvoiceLineRepository.removeBatchByIds(dto.getCorpid(), existingLineIds);
        }
        List<SalesInvoiceLine> lines = toLines(dto, invoiceId);
        salesInvoiceLineRepository.insertBatch(lines);
        List<SalesInvoiceLineSource> lineSources = toLineSources(dto, lines);
        salesInvoiceLineSourceRepository.insertBatch(lineSources);
        refreshSalesOrderInvoiceStatuses(dto.getCorpid(), dto.getUserId(), removedSources, lineSources);
        if (dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) draftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
        return new BaseVO();
    }

    public Long save(SalesInvoiceSaveDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        SalesInvoiceValidator.validateSave(dto);
        SalesInvoice entity = SalesInvoiceAdminAssembler.toSalesInvoice(dto);
        if (dto.getMain() == null) {
            throw new BizException("销售发票主档不能为空");
        }
        calculateAmounts(dto, entity);
        if (entity.getId() == null) {
            if (entity.getInvoiceNo() == null || entity.getInvoiceNo().isBlank()) {
                entity.setInvoiceNo(bizNoGenerator.next(dto.getCorpid(), BusinessCodeEnum.SALES_INVOICE.getCode()));
            }
            entity.setStatus("DRAFT");
            entity.setAuditStatus(AuditStatusEnum.PENDING.getCode());
            entity.setReceivableOpenedAmount(BigDecimal.ZERO);
            entity.setReceivableAvailableAmount(entity.getAmount());
            return salesInvoiceRepository.insert(entity);
        }
        SalesInvoice existing = requireInvoice(dto.getCorpid(), entity.getId());
        if (!AuditStatusEnum.PENDING.getCode().equals(existing.getAuditStatus())
            && !AuditStatusEnum.REJECTED.getCode().equals(existing.getAuditStatus())) {
            throw new BizException("当前销售发票不允许编辑");
        }
        entity.setInvoiceNo(existing.getInvoiceNo());
        entity.setStatus(existing.getStatus());
        entity.setAuditStatus(existing.getAuditStatus());
        entity.setAuditTime(existing.getAuditTime());
        entity.setReceivableOpenedAmount(existing.getReceivableOpenedAmount());
        entity.setReceivableAvailableAmount(existing.getReceivableAvailableAmount());
        salesInvoiceRepository.update(entity);
        return entity.getId();
    }

    private List<SalesInvoiceLine> toLines(SalesInvoiceSaveDTO dto, Long invoiceId) {
        if (dto.getLines() == null || dto.getLines().isEmpty()) {
//            throw new BizException("销售发票明细不能为空");
            return new ArrayList<>();
        }
        return java.util.stream.IntStream.range(0, dto.getLines().size())
            .mapToObj(index -> SalesInvoiceAdminAssembler.toLine(dto.getLines().get(index), dto.getCorpid(), invoiceId,
                index + 1, dto.getUserId()))
            .toList();
    }

    private List<SalesInvoiceLineSource> toLineSources(SalesInvoiceSaveDTO dto, List<SalesInvoiceLine> lines) {
        List<String> sourceTypes = dto.getLines().stream()
            .map(SalesInvoiceLineDTO::getSourceType)
            .filter(SalesInvoiceSourceTypeEnum::isDocumentSource)
            .distinct()
            .toList();
        if (sourceTypes.size() > 1) {
            throw new BizException("同一销售发票只能选择一种来源单类型");
        }
        List<SalesInvoiceLineSource> sources = new ArrayList<>();
        for (int index = 0; index < dto.getLines().size(); index++) {
            SalesInvoiceLineDTO line = dto.getLines().get(index);
            if (!SalesInvoiceSourceTypeEnum.isDocumentSource(line.getSourceType())) {
                continue;
            }
            if (line.getSourceId() == null || line.getSourceLineId() == null) {
                throw new BizException("来源发票明细缺少来源单据或来源行");
            }
            SalesInvoiceLine savedLine = lines.get(index);
            SalesInvoiceLineSource source = new SalesInvoiceLineSource();
            source.setCorpid(dto.getCorpid());
            source.setSalesInvoiceLineId(savedLine.getId());
            source.setSourceType(line.getSourceType());
            source.setSourceId(line.getSourceId());
            source.setSourceLineId(line.getSourceLineId());
            source.setQuantity(savedLine.getQuantity());
            source.setUntaxedAmount(savedLine.getUntaxedAmount());
            source.setTaxAmount(savedLine.getTaxAmount());
            source.setAmount(savedLine.getAmount());
            source.setCreatorId(dto.getUserId());
            source.setModifyId(dto.getUserId());
            sources.add(source);
        }
        return sources;
    }

    private void calculateAmounts(SalesInvoiceSaveDTO dto, SalesInvoice invoice) {
        List<SalesInvoiceLine> lines = toLines(dto, invoice.getId());
        BigDecimal untaxedAmount = BigDecimal.ZERO;
        BigDecimal taxAmount = BigDecimal.ZERO;
        for (int index = 0; index < lines.size(); index++) {
            SalesInvoiceLine line = lines.get(index);
            if (line.getProductName() == null || line.getProductName().isBlank()
                || line.getQuantity() == null || line.getQuantity().signum() <= 0
                || line.getUnitPrice() == null || line.getUnitPrice().signum() < 0) {
                throw new BizException("销售发票明细数据不完整");
            }
            BigDecimal lineUntaxed = line.getQuantity().multiply(line.getUnitPrice()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal rate = line.getTaxRate() == null ? BigDecimal.ZERO : line.getTaxRate();
            BigDecimal lineTax = lineUntaxed.multiply(rate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            line.setUntaxedAmount(lineUntaxed);
            line.setTaxAmount(lineTax);
            line.setAmount(lineUntaxed.add(lineTax));
            SalesInvoiceLineDTO lineDTO = dto.getLines().get(index);
            lineDTO.setUntaxedAmount(lineUntaxed);
            lineDTO.setTaxAmount(lineTax);
            lineDTO.setAmount(line.getAmount());
            untaxedAmount = untaxedAmount.add(lineUntaxed);
            taxAmount = taxAmount.add(lineTax);
        }
        invoice.setUntaxedAmount(untaxedAmount);
        invoice.setTaxAmount(taxAmount);
        invoice.setAmount(untaxedAmount.add(taxAmount));
        invoice.setInvoiceType(invoice.getInvoiceType() == null ? "SALE" : invoice.getInvoiceType());
    }

    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() != null && !dto.getIdList().isEmpty()) {
            List<SalesInvoiceLineSource> removedSources = new ArrayList<>();
            for (Long id : dto.getIdList()) {
                SalesInvoice invoice = requireInvoice(dto.getCorpid(), id);
                if (!AuditStatusEnum.PENDING.getCode().equals(invoice.getAuditStatus())
                    && !AuditStatusEnum.REJECTED.getCode().equals(invoice.getAuditStatus())) {
                    throw new BizException("当前销售发票不允许删除");
                }
                List<Long> lineIds = salesInvoiceLineRepository.findByCondition(
                    Map.of("corpid", dto.getCorpid(), "salesInvoiceId", id)).stream()
                    .map(SalesInvoiceLine::getId)
                    .toList();
                removedSources.addAll(salesInvoiceLineSourceRepository.findByInvoiceLineIds(dto.getCorpid(), lineIds));
            }
            salesInvoiceRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
            refreshSalesOrderInvoiceStatuses(dto.getCorpid(), dto.getUserId(), removedSources);
        }
    }

    @Transactional
    public BaseVO audit(xbb.ai.erp.base.common.dto.IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        SalesInvoice invoice = requireInvoice(dto.getCorpid(), dto.getId());
        if (!AuditStatusEnum.PENDING.getCode().equals(invoice.getAuditStatus())
            && !AuditStatusEnum.REJECTED.getCode().equals(invoice.getAuditStatus())) {
            throw new BizException("当前销售发票不可审核");
        }
        invoice.setAuditStatus(AuditStatusEnum.APPROVED.getCode());
        invoice.setAuditTime(System.currentTimeMillis());
        invoice.setModifyId(dto.getUserId());
        salesInvoiceRepository.update(invoice);
        return new BaseVO();
    }

    @Transactional
    public BaseVO unaudit(xbb.ai.erp.base.common.dto.IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        SalesInvoice invoice = requireInvoice(dto.getCorpid(), dto.getId());
        if (!AuditStatusEnum.APPROVED.getCode().equals(invoice.getAuditStatus())) {
            throw new BizException("当前销售发票不可反审核");
        }
        invoice.setAuditStatus(AuditStatusEnum.PENDING.getCode());
        invoice.setAuditTime(null);
        invoice.setModifyId(dto.getUserId());
        salesInvoiceRepository.update(invoice);
        return new BaseVO();
    }

    @Transactional
    public BaseVO post(xbb.ai.erp.base.common.dto.IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        SalesInvoice invoice = requireInvoice(dto.getCorpid(), dto.getId());
        if (!AuditStatusEnum.APPROVED.getCode().equals(invoice.getAuditStatus())) {
            throw new BizException("仅已审核销售发票可以过账");
        }
        if (!"DRAFT".equals(invoice.getStatus())) {
            throw new BizException("当前销售发票不可重复过账");
        }
        List<SalesInvoiceLine> invoiceLines = salesInvoiceLineRepository.findByCondition(
            Map.of("corpid", dto.getCorpid(), "salesInvoiceId", invoice.getId()));
        List<SalesInvoiceLineSource> lineSources = invoiceLines.isEmpty()
            ? List.of()
            : salesInvoiceLineSourceRepository.findByInvoiceLineIds(dto.getCorpid(),
                invoiceLines.stream().map(SalesInvoiceLine::getId).toList());
        businessValidator.validateForPost(dto.getCorpid(), invoice.getCustomerId(), invoiceLines, lineSources);
        invoice.setStatus("POSTED");
        invoice.setPostedTime(System.currentTimeMillis());
        invoice.setModifyId(dto.getUserId());
        salesInvoiceRepository.update(invoice);
        if (businessConfigQueryApi.get(dto.getCorpid(),
            BooleanConfigKeyEnum.SALES_INVOICE_AUTO_CREATE_RECEIVABLE)) {
            invoiceReceivableApi.createForInvoice(new InvoiceReceivableCommand(dto.getCorpid(), invoice.getId(),
                invoice.getCustomerId(), invoice.getInvoiceDate(), invoice.getDueDate(), invoice.getAmount(),
                invoice.getRemark(), dto.getUserId()));
        }
        return new BaseVO();
    }

    @Transactional
    public BaseVO voidInvoice(xbb.ai.erp.base.common.dto.IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        SalesInvoice invoice = requireInvoice(dto.getCorpid(), dto.getId());
        if (!"DRAFT".equals(invoice.getStatus())) {
            throw new BizException("仅未过账销售发票可以作废");
        }
        if (AuditStatusEnum.APPROVED.getCode().equals(invoice.getAuditStatus())) {
            throw new BizException("已审核销售发票请先反审核后再作废");
        }
        invoice.setStatus("VOIDED");
        invoice.setModifyId(dto.getUserId());
        salesInvoiceRepository.update(invoice);
        List<Long> lineIds = salesInvoiceLineRepository.findByCondition(
            Map.of("corpid", dto.getCorpid(), "salesInvoiceId", invoice.getId())).stream()
            .map(SalesInvoiceLine::getId)
            .toList();
        refreshSalesOrderInvoiceStatuses(dto.getCorpid(), dto.getUserId(),
            salesInvoiceLineSourceRepository.findByInvoiceLineIds(dto.getCorpid(), lineIds));
        return new BaseVO();
    }

    @Transactional
    public BaseVO redFlush(xbb.ai.erp.base.common.dto.IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        SalesInvoice original = requireInvoice(dto.getCorpid(), dto.getId());
        if (!"POSTED".equals(original.getStatus()) || !"SALE".equals(original.getInvoiceType())) {
            throw new BizException("仅正常已过账销售发票可以红冲");
        }
        if (original.getReceivableOpenedAmount() != null
            && original.getReceivableOpenedAmount().compareTo(BigDecimal.ZERO) > 0) {
            throw new BizException("存在关联应收款，请先完成应收款处理后再红冲发票");
        }
        if (original.getReceivableOpenedAmount() != null
            && original.getReceivableOpenedAmount().compareTo(BigDecimal.ZERO) > 0) {
            throw new BizException("存在关联应收款，請先完成应收款处理后再红冲发票");
        }
        if (!salesInvoiceRepository.findByCondition(Map.of(
            "corpid", dto.getCorpid(), "originalInvoiceId", original.getId())).isEmpty()) {
            throw new BizException("该销售发票已完成红冲");
        }
        long now = System.currentTimeMillis();
        SalesInvoice creditInvoice = new SalesInvoice();
        creditInvoice.setCorpid(dto.getCorpid());
        creditInvoice.setInvoiceNo(bizNoGenerator.next(dto.getCorpid(), BusinessCodeEnum.SALES_INVOICE.getCode()));
        creditInvoice.setCustomerId(original.getCustomerId());
        creditInvoice.setInvoiceDate(now);
        creditInvoice.setDueDate(original.getDueDate());
        creditInvoice.setPaymentTerm(original.getPaymentTerm());
        creditInvoice.setUntaxedAmount(original.getUntaxedAmount().negate());
        creditInvoice.setTaxAmount(original.getTaxAmount().negate());
        creditInvoice.setAmount(original.getAmount().negate());
        creditInvoice.setInvoiceType("CREDIT_NOTE");
        creditInvoice.setOriginalInvoiceId(original.getId());
        creditInvoice.setStatus("POSTED");
        creditInvoice.setAuditStatus(AuditStatusEnum.APPROVED.getCode());
        creditInvoice.setAuditTime(now);
        creditInvoice.setPostedTime(now);
        creditInvoice.setRemark("红冲原销售发票：" + original.getInvoiceNo());
        creditInvoice.setCreatorId(dto.getUserId());
        creditInvoice.setModifyId(dto.getUserId());
        Long creditInvoiceId = salesInvoiceRepository.insert(creditInvoice);
        List<SalesInvoiceLine> originalLines = salesInvoiceLineRepository.findByCondition(
            Map.of("corpid", dto.getCorpid(), "salesInvoiceId", original.getId()));
        List<SalesInvoiceLine> creditLines = originalLines.stream()
            .map(line -> toCreditLine(line, dto.getCorpid(), creditInvoiceId, dto.getUserId()))
            .toList();
        if (!creditLines.isEmpty()) {
            salesInvoiceLineRepository.insertBatch(creditLines);
            List<SalesInvoiceLineSource> originalSources = salesInvoiceLineSourceRepository
                .findByInvoiceLineIds(dto.getCorpid(), originalLines.stream().map(SalesInvoiceLine::getId).toList());
            List<SalesInvoiceLineSource> creditSources = toCreditLineSources(originalLines, creditLines,
                originalSources, dto.getCorpid(), dto.getUserId());
            if (!creditSources.isEmpty()) {
                salesInvoiceLineSourceRepository.insertBatch(creditSources);
                refreshSalesOrderInvoiceStatuses(dto.getCorpid(), dto.getUserId(), creditSources);
            }
        }
        invoiceReceivableApi.createForInvoice(new InvoiceReceivableCommand(dto.getCorpid(), creditInvoiceId,
            creditInvoice.getCustomerId(), creditInvoice.getInvoiceDate(), creditInvoice.getDueDate(),
            creditInvoice.getAmount(), creditInvoice.getRemark(), dto.getUserId()));
        return new BaseVO();
    }

    private SalesInvoiceLine toCreditLine(SalesInvoiceLine originalLine, String corpid, Long invoiceId, String userId) {
        SalesInvoiceLine creditLine = new SalesInvoiceLine();
        creditLine.setCorpid(corpid);
        creditLine.setSalesInvoiceId(invoiceId);
        creditLine.setLineNo(originalLine.getLineNo());
        creditLine.setProductId(originalLine.getProductId());
        creditLine.setProductName(originalLine.getProductName());
        creditLine.setSpecification(originalLine.getSpecification());
        creditLine.setUnitId(originalLine.getUnitId());
        creditLine.setQuantity(originalLine.getQuantity().negate());
        creditLine.setUnitPrice(originalLine.getUnitPrice());
        creditLine.setTaxRate(originalLine.getTaxRate());
        creditLine.setUntaxedAmount(originalLine.getUntaxedAmount().negate());
        creditLine.setTaxAmount(originalLine.getTaxAmount().negate());
        creditLine.setAmount(originalLine.getAmount().negate());
        creditLine.setRemark(originalLine.getRemark());
        creditLine.setCreatorId(userId);
        creditLine.setModifyId(userId);
        return creditLine;
    }

    private List<SalesInvoiceLineSource> toCreditLineSources(List<SalesInvoiceLine> originalLines,
                                                               List<SalesInvoiceLine> creditLines,
                                                               List<SalesInvoiceLineSource> originalSources,
                                                               String corpid, String userId) {
        Map<Long, SalesInvoiceLineSource> sourceByInvoiceLineId = originalSources.stream()
            .collect(java.util.stream.Collectors.toMap(SalesInvoiceLineSource::getSalesInvoiceLineId,
                source -> source));
        List<SalesInvoiceLineSource> creditSources = new ArrayList<>();
        for (int index = 0; index < originalLines.size(); index++) {
            SalesInvoiceLineSource originalSource = sourceByInvoiceLineId.get(originalLines.get(index).getId());
            if (originalSource == null) {
                continue;
            }
            SalesInvoiceLineSource creditSource = new SalesInvoiceLineSource();
            creditSource.setCorpid(corpid);
            creditSource.setSalesInvoiceLineId(creditLines.get(index).getId());
            creditSource.setSourceType(originalSource.getSourceType());
            creditSource.setSourceId(originalSource.getSourceId());
            creditSource.setSourceLineId(originalSource.getSourceLineId());
            creditSource.setQuantity(originalSource.getQuantity().negate());
            creditSource.setUntaxedAmount(originalSource.getUntaxedAmount().negate());
            creditSource.setTaxAmount(originalSource.getTaxAmount().negate());
            creditSource.setAmount(originalSource.getAmount().negate());
            creditSource.setCreatorId(userId);
            creditSource.setModifyId(userId);
            creditSources.add(creditSource);
        }
        return creditSources;
    }

    private void refreshSalesOrderInvoiceStatuses(String corpid, String userId,
                                                   List<SalesInvoiceLineSource>... sourceGroups) {
        Set<Long> salesOrderIds = new HashSet<>();
        for (List<SalesInvoiceLineSource> sources : sourceGroups) {
            for (SalesInvoiceLineSource source : sources) {
                collectSalesOrderId(corpid, source, salesOrderIds);
            }
        }
        for (Long salesOrderId : salesOrderIds) {
            refreshSalesOrderInvoiceStatus(corpid, userId, salesOrderId);
        }
    }

    private void collectSalesOrderId(String corpid, SalesInvoiceLineSource source, Set<Long> salesOrderIds) {
        if (SalesInvoiceSourceTypeEnum.SALES_ORDER.name().equals(source.getSourceType())) {
            salesOrderIds.add(source.getSourceId());
            return;
        }
        if (!SalesInvoiceSourceTypeEnum.SALES_OUTBOUND.name().equals(source.getSourceType())) {
            return;
        }
        SalesOutboundItem outboundItem = salesOutboundItemRepository.findById(corpid, source.getSourceLineId());
        if (outboundItem == null || outboundItem.getSalesOrderItemId() == null) {
            return;
        }
        SalesOrderItem orderItem = salesOrderItemRepository.findById(corpid, outboundItem.getSalesOrderItemId());
        if (orderItem != null) {
            salesOrderIds.add(orderItem.getSalesOrderId());
        }
    }

    private void refreshSalesOrderInvoiceStatus(String corpid, String userId, Long salesOrderId) {
        SalesOrder salesOrder = salesOrderRepository.findById(corpid, salesOrderId);
        if (salesOrder == null) {
            return;
        }
        BigDecimal totalQuantity = BigDecimal.ZERO;
        BigDecimal invoicedQuantity = BigDecimal.ZERO;
        for (SalesOrderItem item : salesOrderItemRepository.findByCondition(
            Map.of("corpid", corpid, "salesOrderId", salesOrderId))) {
            totalQuantity = totalQuantity.add(item.getQty());
            invoicedQuantity = invoicedQuantity.add(salesInvoiceLineSourceRepository
                .sumPostedQuantityBySalesOrderItem(corpid, item.getId(), null));
        }
        salesOrder.setInvoiceStatus(resolveInvoiceStatus(totalQuantity, invoicedQuantity));
        salesOrder.setModifyId(userId);
        salesOrderRepository.update(salesOrder);
    }

    private Integer resolveInvoiceStatus(BigDecimal totalQuantity, BigDecimal invoicedQuantity) {
        if (invoicedQuantity.signum() <= 0) {
            return InvoiceStatusEnum.NOT_INVOICED.getCode();
        }
        if (invoicedQuantity.compareTo(totalQuantity) >= 0) {
            return InvoiceStatusEnum.FULLY_INVOICED.getCode();
        }
        return InvoiceStatusEnum.PARTIALLY_INVOICED.getCode();
    }

    private SalesInvoice requireInvoice(String corpid, Long id) {
        SalesInvoice invoice = salesInvoiceRepository.findById(corpid, id);
        if (invoice == null) {
            throw new BizException("销售发票不存在");
        }
        return invoice;
    }
}
