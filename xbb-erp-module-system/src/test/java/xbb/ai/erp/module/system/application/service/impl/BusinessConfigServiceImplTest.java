package xbb.ai.erp.module.system.application.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import xbb.ai.erp.module.system.admin.dto.BusinessConfigGlobalSaveDTO;
import xbb.ai.erp.module.system.application.catalog.BusinessConfigCatalog;
import xbb.ai.erp.module.system.domain.model.BusinessConfig;
import xbb.ai.erp.module.system.domain.repository.BusinessConfigRepository;

class BusinessConfigServiceImplTest {

    private final BusinessConfigRepository repository = Mockito.mock(BusinessConfigRepository.class);
    private final StringRedisTemplate stringRedisTemplate = Mockito.mock(StringRedisTemplate.class);
    private final BusinessConfigServiceImpl service = new BusinessConfigServiceImpl(new BusinessConfigCatalog(), repository,
        stringRedisTemplate, new ObjectMapper());

    @AfterEach
    void clearSynchronization() {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    @Test
    void should_save_selected_documents_as_auto_approval() {
        when(repository.findByKey(any(), any())).thenReturn(null);
        TransactionSynchronizationManager.initSynchronization();
        BusinessConfigGlobalSaveDTO dto = new BusinessConfigGlobalSaveDTO();
        dto.setCorpid("corp-a");
        dto.setAutoApprovalBusinessCodes(List.of("PURCHASE_ORDER", "RECEIPT"));

        service.saveGlobal(dto);

        ArgumentCaptor<BusinessConfig> configCaptor = ArgumentCaptor.forClass(BusinessConfig.class);
        verify(repository, times(2)).save(configCaptor.capture());
        assertEquals("PURCHASE_ORDER", configCaptor.getAllValues().getFirst().businessCode());
        assertEquals("{\"PURCHASE_ORDER_APPROVAL_MODE\":\"AUTO\"}",
            configCaptor.getAllValues().getFirst().configJson());
        assertEquals(new BusinessConfig("corp-a", "RECEIPT", "{\"RECEIPT_APPROVAL_MODE\":\"AUTO\"}"),
            configCaptor.getAllValues().get(1));
    }
}
