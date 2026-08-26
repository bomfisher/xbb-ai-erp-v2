package xbb.ai.erp.module.purchase.application.service.save;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.enums.AuditStatusEnum;
import xbb.ai.erp.base.common.enums.InvoiceStatusEnum;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.purchase.admin.PurchaseInvoiceStatusEnum;
import xbb.ai.erp.module.purchase.admin.PurchaseInvoiceSourceTypeEnum;
import xbb.ai.erp.module.purchase.admin.PurchaseInvoiceTypeEnum;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceLineDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceSubmitSaveDTO;
import xbb.ai.erp.module.purchase.application.assembler.PurchaseInvoiceAdminAssembler;
import xbb.ai.erp.module.purchase.application.port.PurchaseInvoiceDraftRepository;
import xbb.ai.erp.module.purchase.application.validator.PurchaseInvoiceSaveBusinessValidator;
import xbb.ai.erp.module.purchase.application.validator.PurchaseInvoiceSaveCommonValidator;
import xbb.ai.erp.module.purchase.application.validator.PurchaseInvoiceSaveProtocolValidator;
import xbb.ai.erp.module.purchase.application.validator.PurchaseInvoiceValidator;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInvoice;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInvoiceLine;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInvoiceLineSource;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInboundItem;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrder;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrderItem;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInboundItemRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInvoiceLineRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInvoiceLineSourceRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInvoiceRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderItemRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderRepository;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PurchaseInvoiceSaveAppServiceImpl {

    private final PurchaseInvoiceRepository purchaseInvoiceRepository;
    private final PurchaseInvoiceLineRepository lineRepository;
    private final PurchaseInvoiceLineSourceRepository sourceRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final PurchaseInboundItemRepository purchaseInboundItemRepository;
    private final BizNoGenerator bizNoGenerator;
    private final PurchaseInvoiceDraftRepository draftRepository;
    private final PurchaseInvoiceSaveProtocolValidator protocolValidator;
    private final PurchaseInvoiceSaveCommonValidator commonValidator;
    private final PurchaseInvoiceSaveBusinessValidator businessValidator;

    public PurchaseInvoiceSaveAppServiceImpl(PurchaseInvoiceRepository purchaseInvoiceRepository,
                                             PurchaseInvoiceLineRepository lineRepository,
                                             PurchaseInvoiceLineSourceRepository sourceRepository,
                                             BizNoGenerator bizNoGenerator,
                                             PurchaseInvoiceDraftRepository draftRepository,
                                             PurchaseInvoiceSaveProtocolValidator protocolValidator,
                                             PurchaseInvoiceSaveCommonValidator commonValidator,
                                             PurchaseInvoiceSaveBusinessValidator businessValidator) {
        this(purchaseInvoiceRepository, lineRepository, sourceRepository, null, null, null, bizNoGenerator,
            draftRepository, protocolValidator, commonValidator, businessValidator);
    }

    @Transactional
    public BaseVO audit(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        PurchaseInvoice invoice = requireInvoice(dto.getCorpid(), dto.getId());
        if (!AuditStatusEnum.PENDING.getCode().equals(invoice.getAuditStatus())) {
            throw new BizException("当前采购发票不可审核");
        }
        invoice.setAuditStatus(AuditStatusEnum.APPROVED.getCode());
        invoice.setAuditTime(System.currentTimeMillis());
        invoice.setModifyId(dto.getUserId());
        purchaseInvoiceRepository.update(invoice);
        return new BaseVO();
    }

    @Transactional
    public BaseVO unaudit(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        PurchaseInvoice invoice = requireInvoice(dto.getCorpid(), dto.getId());
        if (!AuditStatusEnum.APPROVED.getCode().equals(invoice.getAuditStatus())) {
            throw new BizException("当前采购发票不可反审核");
        }
        if (!PurchaseInvoiceStatusEnum.DRAFT.getCode().equals(invoice.getStatus())) {
            throw new BizException("已过账采购发票不可反审核");
        }
        invoice.setAuditStatus(AuditStatusEnum.PENDING.getCode());
        invoice.setModifyId(dto.getUserId());
        purchaseInvoiceRepository.update(invoice);
        return new BaseVO();
    }

    @Transactional
    public BaseVO post(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        PurchaseInvoice invoice = requireInvoice(dto.getCorpid(), dto.getId());
        if (!AuditStatusEnum.APPROVED.getCode().equals(invoice.getAuditStatus())) {
            throw new BizException("仅已审核采购发票可以过账");
        }
        if (!PurchaseInvoiceStatusEnum.DRAFT.getCode().equals(invoice.getStatus())) {
            throw new BizException("当前采购发票不可重复过账");
        }
        invoice.setStatus(PurchaseInvoiceStatusEnum.POSTED.getCode());
        invoice.setPostedTime(System.currentTimeMillis());
        invoice.setModifyId(dto.getUserId());
        purchaseInvoiceRepository.update(invoice);
        return new BaseVO();
    }

    @Transactional
    public BaseVO voidInvoice(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        PurchaseInvoice invoice = requireInvoice(dto.getCorpid(), dto.getId());
        if (!PurchaseInvoiceStatusEnum.DRAFT.getCode().equals(invoice.getStatus())) {
            throw new BizException("仅未过账采购发票可以作废");
        }
        if (AuditStatusEnum.APPROVED.getCode().equals(invoice.getAuditStatus())) {
            throw new BizException("已审核采购发票请先反审核后再作废");
        }
        List<Long> lineIds = lineRepository.findByInvoiceId(dto.getCorpid(), invoice.getId()).stream()
            .map(PurchaseInvoiceLine::getId)
            .toList();
        List<PurchaseInvoiceLineSource> sources = sourceRepository.findByInvoiceLineIds(dto.getCorpid(), lineIds);
        invoice.setStatus(PurchaseInvoiceStatusEnum.VOIDED.getCode());
        invoice.setModifyId(dto.getUserId());
        purchaseInvoiceRepository.update(invoice);
        refreshPurchaseOrderInvoiceStatuses(dto.getCorpid(), dto.getUserId(), sources);
        return new BaseVO();
    }

    @Transactional
    public BaseVO redFlush(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        PurchaseInvoice original = requireInvoice(dto.getCorpid(), dto.getId());
        if (!PurchaseInvoiceStatusEnum.POSTED.getCode().equals(original.getStatus())
            || !PurchaseInvoiceTypeEnum.PURCHASE.getCode().equals(original.getInvoiceType())) {
            throw new BizException("仅正常已过账采购发票可以红冲");
        }
        if (original.getPayableOpenedAmount() != null
            && original.getPayableOpenedAmount().compareTo(BigDecimal.ZERO) > 0) {
            throw new BizException("存在关联应付款，请先完成应付款处理后再红冲发票");
        }
        if (original.getPayableOpenedAmount() != null
            && original.getPayableOpenedAmount().compareTo(BigDecimal.ZERO) > 0) {
            throw new BizException("存在关联应付款，请先完成应付款处理后再红冲发票");
        }
        if (!purchaseInvoiceRepository.findByCondition(Map.of(
            "corpid", dto.getCorpid(), "originalInvoiceId", original.getId())).isEmpty()) {
            throw new BizException("该采购发票已完成红冲");
        }

        long now = System.currentTimeMillis();
        PurchaseInvoice creditInvoice = toCreditInvoice(original, dto, now);
        Long creditInvoiceId = purchaseInvoiceRepository.insert(creditInvoice);
        List<PurchaseInvoiceLineSource> creditSources = copyCreditLines(original, creditInvoiceId, dto.getCorpid(),
            dto.getUserId());
        refreshPurchaseOrderInvoiceStatuses(dto.getCorpid(), dto.getUserId(), creditSources);
        return new BaseVO();
    }

    @Transactional
    public BaseVO saveAndSubmit(PurchaseInvoiceSubmitSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForSubmit(dto);
        businessValidator.validateForSubmit(dto);
        save(dto);
        if (dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) {
            draftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
        }
        return new BaseVO();
    }

    public Long save(PurchaseInvoiceSaveDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        PurchaseInvoiceValidator.validateSave(dto);
        PurchaseInvoice entity = PurchaseInvoiceAdminAssembler.toPurchaseInvoice(dto);
        if (entity.getId() == null) {
            initializeNewInvoice(entity, dto.getCorpid());
            Long invoiceId = purchaseInvoiceRepository.insert(entity);
            replaceLines(dto, invoiceId);
            return invoiceId;
        }
        purchaseInvoiceRepository.update(entity);
        replaceLines(dto, entity.getId());
        return entity.getId();
    }

    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() != null && !dto.getIdList().isEmpty()) {
            purchaseInvoiceRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
        }
    }

    private List<PurchaseInvoiceLineSource> copyCreditLines(PurchaseInvoice original, Long creditInvoiceId,
                                                             String corpid, String userId) {
        List<PurchaseInvoiceLine> originalLines = lineRepository.findByInvoiceId(corpid, original.getId());
        if (originalLines.isEmpty()) {
            return List.of();
        }
        List<PurchaseInvoiceLine> creditLines = originalLines.stream()
            .map(line -> toCreditLine(line, corpid, creditInvoiceId, userId))
            .toList();
        lineRepository.insertBatch(creditLines);

        List<PurchaseInvoiceLineSource> originalSources = sourceRepository.findByInvoiceLineIds(
            corpid, originalLines.stream().map(PurchaseInvoiceLine::getId).toList());
        List<PurchaseInvoiceLineSource> creditSources = toCreditLineSources(
            originalLines, creditLines, originalSources, corpid, userId);
        sourceRepository.insertBatch(creditSources);
        return creditSources;
    }

    private PurchaseInvoice toCreditInvoice(PurchaseInvoice original, IdBaseDTO dto, long now) {
        PurchaseInvoice creditInvoice = new PurchaseInvoice();
        creditInvoice.setCorpid(dto.getCorpid());
        creditInvoice.setInvoiceNo(bizNoGenerator.next(dto.getCorpid(), BusinessCodeEnum.PURCHASE_INVOICE.getCode()));
        creditInvoice.setSupplierInvoiceNo(original.getSupplierInvoiceNo());
        creditInvoice.setSupplierId(original.getSupplierId());
        creditInvoice.setInvoiceDate(now);
        creditInvoice.setDueDate(original.getDueDate());
        creditInvoice.setPaymentTerm(original.getPaymentTerm());
        creditInvoice.setUntaxedAmount(negate(original.getUntaxedAmount()));
        creditInvoice.setTaxAmount(negate(original.getTaxAmount()));
        creditInvoice.setAmount(negate(original.getAmount()));
        creditInvoice.setPayableOpenedAmount(BigDecimal.ZERO);
        creditInvoice.setPayableAvailableAmount(creditInvoice.getAmount());
        creditInvoice.setInvoiceType(PurchaseInvoiceTypeEnum.CREDIT_NOTE.getCode());
        creditInvoice.setOriginalInvoiceId(original.getId());
        creditInvoice.setStatus(PurchaseInvoiceStatusEnum.POSTED.getCode());
        creditInvoice.setAuditStatus(AuditStatusEnum.APPROVED.getCode());
        creditInvoice.setAuditTime(now);
        creditInvoice.setPostedTime(now);
        creditInvoice.setRemark("红冲原采购发票：" + original.getInvoiceNo());
        creditInvoice.setSourceType(original.getSourceType());
        creditInvoice.setSourceId(original.getSourceId());
        creditInvoice.setManualReason(original.getManualReason());
        creditInvoice.setCreatorId(dto.getUserId());
        creditInvoice.setModifyId(dto.getUserId());
        return creditInvoice;
    }

    private PurchaseInvoiceLine toCreditLine(PurchaseInvoiceLine original, String corpid, Long invoiceId,
                                              String userId) {
        PurchaseInvoiceLine creditLine = new PurchaseInvoiceLine();
        creditLine.setCorpid(corpid);
        creditLine.setPurchaseInvoiceId(invoiceId);
        creditLine.setLineNo(original.getLineNo());
        creditLine.setProductId(original.getProductId());
        creditLine.setProductName(original.getProductName());
        creditLine.setSpecification(original.getSpecification());
        creditLine.setUnitName(original.getUnitName());
        creditLine.setQuantity(negate(original.getQuantity()));
        creditLine.setUnitPrice(original.getUnitPrice());
        creditLine.setTaxRate(original.getTaxRate());
        creditLine.setUntaxedAmount(negate(original.getUntaxedAmount()));
        creditLine.setTaxAmount(negate(original.getTaxAmount()));
        creditLine.setAmount(negate(original.getAmount()));
        creditLine.setRemark(original.getRemark());
        creditLine.setCreatorId(userId);
        creditLine.setModifyId(userId);
        return creditLine;
    }

    private List<PurchaseInvoiceLineSource> toCreditLineSources(List<PurchaseInvoiceLine> originalLines,
                                                                  List<PurchaseInvoiceLine> creditLines,
                                                                  List<PurchaseInvoiceLineSource> originalSources,
                                                                  String corpid, String userId) {
        Map<Long, PurchaseInvoiceLineSource> sourceByLineId = new HashMap<>();
        for (PurchaseInvoiceLineSource source : originalSources) {
            sourceByLineId.put(source.getPurchaseInvoiceLineId(), source);
        }

        List<PurchaseInvoiceLineSource> creditSources = new ArrayList<>();
        for (int index = 0; index < originalLines.size(); index++) {
            PurchaseInvoiceLineSource originalSource = sourceByLineId.get(originalLines.get(index).getId());
            if (originalSource == null) {
                continue;
            }
            PurchaseInvoiceLineSource creditSource = new PurchaseInvoiceLineSource();
            creditSource.setCorpid(corpid);
            creditSource.setPurchaseInvoiceLineId(creditLines.get(index).getId());
            creditSource.setSourceType(originalSource.getSourceType());
            creditSource.setSourceId(originalSource.getSourceId());
            creditSource.setSourceLineId(originalSource.getSourceLineId());
            creditSource.setQuantity(negate(originalSource.getQuantity()));
            creditSource.setUntaxedAmount(negate(originalSource.getUntaxedAmount()));
            creditSource.setTaxAmount(negate(originalSource.getTaxAmount()));
            creditSource.setAmount(negate(originalSource.getAmount()));
            creditSource.setCreatorId(userId);
            creditSource.setModifyId(userId);
            creditSources.add(creditSource);
        }
        return creditSources;
    }

    private void replaceLines(PurchaseInvoiceSaveDTO dto, Long invoiceId) {
        List<PurchaseInvoiceLine> oldLines = lineRepository.findByInvoiceId(dto.getCorpid(), invoiceId);
        List<PurchaseInvoiceLineSource> removedSources = sourceRepository.findByInvoiceLineIds(dto.getCorpid(),
            oldLines.stream().map(PurchaseInvoiceLine::getId).toList());
        sourceRepository.removeByInvoiceLineIds(dto.getCorpid(), oldLines.stream().map(PurchaseInvoiceLine::getId).toList());
        lineRepository.removeByInvoiceId(dto.getCorpid(), invoiceId);

        List<PurchaseInvoiceLineDTO> lineDtos = dto.getLines() == null ? List.of() : dto.getLines();
        List<PurchaseInvoiceLine> lines = java.util.stream.IntStream.range(0, lineDtos.size())
            .mapToObj(index -> PurchaseInvoiceAdminAssembler.toLine(lineDtos.get(index), dto.getCorpid(), invoiceId,
                index + 1, dto.getUserId()))
            .toList();
        lineRepository.insertBatch(lines);

        List<PurchaseInvoiceLineSource> sources = new ArrayList<>();
        for (int index = 0; index < lines.size(); index++) {
            PurchaseInvoiceLineDTO lineDto = lineDtos.get(index);
            if (lineDto.getSourceType() == null || lineDto.getSourceId() == null || lineDto.getSourceLineId() == null) {
                continue;
            }
            PurchaseInvoiceLine line = lines.get(index);
            PurchaseInvoiceLineSource source = new PurchaseInvoiceLineSource();
            source.setCorpid(dto.getCorpid());
            source.setPurchaseInvoiceLineId(line.getId());
            source.setSourceType(lineDto.getSourceType());
            source.setSourceId(lineDto.getSourceId());
            source.setSourceLineId(lineDto.getSourceLineId());
            source.setQuantity(line.getQuantity());
            source.setUntaxedAmount(line.getUntaxedAmount());
            source.setTaxAmount(line.getTaxAmount());
            source.setAmount(line.getAmount());
            source.setCreatorId(dto.getUserId());
            source.setModifyId(dto.getUserId());
            sources.add(source);
        }
        sourceRepository.insertBatch(sources);
        refreshPurchaseOrderInvoiceStatuses(dto.getCorpid(), dto.getUserId(), removedSources, sources);
    }

    private void refreshPurchaseOrderInvoiceStatuses(String corpid, String userId,
                                                      List<PurchaseInvoiceLineSource>... sourceGroups) {
        if (purchaseOrderRepository == null || purchaseOrderItemRepository == null
            || purchaseInboundItemRepository == null) {
            return;
        }
        Set<Long> purchaseOrderIds = new HashSet<>();
        for (List<PurchaseInvoiceLineSource> sources : sourceGroups) {
            for (PurchaseInvoiceLineSource source : sources) {
                collectPurchaseOrderId(corpid, source, purchaseOrderIds);
            }
        }
        for (Long purchaseOrderId : purchaseOrderIds) {
            refreshPurchaseOrderInvoiceStatus(corpid, userId, purchaseOrderId);
        }
    }

    private void collectPurchaseOrderId(String corpid, PurchaseInvoiceLineSource source,
                                        Set<Long> purchaseOrderIds) {
        if (PurchaseInvoiceSourceTypeEnum.PURCHASE_ORDER.name().equals(source.getSourceType())) {
            purchaseOrderIds.add(source.getSourceId());
            return;
        }
        if (!PurchaseInvoiceSourceTypeEnum.PURCHASE_INBOUND.name().equals(source.getSourceType())) {
            return;
        }
        PurchaseInboundItem inboundItem = purchaseInboundItemRepository.findById(corpid, source.getSourceLineId());
        if (inboundItem == null || inboundItem.getPurchaseOrderItemId() == null) {
            return;
        }
        PurchaseOrderItem orderItem = purchaseOrderItemRepository.findById(corpid,
            inboundItem.getPurchaseOrderItemId());
        if (orderItem != null) {
            purchaseOrderIds.add(orderItem.getPurchaseOrderId());
        }
    }

    private void refreshPurchaseOrderInvoiceStatus(String corpid, String userId, Long purchaseOrderId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(corpid, purchaseOrderId);
        if (purchaseOrder == null) {
            return;
        }
        BigDecimal totalQuantity = BigDecimal.ZERO;
        BigDecimal invoicedQuantity = BigDecimal.ZERO;
        for (PurchaseOrderItem item : purchaseOrderItemRepository.findByCondition(
            Map.of("corpid", corpid, "purchaseOrderId", purchaseOrderId))) {
            totalQuantity = totalQuantity.add(item.getQty());
            invoicedQuantity = invoicedQuantity.add(sourceRepository.sumInvoiceQuantityByPurchaseOrderItem(corpid,
                item.getId()));
        }
        purchaseOrder.setInvoiceStatus(resolveInvoiceStatus(totalQuantity, invoicedQuantity));
        purchaseOrder.setModifyId(userId);
        purchaseOrderRepository.update(purchaseOrder);
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

    private void initializeNewInvoice(PurchaseInvoice invoice, String corpid) {
        if (invoice.getInvoiceNo() == null || invoice.getInvoiceNo().isBlank()) {
            invoice.setInvoiceNo(bizNoGenerator.next(corpid, BusinessCodeEnum.PURCHASE_INVOICE.getCode()));
        }
        if (invoice.getInvoiceDate() == null) {
            invoice.setInvoiceDate(System.currentTimeMillis());
        }
        if (invoice.getUntaxedAmount() == null) {
            invoice.setUntaxedAmount(BigDecimal.ZERO);
        }
        if (invoice.getTaxAmount() == null) {
            invoice.setTaxAmount(BigDecimal.ZERO);
        }
        if (invoice.getAmount() == null) {
            invoice.setAmount(invoice.getUntaxedAmount().add(invoice.getTaxAmount()));
        }
        invoice.setPayableOpenedAmount(BigDecimal.ZERO);
        invoice.setPayableAvailableAmount(invoice.getAmount());
        if (invoice.getInvoiceType() == null) {
            invoice.setInvoiceType(PurchaseInvoiceTypeEnum.PURCHASE.getCode());
        }
        if (invoice.getStatus() == null) {
            invoice.setStatus(PurchaseInvoiceStatusEnum.DRAFT.getCode());
        }
        if (invoice.getAuditStatus() == null) {
            invoice.setAuditStatus(AuditStatusEnum.PENDING.getCode());
        }
    }

    private PurchaseInvoice requireInvoice(String corpid, Long invoiceId) {
        PurchaseInvoice invoice = purchaseInvoiceRepository.findById(corpid, invoiceId);
        if (invoice == null) {
            throw new BizException("采购发票不存在");
        }
        return invoice;
    }

    private BigDecimal negate(BigDecimal amount) {
        return amount == null ? BigDecimal.ZERO : amount.negate();
    }
}
