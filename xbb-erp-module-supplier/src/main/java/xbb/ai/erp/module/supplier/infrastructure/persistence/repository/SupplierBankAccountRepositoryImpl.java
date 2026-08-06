package xbb.ai.erp.module.supplier.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.supplier.domain.model.SupplierBankAccount;
import xbb.ai.erp.module.supplier.domain.repository.SupplierBankAccountRepository;
import xbb.ai.erp.module.supplier.infrastructure.persistence.convertor.SupplierBankAccountConvertor;
import xbb.ai.erp.module.supplier.infrastructure.persistence.mapper.SupplierBankAccountMapper;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.SupplierBankAccountPO;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class SupplierBankAccountRepositoryImpl implements SupplierBankAccountRepository {

    private final SupplierBankAccountMapper supplierBankAccountMapper;

    @Override
    public void insert(SupplierBankAccount supplierBankAccount) {
        SupplierBankAccountPO po = SupplierBankAccountConvertor.toPO(supplierBankAccount);
        supplierBankAccountMapper.insert(po);
        supplierBankAccount.setId(po.getId());
    }

    @Override
    public void insertBatch(List<SupplierBankAccount> supplierBankAccountList) {
        supplierBankAccountMapper.insertBatch(supplierBankAccountList.stream().map(SupplierBankAccountConvertor::toPO).toList());
    }

    @Override
    public void removeById(String corpid, Long id) {
        supplierBankAccountMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        supplierBankAccountMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(SupplierBankAccount supplierBankAccount) {
        supplierBankAccountMapper.update(SupplierBankAccountConvertor.toPO(supplierBankAccount));
    }

    @Override
    public SupplierBankAccount findById(String corpid, Long id) {
        return SupplierBankAccountConvertor.toDomain(supplierBankAccountMapper.findById(corpid, id));
    }

    @Override
    public List<SupplierBankAccount> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> normalizedConditionMap = ConditionMapHelper.normalizePage(conditionMap);
        return supplierBankAccountMapper.findByCondition(normalizedConditionMap).stream().map(SupplierBankAccountConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        return supplierBankAccountMapper.count(ConditionMapHelper.normalizePage(conditionMap));
    }
}
