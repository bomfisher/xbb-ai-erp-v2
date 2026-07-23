package xbb.ai.erp.module.supplier.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.supplier.domain.model.VendorContact;
import xbb.ai.erp.module.supplier.domain.repository.VendorContactRepository;
import xbb.ai.erp.module.supplier.infrastructure.persistence.convertor.VendorContactConvertor;
import xbb.ai.erp.module.supplier.infrastructure.persistence.mapper.VendorContactMapper;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.VendorContactPO;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class VendorContactRepositoryImpl implements VendorContactRepository {

    private final VendorContactMapper vendorContactMapper;

    @Override
    public void insert(VendorContact vendorContact) {
        vendorContactMapper.insert(VendorContactConvertor.toPO(vendorContact));
    }

    @Override
    public void insertBatch(List<VendorContact> vendorContactList) {
        vendorContactMapper.insertBatch(vendorContactList.stream().map(VendorContactConvertor::toPO).toList());
    }

    @Override
    public void removeById(String corpid, Long id) {
        vendorContactMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        vendorContactMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(VendorContact vendorContact) {
        VendorContactPO po = VendorContactConvertor.toPO(vendorContact);
        vendorContactMapper.update(po);
    }

    @Override
    public VendorContact findById(String corpid, Long id) {
        return VendorContactConvertor.toDomain(vendorContactMapper.findById(corpid, id));
    }

    @Override
    public List<VendorContact> findByCondition(Map<String, Object> conditionMap) {
        return vendorContactMapper.findByCondition(conditionMap).stream().map(VendorContactConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        return vendorContactMapper.count(conditionMap);
    }
}
