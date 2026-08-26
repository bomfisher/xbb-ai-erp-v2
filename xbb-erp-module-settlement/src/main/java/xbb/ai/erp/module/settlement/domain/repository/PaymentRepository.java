package xbb.ai.erp.module.settlement.domain.repository;

import xbb.ai.erp.module.settlement.domain.model.Payment;
import xbb.ai.erp.module.settlement.domain.model.PaymentDetail;

import java.util.List;
import java.util.Map;

public interface PaymentRepository {
    Long insert(Payment payment);

    void insertBatch(List<Payment> paymentList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(Payment payment);

    Payment findById(String corpid, Long id);

    List<Payment> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);

    List<PaymentDetail> findDetailsByPaymentId(String corpid, Long paymentId);

    void replaceDetails(String corpid, Long paymentId, List<PaymentDetail> details);
}
