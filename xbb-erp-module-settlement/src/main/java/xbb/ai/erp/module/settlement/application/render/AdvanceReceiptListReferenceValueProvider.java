package xbb.ai.erp.module.settlement.application.render;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.common.application.render.ListReferenceKey;
import xbb.ai.erp.module.common.application.render.ListReferenceValueProviderSupport;
import xbb.ai.erp.module.settlement.domain.model.Receipt;
import xbb.ai.erp.module.settlement.domain.repository.ReceiptRepository;

@Component
public class AdvanceReceiptListReferenceValueProvider extends ListReferenceValueProviderSupport {
  private static final String ADVANCE_PAYMENT = "ADVANCE_PAYMENT";

  private final ReceiptRepository receiptRepository;

  public AdvanceReceiptListReferenceValueProvider(ReceiptRepository receiptRepository) {
    this.receiptRepository = receiptRepository;
  }

  @Override
  public ListReferenceKey key() {
    return new ListReferenceKey(
        String.valueOf(FieldTypeEnum.BUSINESS.getType()), BusinessCodeEnum.ADVANCE_RECEIPT.getCode());
  }

  @Override
  public Map<String, String> findDisplayMap(String corpid, Set<String> values) {
    return receiptRepository.findByIds(corpid, parseIds(values)).stream()
        .filter(receipt -> ADVANCE_PAYMENT.equals(receipt.getReceiptType()))
        .collect(Collectors.toMap(receipt -> String.valueOf(receipt.getId()), Receipt::getReceiptNo));
  }
}
