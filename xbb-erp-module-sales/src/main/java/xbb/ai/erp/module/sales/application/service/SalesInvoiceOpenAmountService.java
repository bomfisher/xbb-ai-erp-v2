package xbb.ai.erp.module.sales.application.service;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.enums.AuditStatusEnum;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.sales.contract.SalesInvoiceOpenAmountApi;
import xbb.ai.erp.module.sales.domain.model.SalesInvoice;
import xbb.ai.erp.module.sales.domain.repository.SalesInvoiceRepository;
import xbb.ai.erp.module.sales.infrastructure.persistence.mapper.SalesInvoiceMapper;

@Service
@RequiredArgsConstructor
public class SalesInvoiceOpenAmountService implements SalesInvoiceOpenAmountApi {

    private final SalesInvoiceRepository salesInvoiceRepository;
    private final SalesInvoiceMapper salesInvoiceMapper;

    @Override
    public SalesInvoiceOpenAmount findOpenAmount(String corpid, Long invoiceId) {
        SalesInvoice invoice = requirePostedInvoice(corpid, invoiceId);
        return new SalesInvoiceOpenAmount(invoice.getId(), invoice.getCustomerId(), invoice.getAmount(),
            invoice.getReceivableOpenedAmount(), invoice.getReceivableAvailableAmount(),
            invoice.getStatus(), invoice.getAuditStatus());
    }

    @Override
    public void changeReceivableOpenedAmount(String corpid, Long invoiceId, BigDecimal delta, String userId) {
        if (delta == null || delta.signum() == 0) {
            return;
        }
        requirePostedInvoice(corpid, invoiceId);
        if (salesInvoiceMapper.changeReceivableOpenedAmount(corpid, invoiceId, delta, userId) != 1) {
            throw new BizException("销售发票可开应收金额不足");
        }
    }

    private SalesInvoice requirePostedInvoice(String corpid, Long invoiceId) {
        SalesInvoice invoice = salesInvoiceRepository.findById(corpid, invoiceId);
        if (invoice == null || !"POSTED".equals(invoice.getStatus())
            || !AuditStatusEnum.allowsDownstream(invoice.getAuditStatus())) {
            throw new BizException("来源销售发票不存在或未过账");
        }
        return invoice;
    }
}
