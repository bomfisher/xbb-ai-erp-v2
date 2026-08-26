package xbb.ai.erp.module.sales.infrastructure.persistence.convertor;

import xbb.ai.erp.module.sales.domain.model.SalesInvoiceLineSource;
import xbb.ai.erp.module.sales.infrastructure.persistence.po.SalesInvoiceLineSourcePO;

public final class SalesInvoiceLineSourceConvertor {
    private SalesInvoiceLineSourceConvertor() {
    }

    public static SalesInvoiceLineSourcePO toPO(SalesInvoiceLineSource source) {
        SalesInvoiceLineSourcePO po = new SalesInvoiceLineSourcePO();
        po.setId(source.getId());
        po.setCorpid(source.getCorpid());
        po.setSalesInvoiceLineId(source.getSalesInvoiceLineId());
        po.setSourceType(source.getSourceType());
        po.setSourceId(source.getSourceId());
        po.setSourceLineId(source.getSourceLineId());
        po.setQuantity(source.getQuantity());
        po.setUntaxedAmount(source.getUntaxedAmount());
        po.setTaxAmount(source.getTaxAmount());
        po.setAmount(source.getAmount());
        po.setCreatorId(source.getCreatorId());
        po.setModifyId(source.getModifyId());
        return po;
    }

    public static SalesInvoiceLineSource toDomain(SalesInvoiceLineSourcePO po) {
        SalesInvoiceLineSource source = new SalesInvoiceLineSource();
        source.setId(po.getId());
        source.setCorpid(po.getCorpid());
        source.setSalesInvoiceLineId(po.getSalesInvoiceLineId());
        source.setSourceType(po.getSourceType());
        source.setSourceId(po.getSourceId());
        source.setSourceLineId(po.getSourceLineId());
        source.setQuantity(po.getQuantity());
        source.setUntaxedAmount(po.getUntaxedAmount());
        source.setTaxAmount(po.getTaxAmount());
        source.setAmount(po.getAmount());
        source.setCreatorId(po.getCreatorId());
        source.setModifyId(po.getModifyId());
        return source;
    }
}
