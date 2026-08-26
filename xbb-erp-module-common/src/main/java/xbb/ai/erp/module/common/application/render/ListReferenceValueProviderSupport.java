package xbb.ai.erp.module.common.application.render;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class ListReferenceValueProviderSupport implements ListReferenceValueProvider {
  protected Set<Long> parseIds(Set<String> values) {
    Set<Long> ids = new LinkedHashSet<>();
    for (String value : values) {
      try {
        ids.add(Long.valueOf(value));
      } catch (NumberFormatException ignored) {
      }
    }
    return ids;
  }
}
