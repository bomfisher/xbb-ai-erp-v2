package xbb.ai.erp.module.settlement.contract;

public interface InvoiceReceivableApi {
    void createForInvoice(InvoiceReceivableCommand command);
}
