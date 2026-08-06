package xbb.ai.erp.module.product.infrastructure.persistence.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.product.application.pojo.ProductSaveDraftPojo;
import xbb.ai.erp.module.product.application.port.ProductDraftRepository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductDraftRepositoryImpl implements ProductDraftRepository {

    static final String DRAFT_KEY_PREFIX = "product:draft:";
    static final String DRAFT_INDEX_KEY_PREFIX = "product:draft:index:";
    static final Duration DRAFT_TTL = Duration.ofDays(7);
    static final int MAX_DRAFT_COUNT = 10;

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public String saveDraft(ProductSaveDraftPojo draft) {
        String draftCode = draft.getDraftCode();
        if (draftCode == null || draftCode.isBlank()) {
            draftCode = UUID.randomUUID().toString();
            draft.setDraftCode(draftCode);
        }
        Long draftId = draft.getDraftId();
        if (draftId == null) {
            draftId = Math.abs(UUID.randomUUID().getMostSignificantBits());
            if (draftId == 0L) {
                draftId = 1L;
            }
            draft.setDraftId(draftId);
        }
        Long updatedTime = draft.getUpdatedTime();
        if (updatedTime == null) {
            updatedTime = System.currentTimeMillis();
            draft.setUpdatedTime(updatedTime);
        }

        String draftKey = buildDraftKey(draft.getCorpid(), draftId);
        String indexKey = buildIndexKey(draft.getCorpid());
        redisTemplate.opsForValue().set(draftKey, toJson(draft), DRAFT_TTL);
        redisTemplate.opsForZSet().add(indexKey, String.valueOf(draftId), updatedTime.doubleValue());
        redisTemplate.expire(indexKey, DRAFT_TTL);
        trimOverflowDrafts(draft.getCorpid(), indexKey);
        return draftCode;
    }

    @Override
    public List<ProductSaveDraftPojo> listDrafts(String corpid, int limit) {
        if (limit <= 0) {
            return List.of();
        }
        Set<String> draftIds = redisTemplate.opsForZSet().reverseRange(buildIndexKey(corpid), 0, limit - 1L);
        if (draftIds == null || draftIds.isEmpty()) {
            return List.of();
        }
        List<ProductSaveDraftPojo> drafts = new ArrayList<>();
        List<String> staleDraftIds = new ArrayList<>();
        for (Object draftId : draftIds) {
            ProductSaveDraftPojo draft = loadDraft(corpid, Long.valueOf(String.valueOf(draftId)));
            if (draft == null) {
                staleDraftIds.add(String.valueOf(draftId));
                continue;
            }
            drafts.add(draft);
        }
        if (!staleDraftIds.isEmpty()) {
            redisTemplate.opsForZSet().remove(buildIndexKey(corpid), staleDraftIds.toArray());
        }
        return drafts;
    }

    @Override
    public ProductSaveDraftPojo loadDraft(String corpid, Long draftId) {
        if (draftId == null) {
            return null;
        }
        Object value = redisTemplate.opsForValue().get(buildDraftKey(corpid, draftId));
        if (!(value instanceof String payload) || payload.isBlank()) {
            return null;
        }
        return fromJson(payload);
    }

    @Override
    public void removeDraft(String corpid, String draftCode) {
        if (draftCode == null || draftCode.isBlank()) {
            return;
        }
        List<ProductSaveDraftPojo> drafts = listDrafts(corpid, MAX_DRAFT_COUNT);
        for (ProductSaveDraftPojo draft : drafts) {
            if (draftCode.equals(draft.getDraftCode()) && draft.getDraftId() != null) {
                redisTemplate.delete(buildDraftKey(corpid, draft.getDraftId()));
                redisTemplate.opsForZSet().remove(buildIndexKey(corpid), String.valueOf(draft.getDraftId()));
                break;
            }
        }
    }

    private void trimOverflowDrafts(String corpid, String indexKey) {
        Long draftCount = redisTemplate.opsForZSet().zCard(indexKey);
        if (draftCount == null || draftCount <= MAX_DRAFT_COUNT) {
            return;
        }
        long overflowCount = draftCount - MAX_DRAFT_COUNT;
        Set<String> overflowDraftIds = redisTemplate.opsForZSet().range(indexKey, 0, overflowCount - 1);
        if (overflowDraftIds == null || overflowDraftIds.isEmpty()) {
            return;
        }
        redisTemplate.opsForZSet().remove(indexKey, overflowDraftIds.toArray());
        for (Object overflowDraftId : overflowDraftIds) {
            redisTemplate.delete(buildDraftKey(corpid, Long.valueOf(String.valueOf(overflowDraftId))));
        }
    }

    private String buildDraftKey(String corpid, Long draftId) {
        return DRAFT_KEY_PREFIX + corpid + ":" + draftId;
    }

    private String buildIndexKey(String corpid) {
        return DRAFT_INDEX_KEY_PREFIX + corpid;
    }

    private String toJson(ProductSaveDraftPojo draft) {
        try {
            return objectMapper.writeValueAsString(draft);
        } catch (JsonProcessingException ex) {
            throw new BizException("商品草稿序列化失败");
        }
    }

    private ProductSaveDraftPojo fromJson(String payload) {
        try {
            return objectMapper.readValue(payload, ProductSaveDraftPojo.class);
        } catch (JsonProcessingException ex) {
            throw new BizException("商品草稿反序列化失败");
        }
    }
}
