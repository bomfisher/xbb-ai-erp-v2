package xbb.ai.erp.module.settlement.domain.repository;

import xbb.ai.erp.module.settlement.domain.model.Receipt;
import xbb.ai.erp.module.settlement.domain.model.ReceiptPayment;

import java.util.List;
import java.util.Map;

public interface ReceiptRepository {
    Long insert(Receipt receipt);

    void insertBatch(List<Receipt> receiptList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(Receipt receipt);

    Receipt findById(String corpid, Long id);

    List<Receipt> findByIds(String corpid, java.util.Collection<Long> ids);

    List<Receipt> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);

    List<ReceiptPayment> findPaymentsByReceiptId(String corpid, Long receiptId);

    void replacePayments(String corpid, Long receiptId, List<ReceiptPayment> payments);
}
