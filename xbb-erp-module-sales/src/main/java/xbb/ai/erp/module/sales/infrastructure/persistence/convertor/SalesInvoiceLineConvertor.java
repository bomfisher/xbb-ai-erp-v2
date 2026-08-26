package xbb.ai.erp.module.sales.infrastructure.persistence.convertor;

import xbb.ai.erp.module.sales.domain.model.SalesInvoiceLine;
import xbb.ai.erp.module.sales.infrastructure.persistence.po.SalesInvoiceLinePO;

public final class SalesInvoiceLineConvertor {

    private SalesInvoiceLineConvertor() {
    }

    public static SalesInvoiceLinePO toPO(SalesInvoiceLine salesInvoiceLine) {
        if (salesInvoiceLine == null) {
            return null;
        }
        SalesInvoiceLinePO po = new SalesInvoiceLinePO();
        po.setId(salesInvoiceLine.getId());
        po.setCorpid(salesInvoiceLine.getCorpid());
        po.setSalesInvoiceId(salesInvoiceLine.getSalesInvoiceId());
        po.setLineNo(salesInvoiceLine.getLineNo());
        po.setProductId(salesInvoiceLine.getProductId());
        po.setProductName(salesInvoiceLine.getProductName());
        po.setSpecification(salesInvoiceLine.getSpecification());
        po.setUnitId(salesInvoiceLine.getUnitId());
        po.setQuantity(salesInvoiceLine.getQuantity());
        po.setUnitPrice(salesInvoiceLine.getUnitPrice());
        po.setTaxRate(salesInvoiceLine.getTaxRate());
        po.setUntaxedAmount(salesInvoiceLine.getUntaxedAmount());
        po.setTaxAmount(salesInvoiceLine.getTaxAmount());
        po.setAmount(salesInvoiceLine.getAmount());
        po.setRemark(salesInvoiceLine.getRemark());
        po.setCreatorId(salesInvoiceLine.getCreatorId());
        po.setModifyId(salesInvoiceLine.getModifyId());
        return po;
    }

    public static SalesInvoiceLine toDomain(SalesInvoiceLinePO po) {
        if (po == null) {
            return null;
        }
        SalesInvoiceLine salesInvoiceLine = new SalesInvoiceLine();
        salesInvoiceLine.setId(po.getId());
        salesInvoiceLine.setCorpid(po.getCorpid());
        salesInvoiceLine.setSalesInvoiceId(po.getSalesInvoiceId());
        salesInvoiceLine.setLineNo(po.getLineNo());
        salesInvoiceLine.setProductId(po.getProductId());
        salesInvoiceLine.setProductName(po.getProductName());
        salesInvoiceLine.setSpecification(po.getSpecification());
        salesInvoiceLine.setUnitId(po.getUnitId());
        salesInvoiceLine.setQuantity(po.getQuantity());
        salesInvoiceLine.setUnitPrice(po.getUnitPrice());
        salesInvoiceLine.setTaxRate(po.getTaxRate());
        salesInvoiceLine.setUntaxedAmount(po.getUntaxedAmount());
        salesInvoiceLine.setTaxAmount(po.getTaxAmount());
        salesInvoiceLine.setAmount(po.getAmount());
        salesInvoiceLine.setRemark(po.getRemark());
        salesInvoiceLine.setCreatorId(po.getCreatorId());
        salesInvoiceLine.setModifyId(po.getModifyId());
        return salesInvoiceLine;
    }
}
