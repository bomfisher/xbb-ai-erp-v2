package xbb.ai.erp.module.settlement.application.service;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.settlement.admin.ReceivableWriteOffStatusEnum;
import xbb.ai.erp.module.settlement.contract.InvoiceReceivableApi;
import xbb.ai.erp.module.settlement.contract.InvoiceReceivableCommand;
import xbb.ai.erp.module.settlement.domain.model.Receivable;
import xbb.ai.erp.module.settlement.domain.repository.ReceivableRepository;
import xbb.ai.erp.module.sales.contract.SalesInvoiceOpenAmountApi;

@Service
@RequiredArgsConstructor
public class InvoiceReceivableService implements InvoiceReceivableApi {
    private final ReceivableRepository receivableRepository;
    private final BizNoGenerator bizNoGenerator;
    private final SalesInvoiceOpenAmountApi salesInvoiceOpenAmountApi;

    @Override
    public void createForInvoice(InvoiceReceivableCommand command) {
        SalesInvoiceOpenAmountApi.SalesInvoiceOpenAmount invoice = salesInvoiceOpenAmountApi
            .findOpenAmount(command.corpid(), command.invoiceId());
        BigDecimal amount = invoice.availableAmount();
        if (amount == null || amount.signum() <= 0) {
            return;
        }
        if (!invoice.customerId().equals(command.customerId())) {
            throw new xbb.ai.erp.base.common.exception.BizException("销售发票客户不一致");
        }
        Receivable receivable = new Receivable();
        receivable.setCorpid(command.corpid());
        receivable.setReceivableNo(bizNoGenerator.next(command.corpid(), BusinessCodeEnum.RECEIVABLE.getCode()));
        receivable.setCustomerId(command.customerId());
        receivable.setSourceType("SALES_INVOICE");
        receivable.setSourceInvoiceId(command.invoiceId());
        receivable.setReceivableDate(command.receivableDate());
        receivable.setDueDate(command.dueDate());
        receivable.setAmount(amount);
        receivable.setWrittenOffAmount(BigDecimal.ZERO);
        receivable.setRemainingAmount(amount);
        receivable.setStatus(ReceivableWriteOffStatusEnum.UNWRITTEN_OFF.getValue());
        receivable.setRemark(command.remark());
        receivable.setCreatorId(command.userId());
        receivable.setModifyId(command.userId());
        receivableRepository.insert(receivable);
        salesInvoiceOpenAmountApi.changeReceivableOpenedAmount(command.corpid(), command.invoiceId(), amount,
            command.userId());
    }
}
