package xbb.ai.erp.module.supplier.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.supplier.domain.model.VendorBankAccount;
import xbb.ai.erp.module.supplier.domain.repository.VendorBankAccountRepository;
import xbb.ai.erp.module.supplier.infrastructure.persistence.convertor.VendorBankAccountConvertor;
import xbb.ai.erp.module.supplier.infrastructure.persistence.mapper.VendorBankAccountMapper;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.VendorBankAccountPO;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class VendorBankAccountRepositoryImpl implements VendorBankAccountRepository {

    private final VendorBankAccountMapper vendorBankAccountMapper;

    @Override
    public void insert(VendorBankAccount vendorBankAccount) {
        vendorBankAccountMapper.insert(VendorBankAccountConvertor.toPO(vendorBankAccount));
    }

    @Override
    public void insertBatch(List<VendorBankAccount> vendorBankAccountList) {
        vendorBankAccountMapper.insertBatch(vendorBankAccountList.stream().map(VendorBankAccountConvertor::toPO).toList());
    }

    @Override
    public void removeById(String corpid, Long id) {
        vendorBankAccountMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        vendorBankAccountMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(VendorBankAccount vendorBankAccount) {
        VendorBankAccountPO po = VendorBankAccountConvertor.toPO(vendorBankAccount);
        vendorBankAccountMapper.update(po);
    }

    @Override
    public VendorBankAccount findById(String corpid, Long id) {
        return VendorBankAccountConvertor.toDomain(vendorBankAccountMapper.findById(corpid, id));
    }

    @Override
    public List<VendorBankAccount> findByCondition(Map<String, Object> conditionMap) {
        return vendorBankAccountMapper.findByCondition(conditionMap).stream().map(VendorBankAccountConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        return vendorBankAccountMapper.count(conditionMap);
    }
}
