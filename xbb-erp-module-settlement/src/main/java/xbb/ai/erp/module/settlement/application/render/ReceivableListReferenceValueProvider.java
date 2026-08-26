package xbb.ai.erp.module.settlement.application.render;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.common.application.render.ListReferenceKey;
import xbb.ai.erp.module.common.application.render.ListReferenceValueProviderSupport;
import xbb.ai.erp.module.settlement.domain.model.Receivable;
import xbb.ai.erp.module.settlement.domain.repository.ReceivableRepository;

@Component
public class ReceivableListReferenceValueProvider extends ListReferenceValueProviderSupport {
  private final ReceivableRepository receivableRepository;

  public ReceivableListReferenceValueProvider(ReceivableRepository receivableRepository) {
    this.receivableRepository = receivableRepository;
  }

  @Override
  public ListReferenceKey key() {
    return new ListReferenceKey(
        String.valueOf(FieldTypeEnum.BUSINESS.getType()), BusinessCodeEnum.RECEIVABLE.getCode());
  }

  @Override
  public Map<String, String> findDisplayMap(String corpid, Set<String> values) {
    return receivableRepository.findByIds(corpid, parseIds(values)).stream()
        .collect(Collectors.toMap(receivable -> String.valueOf(receivable.getId()), Receivable::getReceivableNo));
  }
}
