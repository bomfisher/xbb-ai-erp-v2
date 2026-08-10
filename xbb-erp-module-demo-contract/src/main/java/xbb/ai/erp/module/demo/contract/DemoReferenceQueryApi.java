package xbb.ai.erp.module.demo.contract;

import java.util.Collection;
import java.util.Map;

public interface DemoReferenceQueryApi {
  Map<Long, DemoReferenceItem> findActiveByIds(String corpid, Collection<Long> ids);

  boolean existsActive(String corpid, Long id);
}
