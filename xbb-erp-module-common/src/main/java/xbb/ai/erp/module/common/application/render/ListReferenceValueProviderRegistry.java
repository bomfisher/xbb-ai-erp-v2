package xbb.ai.erp.module.common.application.render;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.exception.BizException;

@Component
public class ListReferenceValueProviderRegistry {
  private final Map<ListReferenceKey, ListReferenceValueProvider> providerMap;

  public ListReferenceValueProviderRegistry(List<ListReferenceValueProvider> providers) {
    this.providerMap =
        providers == null
            ? Map.of()
            : providers.stream()
                .collect(
                    Collectors.toMap(
                        ListReferenceValueProvider::key,
                        Function.identity(),
                        (left, right) -> {
                          throw new BizException("列表引用渲染提供者重复: " + left.key());
                        },
                        LinkedHashMap::new));
  }

  public ListReferenceValueProvider find(ListReferenceKey key) {
    return providerMap.get(key);
  }
}
