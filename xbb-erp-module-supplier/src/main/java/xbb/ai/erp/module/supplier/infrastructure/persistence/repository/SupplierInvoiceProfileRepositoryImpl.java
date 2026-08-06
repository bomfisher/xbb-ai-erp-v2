package xbb.ai.erp.module.supplier.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.supplier.domain.model.SupplierInvoiceProfile;
import xbb.ai.erp.module.supplier.domain.repository.SupplierInvoiceProfileRepository;
import xbb.ai.erp.module.supplier.infrastructure.persistence.convertor.SupplierInvoiceProfileConvertor;
import xbb.ai.erp.module.supplier.infrastructure.persistence.mapper.SupplierInvoiceProfileMapper;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.SupplierInvoiceProfilePO;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class SupplierInvoiceProfileRepositoryImpl implements SupplierInvoiceProfileRepository {

    private final SupplierInvoiceProfileMapper supplierInvoiceProfileMapper;

    @Override
    public void insert(SupplierInvoiceProfile supplierInvoiceProfile) {
        SupplierInvoiceProfilePO po = SupplierInvoiceProfileConvertor.toPO(supplierInvoiceProfile);
        supplierInvoiceProfileMapper.insert(po);
        supplierInvoiceProfile.setId(po.getId());
    }

    @Override
    public void insertBatch(List<SupplierInvoiceProfile> supplierInvoiceProfileList) {
        supplierInvoiceProfileMapper.insertBatch(supplierInvoiceProfileList.stream().map(SupplierInvoiceProfileConvertor::toPO).toList());
    }

    @Override
    public void removeById(String corpid, Long id) {
        supplierInvoiceProfileMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        supplierInvoiceProfileMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(SupplierInvoiceProfile supplierInvoiceProfile) {
        supplierInvoiceProfileMapper.update(SupplierInvoiceProfileConvertor.toPO(supplierInvoiceProfile));
    }

    @Override
    public SupplierInvoiceProfile findById(String corpid, Long id) {
        return SupplierInvoiceProfileConvertor.toDomain(supplierInvoiceProfileMapper.findById(corpid, id));
    }

    @Override
    public List<SupplierInvoiceProfile> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> normalizedConditionMap = ConditionMapHelper.normalizePage(conditionMap);
        return supplierInvoiceProfileMapper.findByCondition(normalizedConditionMap).stream().map(SupplierInvoiceProfileConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        return supplierInvoiceProfileMapper.count(ConditionMapHelper.normalizePage(conditionMap));
    }
}
