package xbb.ai.erp.module.sales.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import xbb.ai.erp.base.persistence.entity.BaseEntity;
import xbb.ai.erp.module.sales.domain.model.SalesInvoiceLine;
import xbb.ai.erp.module.sales.domain.repository.SalesInvoiceLineRepository;
import xbb.ai.erp.module.sales.infrastructure.persistence.convertor.SalesInvoiceLineConvertor;
import xbb.ai.erp.module.sales.infrastructure.persistence.mapper.SalesInvoiceLineMapper;
import xbb.ai.erp.module.sales.infrastructure.persistence.po.SalesInvoiceLinePO;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository("xbbAiErpModuleSalesSalesInvoiceLineRepositoryImpl")
@RequiredArgsConstructor
public class SalesInvoiceLineRepositoryImpl implements SalesInvoiceLineRepository {

    private final SalesInvoiceLineMapper salesInvoiceLineMapper;

    @Override
    public Long insert(SalesInvoiceLine salesInvoiceLine) {
        SalesInvoiceLinePO po = SalesInvoiceLineConvertor.toPO(salesInvoiceLine);
        initializeForInsert(po);
        salesInvoiceLineMapper.insert(po);
        salesInvoiceLine.setId(po.getId());
        return po.getId();
    }

    @Override
    public void insertBatch(List<SalesInvoiceLine> salesInvoiceLineList) {
        if (CollectionUtils.isEmpty(salesInvoiceLineList)) {
            return;
        }
        List<SalesInvoiceLinePO> poList = salesInvoiceLineList.stream().map(SalesInvoiceLineConvertor::toPO).toList();
        poList.forEach(this::initializeForInsert);
        salesInvoiceLineMapper.insertBatch(poList);
        for (int index = 0; index < salesInvoiceLineList.size(); index++) {
            salesInvoiceLineList.get(index).setId(poList.get(index).getId());
        }
    }

    @Override
    public void removeById(String corpid, Long id) {
        salesInvoiceLineMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        salesInvoiceLineMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(SalesInvoiceLine salesInvoiceLine) {
        SalesInvoiceLinePO po = SalesInvoiceLineConvertor.toPO(salesInvoiceLine);
        salesInvoiceLineMapper.update(po);
    }

    @Override
    public SalesInvoiceLine findById(String corpid, Long id) {
        return SalesInvoiceLineConvertor.toDomain(salesInvoiceLineMapper.findById(corpid, id));
    }

    @Override
    public List<SalesInvoiceLine> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return salesInvoiceLineMapper.findByCondition(preparedConditionMap).stream().map(SalesInvoiceLineConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return salesInvoiceLineMapper.count(preparedConditionMap);
    }

    private void initializeForInsert(BaseEntity po) {
        long now = System.currentTimeMillis();
        po.setId(null);
        po.setDel(0);
        po.setAddTime(now);
        po.setUpdateTime(now);
    }
}
