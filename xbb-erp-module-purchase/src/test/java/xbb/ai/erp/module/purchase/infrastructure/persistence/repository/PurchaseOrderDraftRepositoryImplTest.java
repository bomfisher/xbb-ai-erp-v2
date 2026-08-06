package xbb.ai.erp.module.purchase.infrastructure.persistence.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderMainDTO;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseOrderSaveDraftPojo;

import java.time.Duration;
import java.util.LinkedHashSet;
import java.util.List;

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

class PurchaseOrderDraftRepositoryImplTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private StringRedisTemplate redisTemplate;
    private ValueOperations<String, String> valueOperations;
    private ZSetOperations<String, String> zSetOperations;
    private PurchaseOrderDraftRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        redisTemplate = mock(StringRedisTemplate.class);
        valueOperations = mock(ValueOperations.class);
        zSetOperations = mock(ZSetOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
        repository = new PurchaseOrderDraftRepositoryImpl(redisTemplate, objectMapper);
    }

    @Test
    void should_save_draft_with_ttl_and_index() {
        PurchaseOrderSaveDraftPojo draft = draft("corp-001", "draft-1", "草稿1", "PO-001", 20L, 100L);
        when(zSetOperations.zCard(PurchaseOrderDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-001")).thenReturn(1L);

        String draftCode = repository.saveDraft(draft);

        assertEquals("draft-1", draftCode);
        verify(valueOperations).set(
            eq(PurchaseOrderDraftRepositoryImpl.DRAFT_KEY_PREFIX + "corp-001:draft-1"),
            any(String.class),
            eq(Duration.ofDays(7))
        );
        verify(zSetOperations).add(PurchaseOrderDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-001", "draft-1", 100D);
        verify(redisTemplate).expire(PurchaseOrderDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-001", Duration.ofDays(7));
        verify(zSetOperations, never()).remove(eq(PurchaseOrderDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-001"), any());
    }

    @Test
    void should_list_load_and_remove_draft() throws Exception {
        String indexKey = PurchaseOrderDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-001";
        when(zSetOperations.reverseRange(indexKey, 0, 9L)).thenReturn(new LinkedHashSet<>(List.of("draft-2", "draft-1")));
        when(valueOperations.get(PurchaseOrderDraftRepositoryImpl.DRAFT_KEY_PREFIX + "corp-001:draft-2"))
            .thenReturn(objectMapper.writeValueAsString(draft("corp-001", "draft-2", "草稿2", "PO-002", 30L, 300L)));
        when(valueOperations.get(PurchaseOrderDraftRepositoryImpl.DRAFT_KEY_PREFIX + "corp-001:draft-1"))
            .thenReturn(objectMapper.writeValueAsString(draft("corp-001", "draft-1", "草稿1", "PO-001", 20L, 100L)));

        List<PurchaseOrderSaveDraftPojo> drafts = repository.listDrafts("corp-001", 10);
        PurchaseOrderSaveDraftPojo loaded = repository.loadDraft("corp-001", "draft-1");
        repository.removeDraft("corp-001", "draft-1");

        assertEquals(2, drafts.size());
        assertEquals("draft-2", drafts.get(0).getDraftCode());
        assertNotNull(loaded);
        assertEquals("PO-001", loaded.getMain().getOrderNo());
        verify(redisTemplate).delete(PurchaseOrderDraftRepositoryImpl.DRAFT_KEY_PREFIX + "corp-001:draft-1");
        verify(zSetOperations).remove(PurchaseOrderDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-001", "draft-1");
    }

    @Test
    void should_return_null_when_draft_not_found() {
        when(valueOperations.get(PurchaseOrderDraftRepositoryImpl.DRAFT_KEY_PREFIX + "corp-040:not-found")).thenReturn(null);

        PurchaseOrderSaveDraftPojo loaded = repository.loadDraft("corp-040", "not-found");

        assertNull(loaded);
    }

    @Test
    void should_throw_biz_exception_when_draft_deserialize_failed() {
        when(valueOperations.get(PurchaseOrderDraftRepositoryImpl.DRAFT_KEY_PREFIX + "corp-050:bad-json")).thenReturn("{bad json}");

        BizException ex = assertThrows(BizException.class, () -> repository.loadDraft("corp-050", "bad-json"));

        assertEquals("采购订单草稿反序列化失败", ex.getMessage());
    }

    @Test
    void should_generate_draft_code_when_missing() {
        PurchaseOrderSaveDraftPojo draft = draft("corp-030", null, "草稿A", "PO-030", 50L, null);
        when(zSetOperations.zCard(PurchaseOrderDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-030")).thenReturn(1L);

        String draftCode = repository.saveDraft(draft);

        assertNotNull(draftCode);
        assertTrue(!draftCode.isBlank());
        assertEquals(draftCode, draft.getDraftCode());
    }

    private PurchaseOrderSaveDraftPojo draft(
        String corpid,
        String draftCode,
        String draftTitle,
        String orderNo,
        Long vendorId,
        Long updatedTime
    ) {
        PurchaseOrderMainDTO main = new PurchaseOrderMainDTO();
        main.setOrderNo(orderNo);
        main.setVendorId(vendorId);

        PurchaseOrderSaveDraftPojo draft = new PurchaseOrderSaveDraftPojo();
        draft.setCorpid(corpid);
        draft.setDraftCode(draftCode);
        draft.setDraftTitle(draftTitle);
        draft.setMain(main);
        draft.setUpdatedTime(updatedTime);
        return draft;
    }
}
