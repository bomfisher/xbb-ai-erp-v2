package xbb.ai.erp.module.demo.application.service.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.module.common.application.render.ListValueRenderer;
import xbb.ai.erp.module.demo.admin.vo.DemoListItemVO;
import xbb.ai.erp.module.demo.application.field.DemoFieldFactory;
import xbb.ai.erp.module.demo.application.schema.DemoListSchemaProvider;
import xbb.ai.erp.module.demo.domain.model.Demo;
import xbb.ai.erp.module.demo.domain.repository.DemoRepository;
import xbb.ai.erp.module.demo.domain.repository.DemoItemRepository;

class DemoQueryAppServiceImplTest {

  @Test
  void shouldPassCurrentPageItemsToCommonRenderer() {
    DemoRepository repository = mock(DemoRepository.class);
    DemoListSchemaProvider schemaProvider = mock(DemoListSchemaProvider.class);
    ListValueRenderer renderer = mock(ListValueRenderer.class);
    AtomicReference<List<DemoListItemVO>> renderedItems = new AtomicReference<>();
    when(schemaProvider.conditionMetaMap()).thenReturn(Map.of());
    Demo demo = new Demo();
    demo.setId(1L);
    demo.setCorpid("corp-001");
    demo.setDate(1_723_200_000_000L);
    demo.setTime(1_723_200_000_000L);
    demo.setCreatorId("user-001");
    demo.setModifyId("user-002");
    when(repository.findByCondition(anyMap())).thenReturn(List.of(demo));
    when(repository.count(anyMap())).thenReturn(1L);
    when(renderer.render(eq("corp-001"), eq("DEMO"), anyList()))
        .thenAnswer(
            invocation -> {
              List<DemoListItemVO> items = invocation.getArgument(2);
              renderedItems.set(items);
              return items;
            });
    DemoQueryAppServiceImpl service =
        new DemoQueryAppServiceImpl(repository, mock(DemoItemRepository.class), mock(DemoFieldFactory.class), schemaProvider, renderer);
    ListBaseDTO dto = new ListBaseDTO();
    dto.setCorpid("corp-001");
    dto.setPageNum(1);

    service.list(dto);

    verify(renderer).render(eq("corp-001"), eq("DEMO"), anyList());
    DemoListItemVO item = renderedItems.get().getFirst();
    assertEquals("1723200000000", item.getDate());
    assertEquals("1723200000000", item.getTime());
    assertEquals("user-001", item.getCreatorId());
    assertEquals("user-002", item.getModifyId());
  }
}
