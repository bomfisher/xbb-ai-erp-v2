package xbb.ai.erp.module.settlement.domain.repository;

import java.util.List;
import java.util.Map;
import xbb.ai.erp.module.settlement.domain.model.ReceiptWriteOff;

public interface ReceiptWriteOffRepository {
    Long insert(ReceiptWriteOff writeOff);

    ReceiptWriteOff findById(String corpid, Long id);

    List<ReceiptWriteOff> findActiveByReceiptId(String corpid, Long receiptId);

    void update(ReceiptWriteOff writeOff);

    void reverse(String corpid, Long id, Long reversedTime, String userId);

    List<ReceiptWriteOff> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
