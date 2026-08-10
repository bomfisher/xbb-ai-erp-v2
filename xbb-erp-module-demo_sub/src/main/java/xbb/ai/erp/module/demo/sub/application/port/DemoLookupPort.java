package xbb.ai.erp.module.demo.sub.application.port;

import java.util.Map;
import java.util.Set;

public interface DemoLookupPort {
  Map<Long, String> findNamesByIds(String corpid, Set<Long> ids);

  boolean existsActive(String corpid, Long id);
}
