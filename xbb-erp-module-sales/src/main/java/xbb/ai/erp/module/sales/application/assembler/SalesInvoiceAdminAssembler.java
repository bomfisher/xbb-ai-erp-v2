package xbb.ai.erp.module.sales.application.assembler;

import java.util.Objects;

import xbb.ai.erp.base.common.enums.DocumentStatusEnum;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceMainDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceSaveDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceLineDTO;
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceDetailVO;
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceListItemVO;
import xbb.ai.erp.module.sales.admin.vo.SalesInvoiceSaveItemVO;
import xbb.ai.erp.module.sales.domain.model.SalesInvoice;
import xbb.ai.erp.module.sales.domain.model.SalesInvoiceLine;
import xbb.ai.erp.module.sales.domain.model.SalesInvoiceLineSource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class SalesInvoiceAdminAssembler {

    private SalesInvoiceAdminAssembler() {
    }

    public static SalesInvoiceSaveItemVO buildEmptySaveItemVO() {
        return new SalesInvoiceSaveItemVO();
    }

    public static SalesInvoice toSalesInvoice(SalesInvoiceSaveDTO dto) {
        SalesInvoice salesInvoice = new SalesInvoice();
        SalesInvoiceMainDTO main = dto.getMain();
        if (main != null) {
            salesInvoice.setId(main.getId());
            salesInvoice.setCorpid(main.getCorpid());
            salesInvoice.setInvoiceNo(main.getInvoiceNo());
            salesInvoice.setCustomerId(main.getCustomerId());
            salesInvoice.setInvoiceDate(main.getInvoiceDate());
            salesInvoice.setDueDate(main.getDueDate());
            salesInvoice.setPaymentTerm(main.getPaymentTerm());
            salesInvoice.setUntaxedAmount(main.getUntaxedAmount());
            salesInvoice.setTaxAmount(main.getTaxAmount());
            salesInvoice.setAmount(main.getAmount());
            salesInvoice.setInvoiceType(main.getInvoiceType());
            salesInvoice.setStatus("");
            salesInvoice.setAuditStatus(main.getAuditStatus());
            salesInvoice.setAuditTime(main.getAuditTime());
            salesInvoice.setPostedTime(main.getPostedTime());
            salesInvoice.setRemark(main.getRemark());
            salesInvoice.setCreatorId(main.getCreatorId());
            salesInvoice.setModifyId(main.getModifyId());
            if (Objects.isNull(main.getId())) {
                salesInvoice.setCreatorId(dto.getUserId());
            }
            salesInvoice.setModifyId(dto.getUserId());
        }
        salesInvoice.setCorpid(dto.getCorpid());
        return salesInvoice;
    }

    public static SalesInvoiceListItemVO toListItemVO(SalesInvoice salesInvoice) {
        SalesInvoiceListItemVO vo = new SalesInvoiceListItemVO();
        vo.setId(Objects.isNull(salesInvoice.getId()) ? "" : Objects.toString(salesInvoice.getId()));
        vo.setInvoiceNo(salesInvoice.getInvoiceNo());
        vo.setCustomerId(Objects.isNull(salesInvoice.getCustomerId()) ? "" : Objects.toString(salesInvoice.getCustomerId()));
        vo.setInvoiceDate(Objects.isNull(salesInvoice.getInvoiceDate()) ? "" : java.time.Instant.ofEpochMilli(salesInvoice.getInvoiceDate()).atZone(java.time.ZoneId.systemDefault()).toLocalDate().toString());
        vo.setDueDate(Objects.isNull(salesInvoice.getDueDate()) ? "" : java.time.Instant.ofEpochMilli(salesInvoice.getDueDate()).atZone(java.time.ZoneId.systemDefault()).toLocalDate().toString());
        vo.setPaymentTerm(salesInvoice.getPaymentTerm());
        vo.setUntaxedAmount(Objects.isNull(salesInvoice.getUntaxedAmount()) ? "" : Objects.toString(salesInvoice.getUntaxedAmount()));
        vo.setTaxAmount(Objects.isNull(salesInvoice.getTaxAmount()) ? "" : Objects.toString(salesInvoice.getTaxAmount()));
        vo.setAmount(Objects.isNull(salesInvoice.getAmount()) ? "" : Objects.toString(salesInvoice.getAmount()));
        vo.setInvoiceType(Objects.isNull(salesInvoice.getInvoiceType()) ? "" : Objects.toString(salesInvoice.getInvoiceType()));
        vo.setStatus(Objects.isNull(salesInvoice.getStatus()) ? "" : Objects.toString(salesInvoice.getStatus()));
        vo.setAuditStatus(Objects.isNull(salesInvoice.getAuditStatus()) ? "" : Objects.toString(salesInvoice.getAuditStatus()));
        vo.setAuditTime(Objects.isNull(salesInvoice.getAuditTime()) ? "" : java.time.Instant.ofEpochMilli(salesInvoice.getAuditTime()).atZone(java.time.ZoneId.systemDefault()).toLocalDate().toString());
        vo.setPostedTime(Objects.isNull(salesInvoice.getPostedTime()) ? "" : java.time.Instant.ofEpochMilli(salesInvoice.getPostedTime()).atZone(java.time.ZoneId.systemDefault()).toLocalDate().toString());
        vo.setRemark(salesInvoice.getRemark());
        vo.setCreatorId(salesInvoice.getCreatorId());
        vo.setModifyId(salesInvoice.getModifyId());
        return vo;
    }

    public static SalesInvoiceSaveItemVO toSaveItemVO(SalesInvoice salesInvoice, List<SalesInvoiceLine> lines) {
        return toSaveItemVO(salesInvoice, lines, List.of());
    }

    public static SalesInvoiceSaveItemVO toSaveItemVO(SalesInvoice salesInvoice,
                                                       List<SalesInvoiceLine> lines,
                                                       List<SalesInvoiceLineSource> sources) {
        SalesInvoiceSaveItemVO vo = new SalesInvoiceSaveItemVO();
        if (salesInvoice == null) {
            return vo;
        }
        SalesInvoiceMainDTO main = new SalesInvoiceMainDTO();
        main.setId(salesInvoice.getId());
        main.setCorpid(salesInvoice.getCorpid());
        main.setInvoiceNo(salesInvoice.getInvoiceNo());
        main.setCustomerId(salesInvoice.getCustomerId());
        main.setInvoiceDate(salesInvoice.getInvoiceDate());
        main.setDueDate(salesInvoice.getDueDate());
        main.setPaymentTerm(salesInvoice.getPaymentTerm());
        main.setUntaxedAmount(salesInvoice.getUntaxedAmount());
        main.setTaxAmount(salesInvoice.getTaxAmount());
        main.setAmount(salesInvoice.getAmount());
        main.setInvoiceType(salesInvoice.getInvoiceType());
        main.setAuditStatus(salesInvoice.getAuditStatus());
        main.setAuditTime(salesInvoice.getAuditTime());
        main.setPostedTime(salesInvoice.getPostedTime());
        main.setRemark(salesInvoice.getRemark());
        main.setCreatorId(salesInvoice.getCreatorId());
        main.setModifyId(salesInvoice.getModifyId());
        vo.setMain(main);
        Map<Long, SalesInvoiceLineSource> sourceByInvoiceLineId = sources.stream()
            .collect(Collectors.toMap(SalesInvoiceLineSource::getSalesInvoiceLineId,
                Function.identity(), (first, ignored) -> first));
        vo.setLines(lines.stream()
            .map(line -> toLineDTO(line, sourceByInvoiceLineId.get(line.getId())))
            .toList());
        return vo;
    }

    public static SalesInvoiceLine toLine(SalesInvoiceLineDTO dto, String corpid, Long invoiceId, int lineNo, String userId) {
        SalesInvoiceLine line = new SalesInvoiceLine();
        line.setId(dto.getId());
        line.setCorpid(corpid);
        line.setSalesInvoiceId(invoiceId);
        line.setLineNo(lineNo);
        line.setProductId(dto.getProductId());
        line.setProductName(dto.getProductName());
        line.setSpecification(dto.getSpecification());
        line.setUnitId(dto.getUnitId());
        line.setQuantity(dto.getQuantity());
        line.setUnitPrice(dto.getUnitPrice());
        line.setTaxRate(dto.getTaxRate());
        line.setUntaxedAmount(dto.getUntaxedAmount());
        line.setTaxAmount(dto.getTaxAmount());
        line.setAmount(dto.getAmount());
        line.setRemark(dto.getRemark());
        line.setCreatorId(userId);
        line.setModifyId(userId);
        return line;
    }

    private static SalesInvoiceLineDTO toLineDTO(SalesInvoiceLine line, SalesInvoiceLineSource source) {
        SalesInvoiceLineDTO dto = new SalesInvoiceLineDTO();
        dto.setId(line.getId());
        dto.setProductId(line.getProductId());
        dto.setProductName(line.getProductName());
        dto.setSpecification(line.getSpecification());
        dto.setUnitId(line.getUnitId());
        dto.setQuantity(line.getQuantity());
        dto.setUnitPrice(line.getUnitPrice());
        dto.setTaxRate(line.getTaxRate());
        dto.setUntaxedAmount(line.getUntaxedAmount());
        dto.setTaxAmount(line.getTaxAmount());
        dto.setAmount(line.getAmount());
        dto.setRemark(line.getRemark());
        if (source != null) {
            dto.setSourceType(source.getSourceType());
            dto.setSourceId(source.getSourceId());
            dto.setSourceLineId(source.getSourceLineId());
        }
        return dto;
    }

    public static SalesInvoiceDetailVO toDetailVO(SalesInvoiceSaveItemVO saveItemVO) {
        SalesInvoiceDetailVO detailVO = new SalesInvoiceDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }
}
