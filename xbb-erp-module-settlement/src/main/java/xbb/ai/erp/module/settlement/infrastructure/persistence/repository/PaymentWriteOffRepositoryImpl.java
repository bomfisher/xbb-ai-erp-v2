package xbb.ai.erp.module.settlement.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.settlement.domain.model.PaymentWriteOff;
import xbb.ai.erp.module.settlement.domain.repository.PaymentWriteOffRepository;
import xbb.ai.erp.module.settlement.infrastructure.persistence.mapper.PaymentWriteOffMapper;
import xbb.ai.erp.module.settlement.infrastructure.persistence.po.PaymentWriteOffPO;

@Repository
@RequiredArgsConstructor
public class PaymentWriteOffRepositoryImpl implements PaymentWriteOffRepository {
    private final PaymentWriteOffMapper mapper;

    @Override
    public Long insert(PaymentWriteOff writeOff) {
        PaymentWriteOffPO po = new PaymentWriteOffPO();
        po.setCorpid(writeOff.getCorpid());
        po.setSupplierId(writeOff.getSupplierId());
        po.setWriteoffNo(writeOff.getWriteoffNo());
        po.setPaymentId(writeOff.getPaymentId());
        po.setPayableId(writeOff.getPayableId());
        po.setWriteoffDate(writeOff.getWriteoffDate());
        po.setAmount(writeOff.getAmount());
        po.setRemark(writeOff.getRemark());
        po.setCreatorId(writeOff.getCreatorId());
        po.setModifyId(writeOff.getModifyId());
        long now = System.currentTimeMillis();
        po.setId(null);
        po.setDel(0);
        po.setAddTime(now);
        po.setUpdateTime(now);
        mapper.insert(po);
        writeOff.setId(po.getId());
        return po.getId();
    }

    @Override
    public PaymentWriteOff findById(String corpid, Long id) {
        return toDomain(mapper.findById(corpid, id));
    }

    @Override
    public List<PaymentWriteOff> findByCondition(Map<String, Object> conditionMap) {
        return mapper.findByCondition(conditionMap).stream().map(this::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        return mapper.count(conditionMap);
    }

    @Override
    public void reverse(String corpid, Long id, Long reversedTime, String userId) {
        if (mapper.reverse(corpid, id, reversedTime, userId) != 1) {
            throw new BizException("核销记录已冲销或不存在");
        }
    }

    private PaymentWriteOff toDomain(PaymentWriteOffPO source) {
        if (source == null) {
            return null;
        }
        PaymentWriteOff target = new PaymentWriteOff();
        target.setId(source.getId());
        target.setCorpid(source.getCorpid());
        target.setSupplierId(source.getSupplierId());
        target.setWriteoffNo(source.getWriteoffNo());
        target.setPaymentId(source.getPaymentId());
        target.setPayableId(source.getPayableId());
        target.setWriteoffDate(source.getWriteoffDate());
        target.setAmount(source.getAmount());
        target.setStatus(source.getStatus());
        target.setReversedTime(source.getReversedTime());
        target.setRemark(source.getRemark());
        target.setCreatorId(source.getCreatorId());
        target.setModifyId(source.getModifyId());
        return target;
    }
}
