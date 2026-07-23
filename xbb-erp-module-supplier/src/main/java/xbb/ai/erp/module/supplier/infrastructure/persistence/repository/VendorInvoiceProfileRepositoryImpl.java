package xbb.ai.erp.module.supplier.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.supplier.domain.model.VendorInvoiceProfile;
import xbb.ai.erp.module.supplier.domain.repository.VendorInvoiceProfileRepository;
import xbb.ai.erp.module.supplier.infrastructure.persistence.convertor.VendorInvoiceProfileConvertor;
import xbb.ai.erp.module.supplier.infrastructure.persistence.mapper.VendorInvoiceProfileMapper;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.VendorInvoiceProfilePO;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class VendorInvoiceProfileRepositoryImpl implements VendorInvoiceProfileRepository {

    private final VendorInvoiceProfileMapper vendorInvoiceProfileMapper;

    @Override
    public void insert(VendorInvoiceProfile vendorInvoiceProfile) {
        vendorInvoiceProfileMapper.insert(VendorInvoiceProfileConvertor.toPO(vendorInvoiceProfile));
    }

    @Override
    public void insertBatch(List<VendorInvoiceProfile> vendorInvoiceProfileList) {
        vendorInvoiceProfileMapper.insertBatch(vendorInvoiceProfileList.stream().map(VendorInvoiceProfileConvertor::toPO).toList());
    }

    @Override
    public void removeById(String corpid, Long id) {
        vendorInvoiceProfileMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        vendorInvoiceProfileMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(VendorInvoiceProfile vendorInvoiceProfile) {
        VendorInvoiceProfilePO po = VendorInvoiceProfileConvertor.toPO(vendorInvoiceProfile);
        vendorInvoiceProfileMapper.update(po);
    }

    @Override
    public VendorInvoiceProfile findById(String corpid, Long id) {
        return VendorInvoiceProfileConvertor.toDomain(vendorInvoiceProfileMapper.findById(corpid, id));
    }

    @Override
    public List<VendorInvoiceProfile> findByCondition(Map<String, Object> conditionMap) {
        return vendorInvoiceProfileMapper.findByCondition(conditionMap).stream().map(VendorInvoiceProfileConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        return vendorInvoiceProfileMapper.count(conditionMap);
    }
}
