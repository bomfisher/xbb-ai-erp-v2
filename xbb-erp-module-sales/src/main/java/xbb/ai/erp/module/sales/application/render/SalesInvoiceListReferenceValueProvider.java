package xbb.ai.erp.module.sales.application.render;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.common.application.render.ListReferenceKey;
import xbb.ai.erp.module.common.application.render.ListReferenceValueProviderSupport;
import xbb.ai.erp.module.sales.domain.model.SalesInvoice;
import xbb.ai.erp.module.sales.domain.repository.SalesInvoiceRepository;

@Component
public class SalesInvoiceListReferenceValueProvider extends ListReferenceValueProviderSupport {
  private final SalesInvoiceRepository salesInvoiceRepository;

  public SalesInvoiceListReferenceValueProvider(SalesInvoiceRepository salesInvoiceRepository) {
    this.salesInvoiceRepository = salesInvoiceRepository;
  }

  @Override
  public ListReferenceKey key() {
    return new ListReferenceKey(
        String.valueOf(FieldTypeEnum.BUSINESS.getType()), BusinessCodeEnum.SALES_INVOICE.getCode());
  }

  @Override
  public Map<String, String> findDisplayMap(String corpid, Set<String> values) {
    return salesInvoiceRepository.findByIds(corpid, parseIds(values)).stream()
        .collect(Collectors.toMap(invoice -> String.valueOf(invoice.getId()), SalesInvoice::getInvoiceNo));
  }
}
