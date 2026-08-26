package xbb.ai.erp.module.masterdata.application.render;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.common.application.render.ListReferenceKey;
import xbb.ai.erp.module.common.application.render.ListReferenceValueProviderSupport;
import xbb.ai.erp.module.masterdata.domain.model.Supplier;
import xbb.ai.erp.module.masterdata.domain.repository.SupplierRepository;

@Component
public class SupplierListReferenceValueProvider extends ListReferenceValueProviderSupport {
  private final SupplierRepository supplierRepository;

  public SupplierListReferenceValueProvider(SupplierRepository supplierRepository) {
    this.supplierRepository = supplierRepository;
  }

  @Override
  public ListReferenceKey key() {
    return new ListReferenceKey(
        String.valueOf(FieldTypeEnum.BUSINESS.getType()), BusinessCodeEnum.SUPPLIER.getCode());
  }

  @Override
  public Map<String, String> findDisplayMap(String corpid, Set<String> values) {
    return supplierRepository.findByIds(corpid, parseIds(values)).stream()
        .collect(Collectors.toMap(supplier -> String.valueOf(supplier.getId()), Supplier::getSupplierName));
  }
}
