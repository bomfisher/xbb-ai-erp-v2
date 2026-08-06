package xbb.ai.erp.module.supplier.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.supplier.domain.model.SupplierAddress;
import xbb.ai.erp.module.supplier.domain.repository.SupplierAddressRepository;
import xbb.ai.erp.module.supplier.infrastructure.persistence.convertor.SupplierAddressConvertor;
import xbb.ai.erp.module.supplier.infrastructure.persistence.mapper.SupplierAddressMapper;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.SupplierAddressPO;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class SupplierAddressRepositoryImpl implements SupplierAddressRepository {

    private final SupplierAddressMapper supplierAddressMapper;

    @Override
    public void insert(SupplierAddress supplierAddress) {
        SupplierAddressPO po = SupplierAddressConvertor.toPO(supplierAddress);
        supplierAddressMapper.insert(po);
        supplierAddress.setId(po.getId());
    }

    @Override
    public void insertBatch(List<SupplierAddress> supplierAddressList) {
        supplierAddressMapper.insertBatch(supplierAddressList.stream().map(SupplierAddressConvertor::toPO).toList());
    }

    @Override
    public void removeById(String corpid, Long id) {
        supplierAddressMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        supplierAddressMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(SupplierAddress supplierAddress) {
        supplierAddressMapper.update(SupplierAddressConvertor.toPO(supplierAddress));
    }

    @Override
    public SupplierAddress findById(String corpid, Long id) {
        return SupplierAddressConvertor.toDomain(supplierAddressMapper.findById(corpid, id));
    }

    @Override
    public List<SupplierAddress> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> normalizedConditionMap = ConditionMapHelper.normalizePage(conditionMap);
        return supplierAddressMapper.findByCondition(normalizedConditionMap).stream().map(SupplierAddressConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        return supplierAddressMapper.count(ConditionMapHelper.normalizePage(conditionMap));
    }
}
