package xbb.ai.erp.module.demo.application.render;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.module.common.application.render.ListReferenceKey;
import xbb.ai.erp.module.common.application.render.ListReferenceValueProvider;
import xbb.ai.erp.module.demo.contract.DemoReferenceQueryApi;

@Component
public class DemoListReferenceValueProvider implements ListReferenceValueProvider {
  private final DemoReferenceQueryApi demoReferenceQueryApi;

  public DemoListReferenceValueProvider(DemoReferenceQueryApi demoReferenceQueryApi) {
    this.demoReferenceQueryApi = demoReferenceQueryApi;
  }

  @Override
  public ListReferenceKey key() {
    return new ListReferenceKey(String.valueOf(FieldTypeEnum.BUSINESS.getType()), "DEMO");
  }

  @Override
  public Map<String, String> findDisplayMap(String corpid, Set<String> values) {
    Set<Long> ids =
        values.stream()
            .flatMap(this::toLongStream)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    if (ids.isEmpty()) {
      return Map.of();
    }
    return demoReferenceQueryApi.findActiveByIds(corpid, ids).values().stream()
        .collect(Collectors.toMap(item -> String.valueOf(item.id()), item -> item.name()));
  }

  private java.util.stream.Stream<Long> toLongStream(String value) {
    try {
      return java.util.stream.Stream.of(Long.valueOf(value));
    } catch (NumberFormatException exception) {
      return java.util.stream.Stream.empty();
    }
  }
}
