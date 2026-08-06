package xbb.ai.erp.module.product.infrastructure.persistence.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.product.admin.dto.ProductMainDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSkuItemDTO;
import xbb.ai.erp.module.product.application.pojo.ProductSaveDraftPojo;

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

class ProductDraftRepositoryImplTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private StringRedisTemplate redisTemplate;
    private ValueOperations<String, String> valueOperations;
    private ZSetOperations<String, String> zSetOperations;
    private ProductDraftRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        redisTemplate = mock(StringRedisTemplate.class);
        valueOperations = mock(ValueOperations.class);
        zSetOperations = mock(ZSetOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
        repository = new ProductDraftRepositoryImpl(redisTemplate, objectMapper);
    }

    @Test
    void should_save_draft_with_ttl_and_index() {
        ProductSaveDraftPojo draft = draft("corp-001", 11L, "draft-1", "草稿1", "SPU-001", "商品1", 100L);
        when(zSetOperations.zCard(ProductDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-001")).thenReturn(1L);

        String draftCode = repository.saveDraft(draft);

        assertEquals("draft-1", draftCode);
        verify(valueOperations).set(
            eq(ProductDraftRepositoryImpl.DRAFT_KEY_PREFIX + "corp-001:11"),
            any(String.class),
            eq(Duration.ofDays(7))
        );
        verify(zSetOperations).add(ProductDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-001", "11", 100D);
        verify(redisTemplate).expire(ProductDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-001", Duration.ofDays(7));
        verify(zSetOperations, never()).remove(eq(ProductDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-001"), any());
    }

    @Test
    void should_trim_oldest_draft_when_count_exceeds_ten() {
        ProductSaveDraftPojo draft = draft("corp-010", 11L, "draft-11", "草稿11", "SPU-011", "商品11", 110L);
        String indexKey = ProductDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-010";
        when(zSetOperations.zCard(indexKey)).thenReturn(11L);
        when(zSetOperations.range(indexKey, 0, 0)).thenReturn(Set.of("1"));

        repository.saveDraft(draft);

        verify(zSetOperations).remove(indexKey, "1");
        verify(redisTemplate).delete(ProductDraftRepositoryImpl.DRAFT_KEY_PREFIX + "corp-010:1");
    }

    @Test
    void should_list_latest_drafts_and_cleanup_stale_index_items() throws Exception {
        String indexKey = ProductDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-001";
        when(zSetOperations.reverseRange(indexKey, 0, 9L)).thenReturn(new LinkedHashSet<>(List.of("12", "99", "11")));
        when(valueOperations.get(ProductDraftRepositoryImpl.DRAFT_KEY_PREFIX + "corp-001:12"))
            .thenReturn(objectMapper.writeValueAsString(draft("corp-001", 12L, "draft-2", "草稿2", "SPU-002", "商品2", 300L)));
        when(valueOperations.get(ProductDraftRepositoryImpl.DRAFT_KEY_PREFIX + "corp-001:99"))
            .thenReturn(null);
        when(valueOperations.get(ProductDraftRepositoryImpl.DRAFT_KEY_PREFIX + "corp-001:11"))
            .thenReturn(objectMapper.writeValueAsString(draft("corp-001", 11L, "draft-1", "草稿1", "SPU-001", "商品1", 100L)));

        List<ProductSaveDraftPojo> drafts = repository.listDrafts("corp-001", 10);

        assertEquals(2, drafts.size());
        assertEquals("draft-2", drafts.get(0).getDraftCode());
        assertEquals("draft-1", drafts.get(1).getDraftCode());
        verify(zSetOperations).remove(indexKey, "99");
    }

    @Test
    void should_load_and_remove_draft() throws Exception {
        String draftKey = ProductDraftRepositoryImpl.DRAFT_KEY_PREFIX + "corp-020:21";
        when(zSetOperations.reverseRange(ProductDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-020", 0, 9L)).thenReturn(Set.of("21"));
        when(valueOperations.get(draftKey))
            .thenReturn(objectMapper.writeValueAsString(draft("corp-020", 21L, "draft-remove", "草稿A", "SPU-001", "商品A", 100L)));

        ProductSaveDraftPojo loaded = repository.loadDraft("corp-020", 21L);
        repository.removeDraft("corp-020", "draft-remove");

        assertNotNull(loaded);
        assertEquals("草稿A", loaded.getDraftName());
        verify(redisTemplate).delete(draftKey);
        verify(zSetOperations).remove(ProductDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-020", "21");
    }

    @Test
    void should_generate_draft_id_and_code_when_missing() {
        ProductSaveDraftPojo draft = draft("corp-030", null, null, "草稿A", "SPU-001", "商品A", null);
        when(zSetOperations.zCard(ProductDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-030")).thenReturn(1L);

        String draftCode = repository.saveDraft(draft);

        assertNotNull(draftCode);
        assertTrue(!draftCode.isBlank());
        assertNotNull(draft.getDraftId());
        verify(zSetOperations).add(
            ProductDraftRepositoryImpl.DRAFT_INDEX_KEY_PREFIX + "corp-030",
            String.valueOf(draft.getDraftId()),
            draft.getUpdatedTime().doubleValue()
        );
    }

    @Test
    void should_return_null_when_draft_not_found() {
        when(valueOperations.get(ProductDraftRepositoryImpl.DRAFT_KEY_PREFIX + "corp-040:404")).thenReturn(null);

        ProductSaveDraftPojo loaded = repository.loadDraft("corp-040", 404L);

        assertNull(loaded);
    }

    @Test
    void should_throw_biz_exception_when_draft_deserialize_failed() {
        when(valueOperations.get(ProductDraftRepositoryImpl.DRAFT_KEY_PREFIX + "corp-050:501")).thenReturn("{bad json}");

        BizException ex = assertThrows(BizException.class, () -> repository.loadDraft("corp-050", 501L));

        assertEquals("商品草稿反序列化失败", ex.getMessage());
    }

    private ProductSaveDraftPojo draft(String corpid, Long draftId, String draftCode, String draftName, String spuCode, String spuName, Long updatedTime) {
        ProductMainDTO main = new ProductMainDTO();
        main.setSpuCode(spuCode);
        main.setSpuName(spuName);
        main.setEnableSpec(0);
        main.setProductType("NORMAL");
        main.setSpuEnableStatus(1);

        ProductSkuItemDTO sku = new ProductSkuItemDTO();
        sku.setSkuCode("SKU-001");
        sku.setSkuName("默认SKU");
        sku.setSpecSignature("DEFAULT");
        sku.setSpecSnapshot("DEFAULT");
        sku.setSkuEnableStatus(1);
        sku.setListingStatus(1);

        ProductSaveDraftPojo draft = new ProductSaveDraftPojo();
        draft.setCorpid(corpid);
        draft.setDraftId(draftId);
        draft.setDraftCode(draftCode);
        draft.setDraftName(draftName);
        draft.setMain(main);
        draft.setSkus(List.of(sku));
        draft.setUpdatedTime(updatedTime);
        return draft;
    }
}
