package xbb.ai.erp.module.demo.sub.application.service.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.demo.sub.admin.vo.DemoSubSaveItemVO;
import xbb.ai.erp.module.demo.sub.application.field.DefaultDemoSubFieldFactory;
import xbb.ai.erp.module.demo.sub.application.port.DemoLookupPort;
import xbb.ai.erp.module.demo.sub.domain.model.DemoSub;
import xbb.ai.erp.module.demo.sub.domain.repository.DemoSubRepository;

class DemoSubQueryAppServiceImplTest {

  @Test
  void shouldBuildBusinessSelectConfigForCreateAndUpdate() {
    DemoSubRepository repository = mock(DemoSubRepository.class);
    DemoLookupPort demoLookupPort = mock(DemoLookupPort.class);
    DemoSub demoSub = new DemoSub();
    demoSub.setId(1L);
    demoSub.setCorpid("corp-001");
    demoSub.setDataId(100L);
    when(repository.findById("corp-001", 1L)).thenReturn(demoSub);
    DemoSubQueryAppServiceImpl service =
        new DemoSubQueryAppServiceImpl(repository, null, new DefaultDemoSubFieldFactory(), demoLookupPort);

    BaseDTO createDTO = new BaseDTO();
    createDTO.setCorpid("corp-001");
    SaveItemVO<DemoSubSaveItemVO> createResult = service.addItem(createDTO);

    IdBaseDTO updateDTO = new IdBaseDTO();
    updateDTO.setCorpid("corp-001");
    updateDTO.setId(1L);
    SaveItemVO<DemoSubSaveItemVO> updateResult = service.updateItem(updateDTO);

    assertSelectConfigs(createResult);
    assertSelectConfigs(updateResult);
  }

  private void assertSelectConfigs(SaveItemVO<DemoSubSaveItemVO> result) {
    FieldEntity dataIdField =
        result.getHeadList().stream()
            .filter(field -> "main.dataId".equals(field.getAttr()))
            .findFirst()
            .orElseThrow();

    FieldEntity.BusinessSelectConfig config = dataIdField.getBusinessSelectConfig();
    assertNotNull(config);
    assertEquals("demo", config.getBusinessType());
    assertEquals("DEMO", config.getBusinessCode());
    assertEquals("corp-001", config.getRequestPayload().get("corpid"));
    assertFalse(config.getMultiple());

    assertFieldConfig(result, "main.userId", "member", "/erp/v1/org/memberSelect/getById");
    assertFieldConfig(result, "main.departmentId", "department", "/erp/v1/org/departmentSelect/getById");
  }

  private void assertFieldConfig(
      SaveItemVO<DemoSubSaveItemVO> result, String attr, String businessType, String getByIdUrl) {
    FieldEntity field =
        result.getHeadList().stream().filter(item -> attr.equals(item.getAttr())).findFirst().orElseThrow();
    assertNotNull(field.getBusinessSelectConfig());
    assertEquals(businessType, field.getBusinessSelectConfig().getBusinessType());
    assertEquals(getByIdUrl, field.getBusinessSelectConfig().getGetByIdUrl());
  }
}
