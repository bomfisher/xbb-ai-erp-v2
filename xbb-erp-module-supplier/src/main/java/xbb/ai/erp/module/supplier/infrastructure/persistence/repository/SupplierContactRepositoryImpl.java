package xbb.ai.erp.module.supplier.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.supplier.domain.model.SupplierContact;
import xbb.ai.erp.module.supplier.domain.repository.SupplierContactRepository;
import xbb.ai.erp.module.supplier.infrastructure.persistence.convertor.SupplierContactConvertor;
import xbb.ai.erp.module.supplier.infrastructure.persistence.mapper.SupplierContactMapper;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.SupplierContactPO;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class SupplierContactRepositoryImpl implements SupplierContactRepository {

    private final SupplierContactMapper supplierContactMapper;

    @Override
    public void insert(SupplierContact supplierContact) {
        SupplierContactPO po = SupplierContactConvertor.toPO(supplierContact);
        supplierContactMapper.insert(po);
        supplierContact.setId(po.getId());
    }

    @Override
    public void insertBatch(List<SupplierContact> supplierContactList) {
        supplierContactMapper.insertBatch(supplierContactList.stream().map(SupplierContactConvertor::toPO).toList());
    }

    @Override
    public void removeById(String corpid, Long id) {
        supplierContactMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        supplierContactMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(SupplierContact supplierContact) {
        supplierContactMapper.update(SupplierContactConvertor.toPO(supplierContact));
    }

    @Override
    public SupplierContact findById(String corpid, Long id) {
        return SupplierContactConvertor.toDomain(supplierContactMapper.findById(corpid, id));
    }

    @Override
    public List<SupplierContact> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> normalizedConditionMap = ConditionMapHelper.normalizePage(conditionMap);
        return supplierContactMapper.findByCondition(normalizedConditionMap).stream().map(SupplierContactConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        return supplierContactMapper.count(ConditionMapHelper.normalizePage(conditionMap));
    }
}
