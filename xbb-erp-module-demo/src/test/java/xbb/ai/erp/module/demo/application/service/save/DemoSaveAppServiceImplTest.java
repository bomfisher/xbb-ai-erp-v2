package xbb.ai.erp.module.demo.application.service.save;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doAnswer;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import xbb.ai.erp.module.demo.admin.dto.DemoMainDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoSaveDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoSubmitSaveDTO;
import xbb.ai.erp.module.demo.admin.dto.DemoItemDTO;
import xbb.ai.erp.module.demo.application.port.DemoDraftRepository;
import xbb.ai.erp.module.demo.application.validator.DemoSaveBusinessValidator;
import xbb.ai.erp.module.demo.application.validator.DemoSaveCommonValidator;
import xbb.ai.erp.module.demo.application.validator.DemoSaveProtocolValidator;
import xbb.ai.erp.module.demo.domain.model.Demo;
import xbb.ai.erp.module.demo.domain.model.DemoItem;
import xbb.ai.erp.module.demo.domain.repository.DemoRepository;
import xbb.ai.erp.module.demo.domain.repository.DemoItemRepository;
import java.util.List;

class DemoSaveAppServiceImplTest {

    @Test
    void shouldInitializeAuditFieldsAndDelBeforeInsert() {
        DemoRepository repository = mock(DemoRepository.class);
        DemoSaveAppServiceImpl service = new DemoSaveAppServiceImpl(
            repository,
            mock(DemoItemRepository.class),
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
        assertNull(entity.getId());
        assertEquals("user-001", entity.getCreatorId());
        assertEquals("user-001", entity.getModifyId());
        assertEquals(0, entity.getDel());
        assertNotNull(entity.getAddTime());
        assertEquals(entity.getAddTime(), entity.getUpdateTime());
        assertTrue(entity.getAddTime() >= beforeSave);
    }

    @Test
    void shouldSynchronizeDemoItemsByIdAfterSavingMainRecord() {
        DemoRepository repository = mock(DemoRepository.class);
        DemoItemRepository itemRepository = mock(DemoItemRepository.class);
        DemoSaveAppServiceImpl service = new DemoSaveAppServiceImpl(
            repository, itemRepository, mock(DemoDraftRepository.class), mock(DemoSaveProtocolValidator.class),
            mock(DemoSaveCommonValidator.class), mock(DemoSaveBusinessValidator.class));
        DemoItemDTO first = new DemoItemDTO();
        first.setId(10L);
        first.setName("子项A已修改");
        DemoItemDTO secondGroupItem = new DemoItemDTO();
        secondGroupItem.setName("子项C");
        DemoSubmitSaveDTO dto = new DemoSubmitSaveDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        dto.setMain(new DemoMainDTO());
        dto.setItems(List.of(first));
        dto.setItems2(List.of(secondGroupItem));
        DemoItem existingFirst = new DemoItem();
        existingFirst.setId(10L);
        DemoItem existingSecond = new DemoItem();
        existingSecond.setId(20L);
        org.mockito.Mockito.when(itemRepository.findByDataId("corp-001", 100L))
            .thenReturn(List.of(existingFirst, existingSecond));
        doAnswer(invocation -> {
            invocation.getArgument(0, Demo.class).setId(100L);
            return null;
        }).when(repository).insert(org.mockito.ArgumentMatchers.any(Demo.class));

        service.saveAndSubmit(dto);

        verify(itemRepository).removeBatchByIds("corp-001", List.of(20L));
        ArgumentCaptor<DemoItem> updateCaptor = ArgumentCaptor.forClass(DemoItem.class);
        verify(itemRepository).update(updateCaptor.capture());
        assertEquals(10L, updateCaptor.getValue().getId());
        assertEquals("子项A已修改", updateCaptor.getValue().getName());
        ArgumentCaptor<List<xbb.ai.erp.module.demo.domain.model.DemoItem>> captor = ArgumentCaptor.forClass(List.class);
        verify(itemRepository).insertBatch(captor.capture());
        assertEquals(1, captor.getValue().size());
        assertEquals(100L, captor.getValue().getFirst().getDataId());
        assertNull(captor.getValue().getFirst().getId());
        assertEquals("子项C-2", captor.getValue().getLast().getName());
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
