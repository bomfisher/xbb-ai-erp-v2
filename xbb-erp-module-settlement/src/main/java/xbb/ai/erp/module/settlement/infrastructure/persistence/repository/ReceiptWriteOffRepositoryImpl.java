package xbb.ai.erp.module.settlement.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.settlement.domain.model.ReceiptWriteOff;
import xbb.ai.erp.module.settlement.domain.repository.ReceiptWriteOffRepository;
import xbb.ai.erp.module.settlement.infrastructure.persistence.mapper.ReceiptWriteOffMapper;
import xbb.ai.erp.module.settlement.infrastructure.persistence.po.ReceiptWriteOffPO;

@Repository
@RequiredArgsConstructor
public class ReceiptWriteOffRepositoryImpl implements ReceiptWriteOffRepository {
    private final ReceiptWriteOffMapper mapper;

    @Override
    public Long insert(ReceiptWriteOff writeOff) {
        ReceiptWriteOffPO po = toPO(writeOff);
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
    public ReceiptWriteOff findById(String corpid, Long id) {
        return toDomain(mapper.findById(corpid, id));
    }

    @Override
    public List<ReceiptWriteOff> findActiveByReceiptId(String corpid, Long receiptId) {
        return mapper.findActiveByReceiptId(corpid, receiptId).stream().map(this::toDomain).toList();
    }

    @Override
    public void update(ReceiptWriteOff writeOff) {
        ReceiptWriteOffPO po = toPO(writeOff);
        po.setUpdateTime(System.currentTimeMillis());
        mapper.update(po);
    }

    @Override
    public void reverse(String corpid, Long id, Long reversedTime, String userId) {
        if (mapper.reverse(corpid, id, reversedTime, userId) != 1) {
            throw new BizException("核销记录已冲销或不存在");
        }
    }

    @Override
    public List<ReceiptWriteOff> findByCondition(Map<String, Object> conditionMap) {
        return mapper.findByCondition(conditionMap).stream().map(this::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        return mapper.count(conditionMap);
    }

    private ReceiptWriteOffPO toPO(ReceiptWriteOff source) {
        if (source == null) return null;
        ReceiptWriteOffPO target = new ReceiptWriteOffPO();
        target.setId(source.getId()); target.setCorpid(source.getCorpid()); target.setCustomerId(source.getCustomerId()); target.setWriteoffNo(source.getWriteoffNo()); target.setReceiptId(source.getReceiptId());
        target.setReceivableId(source.getReceivableId()); target.setWriteoffDate(source.getWriteoffDate());
        target.setAmount(source.getAmount()); target.setStatus(source.getStatus());
        target.setReversedTime(source.getReversedTime()); target.setRemark(source.getRemark());
        target.setCreatorId(source.getCreatorId()); target.setModifyId(source.getModifyId());
        return target;
    }

    private ReceiptWriteOff toDomain(ReceiptWriteOffPO source) {
        if (source == null) return null;
        ReceiptWriteOff target = new ReceiptWriteOff();
        target.setId(source.getId()); target.setCorpid(source.getCorpid()); target.setCustomerId(source.getCustomerId()); target.setWriteoffNo(source.getWriteoffNo()); target.setReceiptId(source.getReceiptId());
        target.setReceivableId(source.getReceivableId()); target.setWriteoffDate(source.getWriteoffDate());
        target.setAmount(source.getAmount()); target.setStatus(source.getStatus());
        target.setReversedTime(source.getReversedTime()); target.setRemark(source.getRemark());
        target.setCreatorId(source.getCreatorId()); target.setModifyId(source.getModifyId());
        return target;
    }
}
