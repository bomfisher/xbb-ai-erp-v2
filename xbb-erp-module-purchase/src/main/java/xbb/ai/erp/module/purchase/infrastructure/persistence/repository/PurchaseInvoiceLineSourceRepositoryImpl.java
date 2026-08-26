package xbb.ai.erp.module.purchase.infrastructure.persistence.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.persistence.entity.BaseEntity;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInvoiceLineSource;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInvoiceLineSourceRepository;
import xbb.ai.erp.module.purchase.infrastructure.persistence.mapper.PurchaseInvoiceLineSourceMapper;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseInvoiceLineSourcePO;

@Repository
@RequiredArgsConstructor
public class PurchaseInvoiceLineSourceRepositoryImpl implements PurchaseInvoiceLineSourceRepository {
    private final PurchaseInvoiceLineSourceMapper mapper;
    @Override
    public void insertBatch(List<PurchaseInvoiceLineSource> sources) {
        if (sources == null || sources.isEmpty()) {
            return;
        }
        List<PurchaseInvoiceLineSourcePO> pos = sources.stream().map(this::toPO).toList();
        pos.forEach(this::initializeForInsert);
        mapper.insertBatch(pos);
        for (int index = 0; index < sources.size(); index++) {
            sources.get(index).setId(pos.get(index).getId());
        }
    }

    @Override
    public void removeByInvoiceLineIds(String corpid, List<Long> lineIds) {
        if (lineIds != null && !lineIds.isEmpty()) {
            mapper.removeByInvoiceLineIds(corpid, lineIds);
        }
    }

    @Override
    public List<PurchaseInvoiceLineSource> findByInvoiceLineIds(String corpid, List<Long> lineIds) {
        if (lineIds == null || lineIds.isEmpty()) {
            return List.of();
        }
        return mapper.findByInvoiceLineIds(corpid, lineIds).stream().map(this::toDomain).toList();
    }

    @Override
    public java.math.BigDecimal sumInvoiceQuantityByPurchaseOrderItem(String corpid, Long purchaseOrderItemId) {
        java.math.BigDecimal quantity = mapper.sumInvoiceQuantityByPurchaseOrderItem(corpid, purchaseOrderItemId);
        return quantity == null ? java.math.BigDecimal.ZERO : quantity;
    }

    private PurchaseInvoiceLineSourcePO toPO(PurchaseInvoiceLineSource source) {
        PurchaseInvoiceLineSourcePO po = new PurchaseInvoiceLineSourcePO();
        po.setCorpid(source.getCorpid());
        po.setPurchaseInvoiceLineId(source.getPurchaseInvoiceLineId());
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

    private PurchaseInvoiceLineSource toDomain(PurchaseInvoiceLineSourcePO po) {
        PurchaseInvoiceLineSource source = new PurchaseInvoiceLineSource();
        source.setId(po.getId());
        source.setCorpid(po.getCorpid());
        source.setPurchaseInvoiceLineId(po.getPurchaseInvoiceLineId());
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

    private void initializeForInsert(BaseEntity po) {
        long now = System.currentTimeMillis();
        po.setId(null);
        po.setDel(0);
        po.setAddTime(now);
        po.setUpdateTime(now);
    }
}
