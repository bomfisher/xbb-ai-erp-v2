package xbb.ai.erp.module.purchase.application.assembler;

import java.util.Objects;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceSourceSelectionDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceLineDTO;
import java.util.List;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInvoiceSaveItemVO;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInvoice;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInvoiceLine;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInvoiceLineSource;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class PurchaseInvoiceAdminAssembler {

    private PurchaseInvoiceAdminAssembler() {
    }

    public static PurchaseInvoiceSaveItemVO buildEmptySaveItemVO() {
        return new PurchaseInvoiceSaveItemVO();
    }

    public static PurchaseInvoice toPurchaseInvoice(PurchaseInvoiceSaveDTO dto) {
        PurchaseInvoice purchaseInvoice = new PurchaseInvoice();
        PurchaseInvoiceMainDTO main = dto.getMain();
        if (main != null) {
            purchaseInvoice.setId(main.getId());
            purchaseInvoice.setCorpid(main.getCorpid());
            purchaseInvoice.setInvoiceNo(main.getInvoiceNo());
            purchaseInvoice.setSupplierInvoiceNo(main.getSupplierInvoiceNo());
            purchaseInvoice.setSupplierId(main.getSupplierId());
            purchaseInvoice.setInvoiceDate(main.getInvoiceDate());
            purchaseInvoice.setDueDate(main.getDueDate());
            purchaseInvoice.setPaymentTerm(main.getPaymentTerm());
            purchaseInvoice.setUntaxedAmount(main.getUntaxedAmount());
            purchaseInvoice.setTaxAmount(main.getTaxAmount());
            purchaseInvoice.setAmount(main.getAmount());
            purchaseInvoice.setInvoiceType(main.getInvoiceType());
            purchaseInvoice.setStatus(main.getStatus());
            purchaseInvoice.setAuditStatus(main.getAuditStatus());
            purchaseInvoice.setAuditTime(main.getAuditTime());
            purchaseInvoice.setPostedTime(main.getPostedTime());
            purchaseInvoice.setOriginalInvoiceId(main.getOriginalInvoiceId());
            purchaseInvoice.setRemark(main.getRemark());
            purchaseInvoice.setSourceType(main.getSourceType());
            purchaseInvoice.setSourceId(main.getSourceId());
            purchaseInvoice.setManualReason(main.getManualReason());
            purchaseInvoice.setCreatorId(main.getCreatorId());
            purchaseInvoice.setModifyId(main.getModifyId());
            if (Objects.isNull(main.getId())) {
                purchaseInvoice.setCreatorId(dto.getUserId());
            }
            purchaseInvoice.setModifyId(dto.getUserId());
        }
        purchaseInvoice.setCorpid(dto.getCorpid());
        if (dto.getSourceSelection() != null) {
            purchaseInvoice.setSourceType(dto.getSourceSelection().getSourceType());
            purchaseInvoice.setSourceId(dto.getSourceSelection().getSourceId());
            purchaseInvoice.setManualReason(dto.getSourceSelection().getManualReason());
        }
        return purchaseInvoice;
    }

    public static PurchaseInvoiceListItemVO toListItemVO(PurchaseInvoice purchaseInvoice) {
        PurchaseInvoiceListItemVO vo = new PurchaseInvoiceListItemVO();
        vo.setId(Objects.isNull(purchaseInvoice.getId()) ? "" : Objects.toString(purchaseInvoice.getId()));
        vo.setInvoiceNo(purchaseInvoice.getInvoiceNo());
        vo.setSupplierInvoiceNo(purchaseInvoice.getSupplierInvoiceNo());
        vo.setSupplierId(Objects.isNull(purchaseInvoice.getSupplierId()) ? "" : Objects.toString(purchaseInvoice.getSupplierId()));
        vo.setInvoiceDate(Objects.isNull(purchaseInvoice.getInvoiceDate()) ? "" : Objects.toString(purchaseInvoice.getInvoiceDate()));
        vo.setDueDate(Objects.isNull(purchaseInvoice.getDueDate()) ? "" : Objects.toString(purchaseInvoice.getDueDate()));
        vo.setPaymentTerm(purchaseInvoice.getPaymentTerm());
        vo.setUntaxedAmount(Objects.isNull(purchaseInvoice.getUntaxedAmount()) ? "" : Objects.toString(purchaseInvoice.getUntaxedAmount()));
        vo.setTaxAmount(Objects.isNull(purchaseInvoice.getTaxAmount()) ? "" : Objects.toString(purchaseInvoice.getTaxAmount()));
        vo.setAmount(Objects.isNull(purchaseInvoice.getAmount()) ? "" : Objects.toString(purchaseInvoice.getAmount()));
        vo.setInvoiceType(purchaseInvoice.getInvoiceType());
        vo.setStatus(purchaseInvoice.getStatus());
        vo.setAuditStatus(Objects.isNull(purchaseInvoice.getAuditStatus()) ? "" : Objects.toString(purchaseInvoice.getAuditStatus()));
        vo.setRemark(purchaseInvoice.getRemark());
        return vo;
    }

    public static PurchaseInvoiceSaveItemVO toSaveItemVO(PurchaseInvoice purchaseInvoice) {
        return toSaveItemVO(purchaseInvoice, List.of(), List.of());
    }

    public static PurchaseInvoiceSaveItemVO toSaveItemVO(PurchaseInvoice purchaseInvoice,
        List<PurchaseInvoiceLine> lines, List<PurchaseInvoiceLineSource> sources) {
        PurchaseInvoiceSaveItemVO vo = new PurchaseInvoiceSaveItemVO();
        if (purchaseInvoice == null) {
            return vo;
        }
        PurchaseInvoiceMainDTO main = new PurchaseInvoiceMainDTO();
        main.setId(purchaseInvoice.getId());
        main.setCorpid(purchaseInvoice.getCorpid());
        main.setInvoiceNo(purchaseInvoice.getInvoiceNo());
        main.setSupplierInvoiceNo(purchaseInvoice.getSupplierInvoiceNo());
        main.setSupplierId(purchaseInvoice.getSupplierId());
        main.setInvoiceDate(purchaseInvoice.getInvoiceDate());
        main.setDueDate(purchaseInvoice.getDueDate());
        main.setPaymentTerm(purchaseInvoice.getPaymentTerm());
        main.setUntaxedAmount(purchaseInvoice.getUntaxedAmount());
        main.setTaxAmount(purchaseInvoice.getTaxAmount());
        main.setAmount(purchaseInvoice.getAmount());
        main.setInvoiceType(purchaseInvoice.getInvoiceType());
        main.setStatus(purchaseInvoice.getStatus());
        main.setAuditStatus(purchaseInvoice.getAuditStatus());
        main.setAuditTime(purchaseInvoice.getAuditTime());
        main.setPostedTime(purchaseInvoice.getPostedTime());
        main.setOriginalInvoiceId(purchaseInvoice.getOriginalInvoiceId());
        main.setRemark(purchaseInvoice.getRemark());
        main.setSourceType(purchaseInvoice.getSourceType());
        main.setSourceId(purchaseInvoice.getSourceId());
        main.setManualReason(purchaseInvoice.getManualReason());
        main.setCreatorId(purchaseInvoice.getCreatorId());
        main.setModifyId(purchaseInvoice.getModifyId());
        vo.setMain(main);
        PurchaseInvoiceSourceSelectionDTO sourceSelection = new PurchaseInvoiceSourceSelectionDTO();
        sourceSelection.setSourceType(purchaseInvoice.getSourceType());
        sourceSelection.setSourceId(purchaseInvoice.getSourceId());
        sourceSelection.setManualReason(purchaseInvoice.getManualReason());
        vo.setSourceSelection(sourceSelection);
        Map<Long, PurchaseInvoiceLineSource> sourceByLineId = sources.stream().collect(Collectors.toMap(
            PurchaseInvoiceLineSource::getPurchaseInvoiceLineId, Function.identity(), (first, ignored) -> first));
        vo.setLines(lines.stream().map(line -> toLineDTO(line, sourceByLineId.get(line.getId()))).toList());
        return vo;
    }

    public static PurchaseInvoiceLine toLine(PurchaseInvoiceLineDTO dto, String corpid, Long invoiceId,
        int lineNo, String userId) {
        PurchaseInvoiceLine line = new PurchaseInvoiceLine();
        line.setId(dto.getId()); line.setCorpid(corpid); line.setPurchaseInvoiceId(invoiceId); line.setLineNo(lineNo);
        line.setProductId(dto.getProductId()); line.setProductName(dto.getProductName()); line.setSpecification(dto.getSpecification());
        line.setUnitName(dto.getUnitName()); line.setQuantity(dto.getQuantity()); line.setUnitPrice(dto.getUnitPrice());
        line.setTaxRate(dto.getTaxRate()); line.setUntaxedAmount(dto.getUntaxedAmount()); line.setTaxAmount(dto.getTaxAmount());
        line.setAmount(dto.getAmount()); line.setRemark(dto.getRemark()); line.setCreatorId(userId); line.setModifyId(userId);
        return line;
    }

    private static PurchaseInvoiceLineDTO toLineDTO(PurchaseInvoiceLine line, PurchaseInvoiceLineSource source) {
        PurchaseInvoiceLineDTO dto = new PurchaseInvoiceLineDTO();
        dto.setId(line.getId()); dto.setProductId(line.getProductId()); dto.setProductName(line.getProductName());
        dto.setSpecification(line.getSpecification()); dto.setUnitName(line.getUnitName()); dto.setQuantity(line.getQuantity());
        dto.setUnitPrice(line.getUnitPrice()); dto.setTaxRate(line.getTaxRate()); dto.setUntaxedAmount(line.getUntaxedAmount());
        dto.setTaxAmount(line.getTaxAmount()); dto.setAmount(line.getAmount()); dto.setRemark(line.getRemark());
        if (source != null) { dto.setSourceType(source.getSourceType()); dto.setSourceId(source.getSourceId()); dto.setSourceLineId(source.getSourceLineId()); }
        return dto;
    }

    public static PurchaseInvoiceDetailVO toDetailVO(PurchaseInvoiceSaveItemVO saveItemVO) {
        PurchaseInvoiceDetailVO detailVO = new PurchaseInvoiceDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }
}
