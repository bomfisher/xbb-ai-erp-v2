package xbb.ai.erp.module.purchase.application.service;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.enums.AuditStatusEnum;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.purchase.contract.PurchaseInvoiceOpenAmountApi;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInvoice;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInvoiceRepository;
import xbb.ai.erp.module.purchase.infrastructure.persistence.mapper.PurchaseInvoiceMapper;

@Service
@RequiredArgsConstructor
public class PurchaseInvoiceOpenAmountService implements PurchaseInvoiceOpenAmountApi {

    private final PurchaseInvoiceRepository purchaseInvoiceRepository;
    private final PurchaseInvoiceMapper purchaseInvoiceMapper;

    @Override
    public PurchaseInvoiceOpenAmount findOpenAmount(String corpid, Long invoiceId) {
        PurchaseInvoice invoice = requirePostedInvoice(corpid, invoiceId);
        return new PurchaseInvoiceOpenAmount(invoice.getId(), invoice.getSupplierId(), invoice.getAmount(),
            invoice.getPayableOpenedAmount(), invoice.getPayableAvailableAmount(),
            invoice.getStatus(), invoice.getAuditStatus());
    }

    @Override
    public void changePayableOpenedAmount(String corpid, Long invoiceId, BigDecimal delta, String userId) {
        if (delta == null || delta.signum() == 0) {
            return;
        }
        requirePostedInvoice(corpid, invoiceId);
        if (purchaseInvoiceMapper.changePayableOpenedAmount(corpid, invoiceId, delta, userId) != 1) {
            throw new BizException("采购发票可开应付金额不足");
        }
    }

    private PurchaseInvoice requirePostedInvoice(String corpid, Long invoiceId) {
        PurchaseInvoice invoice = purchaseInvoiceRepository.findById(corpid, invoiceId);
        if (invoice == null || !"POSTED".equals(invoice.getStatus())
            || !AuditStatusEnum.allowsDownstream(invoice.getAuditStatus())) {
            throw new BizException("来源采购发票不存在或未过账");
        }
        return invoice;
    }
}
