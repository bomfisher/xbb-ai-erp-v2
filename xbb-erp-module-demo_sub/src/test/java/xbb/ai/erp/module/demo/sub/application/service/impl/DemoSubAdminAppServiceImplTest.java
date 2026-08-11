package xbb.ai.erp.module.demo.sub.application.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.demo.sub.admin.dto.DemoSubSelectionFillDTO;
import xbb.ai.erp.module.demo.sub.admin.vo.DemoSubSelectionFillVO;
import xbb.ai.erp.module.demo.sub.application.port.DemoLookupPort;
import xbb.ai.erp.module.demo.sub.application.service.draft.DemoSubDraftAppService;
import xbb.ai.erp.module.demo.sub.application.service.query.DemoSubQueryAppServiceImpl;
import xbb.ai.erp.module.demo.sub.application.service.save.DemoSubSaveAppServiceImpl;

class DemoSubAdminAppServiceImplTest {

  @Test
  void shouldFillParentNameFromSelectedDemo() {
    DemoLookupPort demoLookupPort = mock(DemoLookupPort.class);
    when(demoLookupPort.findNamesByIds("corp-001", java.util.Set.of(100L)))
        .thenReturn(Map.of(100L, "父级DEMO"));
    DemoSubAdminAppServiceImpl service =
        new DemoSubAdminAppServiceImpl(
            mock(DemoSubQueryAppServiceImpl.class),
            mock(DemoSubSaveAppServiceImpl.class),
            mock(DemoSubDraftAppService.class),
            demoLookupPort);
    DemoSubSelectionFillDTO dto = new DemoSubSelectionFillDTO();
    dto.setCorpid("corp-001");
    dto.setFieldAttr("main.dataId");
    dto.setReferenceId(100L);

    DemoSubSelectionFillVO result = service.selectionFill(dto);

    assertEquals(100L, result.getReferenceId());
    assertEquals("父级DEMO", result.getPatch().get("main.parentName"));
  }
}
