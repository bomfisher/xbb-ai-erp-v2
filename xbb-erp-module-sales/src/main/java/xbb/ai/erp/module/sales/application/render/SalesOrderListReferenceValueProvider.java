package xbb.ai.erp.module.sales.application.render;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.common.application.render.ListReferenceKey;
import xbb.ai.erp.module.common.application.render.ListReferenceValueProviderSupport;
import xbb.ai.erp.module.sales.domain.model.SalesOrder;
import xbb.ai.erp.module.sales.domain.repository.SalesOrderRepository;

@Component
public class SalesOrderListReferenceValueProvider extends ListReferenceValueProviderSupport {
  private final SalesOrderRepository salesOrderRepository;

  public SalesOrderListReferenceValueProvider(SalesOrderRepository salesOrderRepository) {
    this.salesOrderRepository = salesOrderRepository;
  }

  @Override
  public ListReferenceKey key() {
    return new ListReferenceKey(
        String.valueOf(FieldTypeEnum.BUSINESS.getType()), BusinessCodeEnum.SALES_ORDER.getCode());
  }

  @Override
  public Map<String, String> findDisplayMap(String corpid, Set<String> values) {
    return salesOrderRepository.findByIds(corpid, parseIds(values)).stream()
        .collect(Collectors.toMap(order -> String.valueOf(order.getId()), SalesOrder::getOrderNo));
  }
}
