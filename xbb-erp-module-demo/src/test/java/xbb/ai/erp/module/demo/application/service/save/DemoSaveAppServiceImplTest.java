package xbb.ai.erp.module.demo.application.service.save;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import xbb.ai.erp.module.demo.admin.dto.DemoMainDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoSaveDTO;
import xbb.ai.erp.module.demo.application.port.DemoDraftRepository;
import xbb.ai.erp.module.demo.application.validator.DemoSaveBusinessValidator;
import xbb.ai.erp.module.demo.application.validator.DemoSaveCommonValidator;
import xbb.ai.erp.module.demo.application.validator.DemoSaveProtocolValidator;
import xbb.ai.erp.module.demo.domain.model.Demo;
import xbb.ai.erp.module.demo.domain.repository.DemoRepository;

class DemoSaveAppServiceImplTest {

    @Test
    void shouldInitializeAuditFieldsAndDelBeforeInsert() {
        DemoRepository repository = mock(DemoRepository.class);
        DemoSaveAppServiceImpl service = new DemoSaveAppServiceImpl(
            repository,
            mock(DemoDraftRepository.class),
            mock(DemoSaveProtocolValidator.class),
            mock(DemoSaveCommonValidator.class),
            mock(DemoSaveBusinessValidator.class)
        );
        DemoSaveDTO dto = saveDTO();
        long beforeSave = System.currentTimeMillis();

        service.save(dto);

        ArgumentCaptor<Demo> captor = ArgumentCaptor.forClass(Demo.class);
        verify(repository).insert(captor.capture());
        Demo entity = captor.getValue();
        assertEquals("user-001", entity.getCreatorId());
        assertEquals("user-001", entity.getModifyId());
        assertEquals(0, entity.getDel());
        assertNotNull(entity.getAddTime());
        assertEquals(entity.getAddTime(), entity.getUpdateTime());
        assertTrue(entity.getAddTime() >= beforeSave);
    }

    private DemoSaveDTO saveDTO() {
        DemoMainDTO main = new DemoMainDTO();
        main.setName("demo");
        DemoSaveDTO dto = new DemoSaveDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        dto.setMain(main);
        return dto;
    }
}
