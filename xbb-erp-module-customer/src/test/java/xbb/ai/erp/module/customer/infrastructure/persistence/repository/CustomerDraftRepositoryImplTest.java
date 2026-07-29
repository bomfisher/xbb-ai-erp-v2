package xbb.ai.erp.module.customer.infrastructure.persistence.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import xbb.ai.erp.module.customer.admin.dto.CustomerMainDTO;
import xbb.ai.erp.module.customer.application.pojo.CustomerSaveDraftPojo;

import java.time.Duration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CustomerDraftRepositoryImplTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private StringRedisTemplate redisTemplate;
    private ValueOperations<String, String> valueOperations;
    private ZSetOperations<String, String> zSetOperations;
    private CustomerDraftRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        redisTemplate = mock(StringRedisTemplate.class);
        valueOperations = mock(ValueOperations.class);
        zSetOperations = mock(ZSetOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
        repository = new CustomerDraftRepositoryImpl(redisTemplate, objectMapper);
    }

    @Test
    void should_save_draft_with_ttl_and_index() {
        CustomerSaveDraftPojo draft = draft("corp-001", "draft-1", "草稿1", "CUST-001", "客户1", 100L);
        when(zSetOperations.zCard(CustomerDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-001")).thenReturn(1L);

        String draftCode = repository.saveDraft(draft);

        assertEquals("draft-1", draftCode);
        verify(valueOperations).set(
            eq(CustomerDraftRepositoryImpl.DRAFT_KEY_PREFIX + "corp-001:draft-1"),
            any(String.class),
            eq(Duration.ofDays(7))
        );
        verify(zSetOperations).add(CustomerDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-001", "draft-1", 100D);
        verify(redisTemplate).expire(CustomerDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-001", Duration.ofDays(7));
        verify(zSetOperations, never()).remove(eq(CustomerDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-001"), any());
    }

    @Test
    void should_trim_oldest_draft_when_count_exceeds_ten() {
        CustomerSaveDraftPojo draft = draft("corp-010", "draft-11", "草稿11", "CUST-011", "客户11", 110L);
        String indexKey = CustomerDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-010";
        when(zSetOperations.zCard(indexKey)).thenReturn(11L);
        when(zSetOperations.range(indexKey, 0, 0)).thenReturn(Set.of("draft-1"));

        repository.saveDraft(draft);

        verify(zSetOperations).remove(indexKey, "draft-1");
        verify(redisTemplate).delete(CustomerDraftRepositoryImpl.DRAFT_KEY_PREFIX + "corp-010:draft-1");
    }

    @Test
    void should_list_latest_drafts_and_cleanup_stale_index_items() throws Exception {
        String indexKey = CustomerDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-001";
        when(zSetOperations.reverseRange(indexKey, 0, 9L)).thenReturn(new LinkedHashSet<>(List.of("draft-2", "draft-missing", "draft-1")));
        when(valueOperations.get(CustomerDraftRepositoryImpl.DRAFT_KEY_PREFIX + "corp-001:draft-2"))
            .thenReturn(objectMapper.writeValueAsString(draft("corp-001", "draft-2", "草稿2", "CUST-002", "客户2", 300L)));
        when(valueOperations.get(CustomerDraftRepositoryImpl.DRAFT_KEY_PREFIX + "corp-001:draft-missing"))
            .thenReturn(null);
        when(valueOperations.get(CustomerDraftRepositoryImpl.DRAFT_KEY_PREFIX + "corp-001:draft-1"))
            .thenReturn(objectMapper.writeValueAsString(draft("corp-001", "draft-1", "草稿1", "CUST-001", "客户1", 100L)));

        List<CustomerSaveDraftPojo> drafts = repository.listDrafts("corp-001", 10);

        assertEquals(2, drafts.size());
        assertEquals("draft-2", drafts.get(0).getDraftCode());
        assertEquals("draft-1", drafts.get(1).getDraftCode());
        verify(zSetOperations).remove(indexKey, "draft-missing");
    }

    @Test
    void should_load_and_remove_draft() throws Exception {
        String draftKey = CustomerDraftRepositoryImpl.DRAFT_KEY_PREFIX + "corp-020:draft-remove";
        when(valueOperations.get(draftKey))
            .thenReturn(objectMapper.writeValueAsString(draft("corp-020", "draft-remove", "草稿A", "CUST-001", "客户A", 100L)));

        CustomerSaveDraftPojo loaded = repository.loadDraft("corp-020", "draft-remove");
        repository.removeDraft("corp-020", "draft-remove");

        assertNotNull(loaded);
        assertEquals("草稿A", loaded.getDraftTitle());
        verify(redisTemplate).delete(draftKey);
        verify(zSetOperations).remove(CustomerDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-020", "draft-remove");
    }

    @Test
    void should_generate_draft_code_when_missing() {
        CustomerSaveDraftPojo draft = draft("corp-030", null, "草稿A", "CUST-001", "客户A", null);
        when(zSetOperations.zCard(CustomerDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-030")).thenReturn(1L);

        String draftCode = repository.saveDraft(draft);

        assertNotNull(draftCode);
        assertTrue(!draftCode.isBlank());
        assertEquals(draftCode, draft.getDraftCode());
        verify(zSetOperations).add(
            CustomerDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-030",
            draftCode,
            draft.getUpdatedTime().doubleValue()
        );
    }

    @Test
    void should_return_null_when_draft_not_found() {
        when(valueOperations.get(CustomerDraftRepositoryImpl.DRAFT_KEY_PREFIX + "corp-040:not-found")).thenReturn(null);

        CustomerSaveDraftPojo loaded = repository.loadDraft("corp-040", "not-found");

        assertNull(loaded);
    }

    private CustomerSaveDraftPojo draft(
        String corpid,
        String draftCode,
        String draftTitle,
        String customerCode,
        String customerName,
        Long updatedTime
    ) {
        CustomerMainDTO main = new CustomerMainDTO();
        main.setCustomerCode(customerCode);
        main.setCustomerName(customerName);

        CustomerSaveDraftPojo draft = new CustomerSaveDraftPojo();
        draft.setCorpid(corpid);
        draft.setDraftCode(draftCode);
        draft.setDraftTitle(draftTitle);
        draft.setMain(main);
        draft.setUpdatedTime(updatedTime);
        return draft;
    }
}
