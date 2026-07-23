package xbb.ai.erp.module.supplier.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.supplier.domain.model.Vendor;
import xbb.ai.erp.module.supplier.domain.repository.VendorRepository;
import xbb.ai.erp.module.supplier.infrastructure.persistence.convertor.VendorConvertor;
import xbb.ai.erp.module.supplier.infrastructure.persistence.mapper.VendorMapper;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.VendorPO;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class VendorRepositoryImpl implements VendorRepository {

    private final VendorMapper vendorMapper;

    @Override
    public void insert(Vendor vendor) {
        vendorMapper.insert(VendorConvertor.toPO(vendor));
    }

    @Override
    public void insertBatch(List<Vendor> vendorList) {
        vendorMapper.insertBatch(vendorList.stream().map(VendorConvertor::toPO).toList());
    }

    @Override
    public void removeById(String corpid, Long id) {
        vendorMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        vendorMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(Vendor vendor) {
        VendorPO po = VendorConvertor.toPO(vendor);
        vendorMapper.update(po);
    }

    @Override
    public Vendor findById(String corpid, Long id) {
        return VendorConvertor.toDomain(vendorMapper.findById(corpid, id));
    }

    @Override
    public List<Vendor> findByCondition(Map<String, Object> conditionMap) {
        return vendorMapper.findByCondition(conditionMap).stream().map(VendorConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        return vendorMapper.count(conditionMap);
    }
}
