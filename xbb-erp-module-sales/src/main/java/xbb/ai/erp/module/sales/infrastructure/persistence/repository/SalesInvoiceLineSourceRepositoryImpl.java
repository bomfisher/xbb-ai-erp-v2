package xbb.ai.erp.module.sales.infrastructure.persistence.repository;

import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.persistence.entity.BaseEntity;
import xbb.ai.erp.module.sales.domain.model.SalesInvoiceLineSource;
import xbb.ai.erp.module.sales.domain.repository.SalesInvoiceLineSourceRepository;
import xbb.ai.erp.module.sales.infrastructure.persistence.convertor.SalesInvoiceLineSourceConvertor;
import xbb.ai.erp.module.sales.infrastructure.persistence.mapper.SalesInvoiceLineSourceMapper;
import xbb.ai.erp.module.sales.infrastructure.persistence.po.SalesInvoiceLineSourcePO;

@Repository
@RequiredArgsConstructor
public class SalesInvoiceLineSourceRepositoryImpl implements SalesInvoiceLineSourceRepository {
    private final SalesInvoiceLineSourceMapper mapper;

    @Override
    public void insertBatch(List<SalesInvoiceLineSource> sources) {
        if (sources.isEmpty()) {
            return;
        }
        List<SalesInvoiceLineSourcePO> pos = sources.stream().map(SalesInvoiceLineSourceConvertor::toPO).toList();
        pos.forEach(this::initializeForInsert);
        mapper.insertBatch(pos);
    }

    @Override
    public void removeByInvoiceLineIds(String corpid, List<Long> invoiceLineIds) {
        if (!invoiceLineIds.isEmpty()) {
            mapper.removeByInvoiceLineIds(corpid, invoiceLineIds);
        }
    }

    @Override
    public List<SalesInvoiceLineSource> findByInvoiceLineIds(String corpid, List<Long> invoiceLineIds) {
        if (invoiceLineIds.isEmpty()) {
            return List.of();
        }
        return mapper.findByInvoiceLineIds(corpid, invoiceLineIds).stream()
            .map(SalesInvoiceLineSourceConvertor::toDomain)
            .toList();
    }

    @Override
    public List<SalesInvoiceLineSource> findByCondition(Map<String, Object> conditionMap) {
        return mapper.findByCondition(conditionMap).stream().map(SalesInvoiceLineSourceConvertor::toDomain).toList();
    }

    @Override
    public BigDecimal sumPostedQuantity(String corpid, String sourceType, Long sourceLineId,
                                        Long excludedInvoiceId) {
        BigDecimal quantity = mapper.sumPostedQuantity(corpid, sourceType, sourceLineId, excludedInvoiceId);
        return quantity == null ? BigDecimal.ZERO : quantity;
    }

    @Override
    public BigDecimal sumPostedQuantityBySalesOrderItem(String corpid, Long salesOrderItemId,
                                                        Long excludedInvoiceId) {
        BigDecimal quantity = mapper.sumPostedQuantityBySalesOrderItem(corpid, salesOrderItemId,
            excludedInvoiceId);
        return quantity == null ? BigDecimal.ZERO : quantity;
    }

    private void initializeForInsert(BaseEntity po) {
        long now = System.currentTimeMillis();
        po.setId(null);
        po.setDel(0);
        po.setAddTime(now);
        po.setUpdateTime(now);
    }
}
