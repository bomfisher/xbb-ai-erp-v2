package xbb.ai.erp.module.purchase.infrastructure.persistence.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestMainDTO;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseRequestSaveDraftPojo;

import java.time.Duration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PurchaseRequestDraftRepositoryImplTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private StringRedisTemplate redisTemplate;
    private ValueOperations<String, String> valueOperations;
    private ZSetOperations<String, String> zSetOperations;
    private PurchaseRequestDraftRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        redisTemplate = mock(StringRedisTemplate.class);
        valueOperations = mock(ValueOperations.class);
        zSetOperations = mock(ZSetOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
        repository = new PurchaseRequestDraftRepositoryImpl(redisTemplate, objectMapper);
    }

    @Test
    void should_save_draft_with_ttl_and_index() {
        PurchaseRequestSaveDraftPojo draft = draft("corp-001", "draft-1", "草稿1", "PR-001", "emp-001", 100L);
        when(zSetOperations.zCard(PurchaseRequestDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-001")).thenReturn(1L);

        String draftCode = repository.saveDraft(draft);

        assertEquals("draft-1", draftCode);
        verify(valueOperations).set(
            eq(PurchaseRequestDraftRepositoryImpl.DRAFT_KEY_PREFIX + "corp-001:draft-1"),
            any(String.class),
            eq(Duration.ofDays(7))
        );
        verify(zSetOperations).add(PurchaseRequestDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-001", "draft-1", 100D);
        verify(redisTemplate).expire(PurchaseRequestDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-001", Duration.ofDays(7));
        verify(zSetOperations, never()).remove(eq(PurchaseRequestDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-001"), any());
    }

    @Test
    void should_list_load_and_remove_draft() throws Exception {
        String indexKey = PurchaseRequestDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-001";
        when(zSetOperations.reverseRange(indexKey, 0, 9L)).thenReturn(new LinkedHashSet<>(List.of("draft-2", "draft-1")));
        when(valueOperations.get(PurchaseRequestDraftRepositoryImpl.DRAFT_KEY_PREFIX + "corp-001:draft-2"))
            .thenReturn(objectMapper.writeValueAsString(draft("corp-001", "draft-2", "草稿2", "PR-002", "emp-002", 300L)));
        when(valueOperations.get(PurchaseRequestDraftRepositoryImpl.DRAFT_KEY_PREFIX + "corp-001:draft-1"))
            .thenReturn(objectMapper.writeValueAsString(draft("corp-001", "draft-1", "草稿1", "PR-001", "emp-001", 100L)));

        List<PurchaseRequestSaveDraftPojo> drafts = repository.listDrafts("corp-001", 10);
        PurchaseRequestSaveDraftPojo loaded = repository.loadDraft("corp-001", "draft-1");
        repository.removeDraft("corp-001", "draft-1");

        assertEquals(2, drafts.size());
        assertEquals("draft-2", drafts.get(0).getDraftCode());
        assertNotNull(loaded);
        assertEquals("PR-001", loaded.getMain().getRequestNo());
        verify(redisTemplate).delete(PurchaseRequestDraftRepositoryImpl.DRAFT_KEY_PREFIX + "corp-001:draft-1");
        verify(zSetOperations).remove(PurchaseRequestDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-001", "draft-1");
    }

    @Test
    void should_return_null_when_draft_not_found() {
        when(valueOperations.get(PurchaseRequestDraftRepositoryImpl.DRAFT_KEY_PREFIX + "corp-040:not-found")).thenReturn(null);

        PurchaseRequestSaveDraftPojo loaded = repository.loadDraft("corp-040", "not-found");

        assertNull(loaded);
    }

    @Test
    void should_throw_biz_exception_when_draft_deserialize_failed() {
        when(valueOperations.get(PurchaseRequestDraftRepositoryImpl.DRAFT_KEY_PREFIX + "corp-050:bad-json")).thenReturn("{bad json}");

        BizException ex = assertThrows(BizException.class, () -> repository.loadDraft("corp-050", "bad-json"));

        assertEquals("采购申请草稿反序列化失败", ex.getMessage());
    }

    @Test
    void should_generate_draft_code_when_missing() {
        PurchaseRequestSaveDraftPojo draft = draft("corp-030", null, "草稿A", "PR-030", "emp-030", null);
        when(zSetOperations.zCard(PurchaseRequestDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-030")).thenReturn(1L);

        String draftCode = repository.saveDraft(draft);

        assertNotNull(draftCode);
        assertTrue(!draftCode.isBlank());
        assertEquals(draftCode, draft.getDraftCode());
    }

    private PurchaseRequestSaveDraftPojo draft(
        String corpid,
        String draftCode,
        String draftTitle,
        String requestNo,
        String applicantId,
        Long updatedTime
    ) {
        PurchaseRequestMainDTO main = new PurchaseRequestMainDTO();
        main.setRequestNo(requestNo);
        main.setApplicantId(applicantId);

        PurchaseRequestSaveDraftPojo draft = new PurchaseRequestSaveDraftPojo();
        draft.setCorpid(corpid);
        draft.setDraftCode(draftCode);
        draft.setDraftTitle(draftTitle);
        draft.setMain(main);
        draft.setUpdatedTime(updatedTime);
        return draft;
    }
}
