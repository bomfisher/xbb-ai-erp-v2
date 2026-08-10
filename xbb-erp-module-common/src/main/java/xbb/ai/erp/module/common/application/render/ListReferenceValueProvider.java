package xbb.ai.erp.module.common.application.render;

import java.util.Map;
import java.util.Set;

public interface ListReferenceValueProvider {
  ListReferenceKey key();

  Map<String, String> findDisplayMap(String corpid, Set<String> values);
}
