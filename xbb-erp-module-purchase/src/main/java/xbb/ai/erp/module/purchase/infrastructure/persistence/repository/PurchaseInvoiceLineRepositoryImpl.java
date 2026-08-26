package xbb.ai.erp.module.purchase.infrastructure.persistence.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.persistence.entity.BaseEntity;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInvoiceLine;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInvoiceLineRepository;
import xbb.ai.erp.module.purchase.infrastructure.persistence.mapper.PurchaseInvoiceLineMapper;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseInvoiceLinePO;

@Repository
@RequiredArgsConstructor
public class PurchaseInvoiceLineRepositoryImpl implements PurchaseInvoiceLineRepository {
    private final PurchaseInvoiceLineMapper mapper;

    @Override
    public void insertBatch(List<PurchaseInvoiceLine> lines) {
        if (lines == null || lines.isEmpty()) {
            return;
        }
        List<PurchaseInvoiceLinePO> pos = lines.stream().map(this::toPO).toList();
        pos.forEach(this::initializeForInsert);
        mapper.insertBatch(pos);
        for (int index = 0; index < lines.size(); index++) {
            lines.get(index).setId(pos.get(index).getId());
        }
    }

    @Override
    public void removeByInvoiceId(String corpid, Long invoiceId) {
        mapper.removeByInvoiceId(corpid, invoiceId);
    }

    @Override
    public List<PurchaseInvoiceLine> findByInvoiceId(String corpid, Long invoiceId) {
        return mapper.findByInvoiceId(corpid, invoiceId).stream().map(this::toDomain).toList();
    }

    private PurchaseInvoiceLinePO toPO(PurchaseInvoiceLine line) {
        PurchaseInvoiceLinePO po = new PurchaseInvoiceLinePO();
        po.setCorpid(line.getCorpid());
        po.setPurchaseInvoiceId(line.getPurchaseInvoiceId());
        po.setLineNo(line.getLineNo());
        po.setProductId(line.getProductId());
        po.setProductName(line.getProductName());
        po.setSpecification(line.getSpecification());
        po.setUnitName(line.getUnitName());
        po.setQuantity(line.getQuantity());
        po.setUnitPrice(line.getUnitPrice());
        po.setTaxRate(line.getTaxRate());
        po.setUntaxedAmount(line.getUntaxedAmount());
        po.setTaxAmount(line.getTaxAmount());
        po.setAmount(line.getAmount());
        po.setRemark(line.getRemark());
        po.setCreatorId(line.getCreatorId());
        po.setModifyId(line.getModifyId());
        return po;
    }

    private PurchaseInvoiceLine toDomain(PurchaseInvoiceLinePO po) {
        PurchaseInvoiceLine line = new PurchaseInvoiceLine();
        line.setId(po.getId());
        line.setCorpid(po.getCorpid());
        line.setPurchaseInvoiceId(po.getPurchaseInvoiceId());
        line.setLineNo(po.getLineNo());
        line.setProductId(po.getProductId());
        line.setProductName(po.getProductName());
        line.setSpecification(po.getSpecification());
        line.setUnitName(po.getUnitName());
        line.setQuantity(po.getQuantity());
        line.setUnitPrice(po.getUnitPrice());
        line.setTaxRate(po.getTaxRate());
        line.setUntaxedAmount(po.getUntaxedAmount());
        line.setTaxAmount(po.getTaxAmount());
        line.setAmount(po.getAmount());
        line.setRemark(po.getRemark());
        line.setCreatorId(po.getCreatorId());
        line.setModifyId(po.getModifyId());
        return line;
    }

    private void initializeForInsert(BaseEntity po) {
        long now = System.currentTimeMillis();
        po.setId(null);
        po.setDel(0);
        po.setAddTime(now);
        po.setUpdateTime(now);
    }
}
