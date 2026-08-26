package xbb.ai.erp.module.purchase.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.persistence.entity.BaseEntity;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInvoice;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInvoiceRepository;
import xbb.ai.erp.module.purchase.infrastructure.persistence.convertor.PurchaseInvoiceConvertor;
import xbb.ai.erp.module.purchase.infrastructure.persistence.mapper.PurchaseInvoiceMapper;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseInvoicePO;

import java.util.List;
import java.util.Map;

@Repository("xbbAiErpModulePurchasePurchaseInvoiceRepositoryImpl")
@RequiredArgsConstructor
public class PurchaseInvoiceRepositoryImpl implements PurchaseInvoiceRepository {

    private final PurchaseInvoiceMapper purchaseInvoiceMapper;

    @Override
    public Long insert(PurchaseInvoice purchaseInvoice) {
        PurchaseInvoicePO po = PurchaseInvoiceConvertor.toPO(purchaseInvoice);
        initializeForInsert(po);
        purchaseInvoiceMapper.insert(po);
        purchaseInvoice.setId(po.getId());
        return po.getId();
    }

    @Override
    public void insertBatch(List<PurchaseInvoice> purchaseInvoiceList) {
        List<PurchaseInvoicePO> poList = purchaseInvoiceList.stream().map(PurchaseInvoiceConvertor::toPO).toList();
        poList.forEach(this::initializeForInsert);
        purchaseInvoiceMapper.insertBatch(poList);
        for (int index = 0; index < purchaseInvoiceList.size(); index++) {
            purchaseInvoiceList.get(index).setId(poList.get(index).getId());
        }
    }

    @Override
    public void removeById(String corpid, Long id) {
        purchaseInvoiceMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        purchaseInvoiceMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(PurchaseInvoice purchaseInvoice) {
        PurchaseInvoicePO po = PurchaseInvoiceConvertor.toPO(purchaseInvoice);
        purchaseInvoiceMapper.update(po);
    }

    @Override
    public PurchaseInvoice findById(String corpid, Long id) {
        return PurchaseInvoiceConvertor.toDomain(purchaseInvoiceMapper.findById(corpid, id));
    }

    @Override
    public List<PurchaseInvoice> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return purchaseInvoiceMapper.findByCondition(preparedConditionMap).stream().map(PurchaseInvoiceConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return purchaseInvoiceMapper.count(preparedConditionMap);
    }

    private void initializeForInsert(BaseEntity po) {
        long now = System.currentTimeMillis();
        po.setId(null);
        po.setDel(0);
        po.setAddTime(now);
        po.setUpdateTime(now);
    }
}
