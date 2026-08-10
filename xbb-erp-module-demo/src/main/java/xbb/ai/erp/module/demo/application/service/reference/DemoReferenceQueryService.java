package xbb.ai.erp.module.demo.application.service.reference;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import xbb.ai.erp.module.demo.contract.DemoReferenceItem;
import xbb.ai.erp.module.demo.contract.DemoReferenceQueryApi;
import xbb.ai.erp.module.demo.domain.repository.DemoRepository;

@Service
public class DemoReferenceQueryService implements DemoReferenceQueryApi {
  private final DemoRepository demoRepository;

  public DemoReferenceQueryService(DemoRepository demoRepository) {
    this.demoRepository = demoRepository;
  }

  @Override
  public Map<Long, DemoReferenceItem> findActiveByIds(String corpid, Collection<Long> ids) {
    if (corpid == null || corpid.isBlank() || ids == null || ids.isEmpty()) {
      return Map.of();
    }
    Map<Long, DemoReferenceItem> references = new LinkedHashMap<>();
    demoRepository.findByIds(corpid, ids).forEach(demo ->
        references.put(demo.getId(), new DemoReferenceItem(demo.getId(), demo.getName())));
    return references;
  }

  @Override
  public boolean existsActive(String corpid, Long id) {
    return id != null && demoRepository.findById(corpid, id) != null;
  }
}
