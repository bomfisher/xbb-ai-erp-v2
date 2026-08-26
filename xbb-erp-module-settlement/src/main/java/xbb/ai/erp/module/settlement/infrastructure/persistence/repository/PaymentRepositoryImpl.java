package xbb.ai.erp.module.settlement.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.persistence.entity.BaseEntity;
import xbb.ai.erp.module.settlement.domain.model.Payment;
import xbb.ai.erp.module.settlement.domain.repository.PaymentRepository;
import xbb.ai.erp.module.settlement.infrastructure.persistence.convertor.PaymentConvertor;
import xbb.ai.erp.module.settlement.infrastructure.persistence.mapper.PaymentMapper;
import xbb.ai.erp.module.settlement.infrastructure.persistence.po.PaymentPO;
import xbb.ai.erp.module.settlement.infrastructure.persistence.mapper.PaymentDetailMapper;
import xbb.ai.erp.module.settlement.infrastructure.persistence.po.PaymentDetailPO;
import xbb.ai.erp.module.settlement.domain.model.PaymentDetail;

import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

@Repository("xbbAiErpModuleSettlementPaymentRepositoryImpl")
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentMapper paymentMapper;
    private final PaymentDetailMapper paymentDetailMapper;

    @Override
    public Long insert(Payment payment) {
        PaymentPO po = PaymentConvertor.toPO(payment);
        initializeForInsert(po);
        paymentMapper.insert(po);
        payment.setId(po.getId());
        return po.getId();
    }

    @Override
    public void insertBatch(List<Payment> paymentList) {
        List<PaymentPO> poList = paymentList.stream().map(PaymentConvertor::toPO).toList();
        poList.forEach(this::initializeForInsert);
        paymentMapper.insertBatch(poList);
        for (int index = 0; index < paymentList.size(); index++) {
            paymentList.get(index).setId(poList.get(index).getId());
        }
    }

    @Override
    public void removeById(String corpid, Long id) {
        paymentMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        paymentMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(Payment payment) {
        PaymentPO po = PaymentConvertor.toPO(payment);
        paymentMapper.update(po);
    }

    @Override
    public Payment findById(String corpid, Long id) {
        return PaymentConvertor.toDomain(paymentMapper.findById(corpid, id));
    }

    @Override
    public List<Payment> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return paymentMapper.findByCondition(preparedConditionMap).stream().map(PaymentConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return paymentMapper.count(preparedConditionMap);
    }

    @Override
    public List<PaymentDetail> findDetailsByPaymentId(String corpid, Long paymentId) {
        return paymentDetailMapper.findByPaymentId(corpid, paymentId).stream().map(this::toDetail).toList();
    }

    @Override
    public void replaceDetails(String corpid, Long paymentId, List<PaymentDetail> details) {
        paymentDetailMapper.removeByPaymentId(corpid, paymentId);
        if (details == null || details.isEmpty()) {
            return;
        }
        List<PaymentDetailPO> poList = java.util.stream.IntStream.range(0, details.size())
            .mapToObj(index -> toDetailPO(details.get(index), corpid, paymentId, index + 1))
            .toList();
        paymentDetailMapper.insertBatch(poList);
    }

    private void initializeForInsert(BaseEntity po) {
        long now = System.currentTimeMillis();
        po.setId(null);
        po.setDel(0);
        po.setAddTime(now);
        po.setUpdateTime(now);
    }

    private PaymentDetailPO toDetailPO(PaymentDetail detail, String corpid, Long paymentId, int sortNo) {
        PaymentDetailPO po = new PaymentDetailPO();
        po.setCorpid(corpid);
        po.setPaymentId(paymentId);
        po.setBankAccountId(detail.getBankAccountId());
        po.setPaymentMethod(detail.getPaymentMethod());
        po.setAmount(detail.getAmount());
        po.setHandlingFee(detail.getHandlingFee() == null ? BigDecimal.ZERO : detail.getHandlingFee());
        po.setTransactionNo(detail.getTransactionNo());
        po.setRemark(detail.getRemark());
        po.setSortNo(sortNo);
        po.setCreatorId(detail.getCreatorId());
        po.setModifyId(detail.getModifyId());
        initializeForInsert(po);
        return po;
    }

    private PaymentDetail toDetail(PaymentDetailPO po) {
        PaymentDetail detail = new PaymentDetail();
        detail.setId(po.getId());
        detail.setCorpid(po.getCorpid());
        detail.setPaymentId(po.getPaymentId());
        detail.setBankAccountId(po.getBankAccountId());
        detail.setPaymentMethod(po.getPaymentMethod());
        detail.setAmount(po.getAmount());
        detail.setHandlingFee(po.getHandlingFee());
        detail.setTransactionNo(po.getTransactionNo());
        detail.setRemark(po.getRemark());
        detail.setSortNo(po.getSortNo());
        detail.setCreatorId(po.getCreatorId());
        detail.setModifyId(po.getModifyId());
        return detail;
    }
}
