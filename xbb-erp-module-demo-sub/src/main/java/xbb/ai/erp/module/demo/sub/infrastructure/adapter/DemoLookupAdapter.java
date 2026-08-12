package xbb.ai.erp.module.demo.sub.infrastructure.adapter;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import xbb.ai.erp.module.demo.contract.DemoReferenceQueryApi;
import xbb.ai.erp.module.demo.sub.application.port.DemoLookupPort;

@Component
@RequiredArgsConstructor
public class DemoLookupAdapter implements DemoLookupPort {
  private final DemoReferenceQueryApi demoReferenceQueryApi;

  @Override
  public Map<Long, String> findNamesByIds(String corpid, Set<Long> ids) {
    return demoReferenceQueryApi.findActiveByIds(corpid, ids).values().stream()
        .collect(Collectors.toMap(item -> item.id(), item -> item.name()));
  }

  @Override
  public boolean existsActive(String corpid, Long id) {
    return demoReferenceQueryApi.existsActive(corpid, id);
  }
}
