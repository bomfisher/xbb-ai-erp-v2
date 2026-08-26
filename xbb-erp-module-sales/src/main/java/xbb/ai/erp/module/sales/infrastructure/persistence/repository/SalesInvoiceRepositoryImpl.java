package xbb.ai.erp.module.sales.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.persistence.entity.BaseEntity;
import xbb.ai.erp.module.sales.domain.model.SalesInvoice;
import xbb.ai.erp.module.sales.domain.repository.SalesInvoiceRepository;
import xbb.ai.erp.module.sales.infrastructure.persistence.convertor.SalesInvoiceConvertor;
import xbb.ai.erp.module.sales.infrastructure.persistence.mapper.SalesInvoiceMapper;
import xbb.ai.erp.module.sales.infrastructure.persistence.po.SalesInvoicePO;

import java.util.List;
import java.util.Map;

@Repository("xbbAiErpModuleSalesSalesInvoiceRepositoryImpl")
@RequiredArgsConstructor
public class SalesInvoiceRepositoryImpl implements SalesInvoiceRepository {

    private final SalesInvoiceMapper salesInvoiceMapper;

    @Override
    public Long insert(SalesInvoice salesInvoice) {
        SalesInvoicePO po = SalesInvoiceConvertor.toPO(salesInvoice);
        initializeForInsert(po);
        salesInvoiceMapper.insert(po);
        salesInvoice.setId(po.getId());
        return po.getId();
    }

    @Override
    public void insertBatch(List<SalesInvoice> salesInvoiceList) {
        List<SalesInvoicePO> poList = salesInvoiceList.stream().map(SalesInvoiceConvertor::toPO).toList();
        poList.forEach(this::initializeForInsert);
        salesInvoiceMapper.insertBatch(poList);
        for (int index = 0; index < salesInvoiceList.size(); index++) {
            salesInvoiceList.get(index).setId(poList.get(index).getId());
        }
    }

    @Override
    public void removeById(String corpid, Long id) {
        salesInvoiceMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        salesInvoiceMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(SalesInvoice salesInvoice) {
        SalesInvoicePO po = SalesInvoiceConvertor.toPO(salesInvoice);
        salesInvoiceMapper.update(po);
    }

    @Override
    public SalesInvoice findById(String corpid, Long id) {
        return SalesInvoiceConvertor.toDomain(salesInvoiceMapper.findById(corpid, id));
    }

    @Override
    public List<SalesInvoice> findByIds(String corpid, java.util.Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return salesInvoiceMapper.findByIds(corpid, ids).stream().map(SalesInvoiceConvertor::toDomain).toList();
    }

    @Override
    public List<SalesInvoice> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return salesInvoiceMapper.findByCondition(preparedConditionMap).stream().map(SalesInvoiceConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return salesInvoiceMapper.count(preparedConditionMap);
    }

    private void initializeForInsert(BaseEntity po) {
        long now = System.currentTimeMillis();
        po.setId(null);
        po.setDel(0);
        po.setAddTime(now);
        po.setUpdateTime(now);
    }
}
