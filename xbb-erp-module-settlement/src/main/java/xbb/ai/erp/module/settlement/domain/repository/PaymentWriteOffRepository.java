package xbb.ai.erp.module.settlement.domain.repository;

import java.util.List;
import java.util.Map;
import xbb.ai.erp.module.settlement.domain.model.PaymentWriteOff;

public interface PaymentWriteOffRepository {
    Long insert(PaymentWriteOff writeOff);

    PaymentWriteOff findById(String corpid, Long id);

    List<PaymentWriteOff> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);

    void reverse(String corpid, Long id, Long reversedTime, String userId);
}
