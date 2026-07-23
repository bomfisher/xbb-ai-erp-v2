package xbb.ai.erp.module.supplier.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.supplier.domain.model.VendorAddress;
import xbb.ai.erp.module.supplier.domain.repository.VendorAddressRepository;
import xbb.ai.erp.module.supplier.infrastructure.persistence.convertor.VendorAddressConvertor;
import xbb.ai.erp.module.supplier.infrastructure.persistence.mapper.VendorAddressMapper;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.VendorAddressPO;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class VendorAddressRepositoryImpl implements VendorAddressRepository {

    private final VendorAddressMapper vendorAddressMapper;

    @Override
    public void insert(VendorAddress vendorAddress) {
        vendorAddressMapper.insert(VendorAddressConvertor.toPO(vendorAddress));
    }

    @Override
    public void insertBatch(List<VendorAddress> vendorAddressList) {
        vendorAddressMapper.insertBatch(vendorAddressList.stream().map(VendorAddressConvertor::toPO).toList());
    }

    @Override
    public void removeById(String corpid, Long id) {
        vendorAddressMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        vendorAddressMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(VendorAddress vendorAddress) {
        VendorAddressPO po = VendorAddressConvertor.toPO(vendorAddress);
        vendorAddressMapper.update(po);
    }

    @Override
    public VendorAddress findById(String corpid, Long id) {
        return VendorAddressConvertor.toDomain(vendorAddressMapper.findById(corpid, id));
    }

    @Override
    public List<VendorAddress> findByCondition(Map<String, Object> conditionMap) {
        return vendorAddressMapper.findByCondition(conditionMap).stream().map(VendorAddressConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        return vendorAddressMapper.count(conditionMap);
    }
}
