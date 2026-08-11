package xbb.ai.erp.module.demo.application.service.reference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import xbb.ai.erp.module.common.application.provider.ListMetaRegistry;
import xbb.ai.erp.module.common.application.render.ListReferenceValueProviderRegistry;
import xbb.ai.erp.module.common.application.render.ListValueRenderer;
import xbb.ai.erp.module.demo.application.field.DemoFieldFactory;
import xbb.ai.erp.module.demo.application.provider.DemoListMetaProvider;
import xbb.ai.erp.module.demo.application.render.DemoListReferenceValueProvider;
import xbb.ai.erp.module.demo.application.schema.DemoListSchemaProvider;
import xbb.ai.erp.module.demo.application.service.query.DemoQueryAppServiceImpl;
import xbb.ai.erp.module.demo.domain.model.Demo;
import xbb.ai.erp.module.demo.domain.repository.DemoRepository;
import xbb.ai.erp.module.demo.domain.repository.DemoItemRepository;

class DemoReferenceQueryServiceTest {
  @Test
  void shouldBatchLoadReferenceNamesWithoutDependingOnListQueryService() {
    DemoRepository repository = mock(DemoRepository.class);
    Demo first = new Demo();
    first.setId(1L);
    first.setName("Demo一");
    Demo second = new Demo();
    second.setId(2L);
    second.setName("Demo二");
    when(repository.findByIds("corp-001", List.of(1L, 2L))).thenReturn(List.of(first, second));

    DemoReferenceQueryService service = new DemoReferenceQueryService(repository);

    assertEquals("Demo一", service.findActiveByIds("corp-001", List.of(1L, 2L)).get(1L).name());
  }

  @Test
  void shouldCreateReferenceProviderAndListQueryWithoutCircularDependency() {
    try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
      context.registerBean(DemoRepository.class, () -> mock(DemoRepository.class));
      context.registerBean(DemoItemRepository.class, () -> mock(DemoItemRepository.class));
      context.registerBean(DemoFieldFactory.class, () -> mock(DemoFieldFactory.class));
      context.register(
          DemoListMetaProvider.class,
          DemoListSchemaProvider.class,
          ListMetaRegistry.class,
          ListReferenceValueProviderRegistry.class,
          ListValueRenderer.class,
          DemoReferenceQueryService.class,
          DemoListReferenceValueProvider.class,
          DemoQueryAppServiceImpl.class);

      context.refresh();

      assertNotNull(context.getBean(DemoQueryAppServiceImpl.class));
      assertNotNull(context.getBean(DemoListReferenceValueProvider.class));
    }
  }
}
