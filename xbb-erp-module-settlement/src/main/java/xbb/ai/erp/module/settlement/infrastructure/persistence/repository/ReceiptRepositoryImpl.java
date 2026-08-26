package xbb.ai.erp.module.settlement.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.persistence.entity.BaseEntity;
import xbb.ai.erp.module.settlement.domain.model.Receipt;
import xbb.ai.erp.module.settlement.domain.repository.ReceiptRepository;
import xbb.ai.erp.module.settlement.infrastructure.persistence.convertor.ReceiptConvertor;
import xbb.ai.erp.module.settlement.infrastructure.persistence.mapper.ReceiptMapper;
import xbb.ai.erp.module.settlement.infrastructure.persistence.po.ReceiptPO;
import xbb.ai.erp.module.settlement.infrastructure.persistence.mapper.ReceiptPaymentMapper;
import xbb.ai.erp.module.settlement.infrastructure.persistence.po.ReceiptPaymentPO;
import xbb.ai.erp.module.settlement.domain.model.ReceiptPayment;

import java.util.List;
import java.util.Map;

@Repository("xbbAiErpModuleSettlementReceiptRepositoryImpl")
@RequiredArgsConstructor
public class ReceiptRepositoryImpl implements ReceiptRepository {

    private final ReceiptMapper receiptMapper;
    private final ReceiptPaymentMapper receiptPaymentMapper;

    @Override
    public Long insert(Receipt receipt) {
        ReceiptPO po = ReceiptConvertor.toPO(receipt);
        initializeForInsert(po);
        receiptMapper.insert(po);
        receipt.setId(po.getId());
        return po.getId();
    }

    @Override
    public void insertBatch(List<Receipt> receiptList) {
        List<ReceiptPO> poList = receiptList.stream().map(ReceiptConvertor::toPO).toList();
        poList.forEach(this::initializeForInsert);
        receiptMapper.insertBatch(poList);
        for (int index = 0; index < receiptList.size(); index++) {
            receiptList.get(index).setId(poList.get(index).getId());
        }
    }

    @Override
    public void removeById(String corpid, Long id) {
        receiptMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        receiptMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(Receipt receipt) {
        ReceiptPO po = ReceiptConvertor.toPO(receipt);
        receiptMapper.update(po);
    }

    @Override
    public Receipt findById(String corpid, Long id) {
        return ReceiptConvertor.toDomain(receiptMapper.findById(corpid, id));
    }

    @Override
    public List<Receipt> findByIds(String corpid, java.util.Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return receiptMapper.findByIds(corpid, ids).stream().map(ReceiptConvertor::toDomain).toList();
    }

    @Override
    public List<Receipt> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return receiptMapper.findByCondition(preparedConditionMap).stream().map(ReceiptConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return receiptMapper.count(preparedConditionMap);
    }

    @Override
    public List<ReceiptPayment> findPaymentsByReceiptId(String corpid, Long receiptId) {
        return receiptPaymentMapper.findByReceiptId(corpid, receiptId).stream()
            .map(this::toPaymentDomain)
            .toList();
    }

    @Override
    public void replacePayments(String corpid, Long receiptId, List<ReceiptPayment> payments) {
        receiptPaymentMapper.removeByReceiptId(corpid, receiptId);
        if (payments == null || payments.isEmpty()) {
            return;
        }
        List<ReceiptPaymentPO> poList = java.util.stream.IntStream.range(0, payments.size())
            .mapToObj(index -> toPaymentPO(payments.get(index), corpid, receiptId, index + 1))
            .toList();
        receiptPaymentMapper.insertBatch(poList);
    }

    private void initializeForInsert(BaseEntity po) {
        long now = System.currentTimeMillis();
        po.setId(null);
        po.setDel(0);
        po.setAddTime(now);
        po.setUpdateTime(now);
    }

    private ReceiptPaymentPO toPaymentPO(ReceiptPayment payment, String corpid, Long receiptId, int sortNo) {
        ReceiptPaymentPO po = new ReceiptPaymentPO();
        po.setCorpid(corpid);
        po.setReceiptId(receiptId);
        po.setBankAccountId(payment.getBankAccountId());
        po.setPaymentMethod(payment.getPaymentMethod());
        po.setAmount(payment.getAmount());
        po.setHandlingFee(payment.getHandlingFee());
        po.setTransactionNo(payment.getTransactionNo());
        po.setRemark(payment.getRemark());
        po.setSortNo(sortNo);
        po.setCreatorId(payment.getCreatorId());
        po.setModifyId(payment.getModifyId());
        initializeForInsert(po);
        return po;
    }

    private ReceiptPayment toPaymentDomain(ReceiptPaymentPO po) {
        ReceiptPayment payment = new ReceiptPayment();
        payment.setId(po.getId());
        payment.setCorpid(po.getCorpid());
        payment.setReceiptId(po.getReceiptId());
        payment.setBankAccountId(po.getBankAccountId());
        payment.setPaymentMethod(po.getPaymentMethod());
        payment.setAmount(po.getAmount());
        payment.setHandlingFee(po.getHandlingFee());
        payment.setTransactionNo(po.getTransactionNo());
        payment.setRemark(po.getRemark());
        payment.setSortNo(po.getSortNo());
        payment.setCreatorId(po.getCreatorId());
        payment.setModifyId(po.getModifyId());
        return payment;
    }
}
