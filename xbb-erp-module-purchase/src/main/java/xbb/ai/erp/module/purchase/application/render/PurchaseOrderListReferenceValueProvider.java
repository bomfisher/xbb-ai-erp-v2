package xbb.ai.erp.module.purchase.application.render;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.common.application.render.ListReferenceKey;
import xbb.ai.erp.module.common.application.render.ListReferenceValueProviderSupport;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrder;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderRepository;

@Component
public class PurchaseOrderListReferenceValueProvider extends ListReferenceValueProviderSupport {
  private final PurchaseOrderRepository purchaseOrderRepository;

  public PurchaseOrderListReferenceValueProvider(PurchaseOrderRepository purchaseOrderRepository) {
    this.purchaseOrderRepository = purchaseOrderRepository;
  }

  @Override
  public ListReferenceKey key() {
    return new ListReferenceKey(
        String.valueOf(FieldTypeEnum.BUSINESS.getType()), BusinessCodeEnum.PURCHASE_ORDER.getCode());
  }

  @Override
  public Map<String, String> findDisplayMap(String corpid, Set<String> values) {
    return purchaseOrderRepository.findByIds(corpid, parseIds(values)).stream()
        .collect(Collectors.toMap(order -> String.valueOf(order.getId()), PurchaseOrder::getOrderNo));
  }
}
