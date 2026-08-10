package xbb.ai.erp.module.demo.sub.application.service.save;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import xbb.ai.erp.module.demo.sub.admin.dto.DemoSubMainDTO;
import xbb.ai.erp.module.demo.sub.admin.dto.DemoSubSaveDTO;
import xbb.ai.erp.module.demo.sub.application.port.DemoSubDraftRepository;
import xbb.ai.erp.module.demo.sub.application.validator.DemoSubSaveBusinessValidator;
import xbb.ai.erp.module.demo.sub.domain.model.DemoSub;
import xbb.ai.erp.module.demo.sub.domain.repository.DemoSubRepository;

class DemoSubSaveAppServiceImplTest {

    @Test
    void shouldInitializeAuditFieldsAndDelBeforeInsert() {
        DemoSubRepository repository = mock(DemoSubRepository.class);
        DemoSubSaveAppServiceImpl service = new DemoSubSaveAppServiceImpl(
            repository,
            mock(DemoSubDraftRepository.class),
            mock(DemoSubSaveBusinessValidator.class)
        );
        DemoSubSaveDTO dto = saveDTO();
        long beforeSave = System.currentTimeMillis();

        service.save(dto);

        ArgumentCaptor<DemoSub> captor = ArgumentCaptor.forClass(DemoSub.class);
        verify(repository).insert(captor.capture());
        DemoSub entity = captor.getValue();
        assertEquals("user-001", entity.getCreatorId());
        assertEquals("user-001", entity.getModifyId());
        assertEquals(0, entity.getDel());
        assertNotNull(entity.getAddTime());
        assertEquals(entity.getAddTime(), entity.getUpdateTime());
        assertTrue(entity.getAddTime() >= beforeSave);
    }

    private DemoSubSaveDTO saveDTO() {
        DemoSubMainDTO main = new DemoSubMainDTO();
        main.setName("demo-sub");
        DemoSubSaveDTO dto = new DemoSubSaveDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        dto.setMain(main);
        return dto;
    }
}
