package xbb.ai.erp.module.customer.infrastructure.persistence.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.customer.application.pojo.CustomerSaveDraftPojo;
import xbb.ai.erp.module.customer.domain.repository.CustomerDraftRepository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CustomerDraftRepositoryImpl implements CustomerDraftRepository {

    static final String DRAFT_KEY_PREFIX = "customer:draft:";
    static final String DRAFT_INDEX_KEY_PREFIX = "customer:draft:index:";
    static final Duration DRAFT_TTL = Duration.ofDays(7);
    static final int MAX_DRAFT_COUNT = 10;

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public String saveDraft(CustomerSaveDraftPojo draft) {
        String draftCode = draft.getDraftCode();
        if (draftCode == null || draftCode.isBlank()) {
            draftCode = UUID.randomUUID().toString();
            draft.setDraftCode(draftCode);
        }
        Long updatedTime = draft.getUpdatedTime();
        if (updatedTime == null) {
            updatedTime = System.currentTimeMillis();
            draft.setUpdatedTime(updatedTime);
        }

        String draftKey = buildDraftKey(draft.getCorpid(), draftCode);
        String indexKey = buildIndexKey(draft.getCorpid());
        redisTemplate.opsForValue().set(draftKey, toJson(draft), DRAFT_TTL);
        redisTemplate.opsForZSet().add(indexKey, draftCode, updatedTime.doubleValue());
        redisTemplate.expire(indexKey, DRAFT_TTL);
        trimOverflowDrafts(draft.getCorpid(), indexKey);
        return draftCode;
    }

    @Override
    public List<CustomerSaveDraftPojo> listDrafts(String corpid, int limit) {
        if (limit <= 0) {
            return List.of();
        }
        Set<String> draftCodes = redisTemplate.opsForZSet().reverseRange(buildIndexKey(corpid), 0, limit - 1L);
        if (draftCodes == null || draftCodes.isEmpty()) {
            return List.of();
        }
        List<CustomerSaveDraftPojo> drafts = new ArrayList<>();
        List<String> staleDraftCodes = new ArrayList<>();
        for (Object draftCode : draftCodes) {
            CustomerSaveDraftPojo draft = loadDraft(corpid, String.valueOf(draftCode));
            if (draft == null) {
                staleDraftCodes.add(String.valueOf(draftCode));
                continue;
            }
            drafts.add(draft);
        }
        if (!staleDraftCodes.isEmpty()) {
            redisTemplate.opsForZSet().remove(buildIndexKey(corpid), staleDraftCodes.toArray());
        }
        return drafts;
    }

    @Override
    public CustomerSaveDraftPojo loadDraft(String corpid, String draftCode) {
        Object value = redisTemplate.opsForValue().get(buildDraftKey(corpid, draftCode));
        if (!(value instanceof String payload) || payload.isBlank()) {
            return null;
        }
        return fromJson(payload);
    }

    @Override
    public void removeDraft(String corpid, String draftCode) {
        redisTemplate.delete(buildDraftKey(corpid, draftCode));
        redisTemplate.opsForZSet().remove(buildIndexKey(corpid), draftCode);
    }

    private void trimOverflowDrafts(String corpid, String indexKey) {
        Long draftCount = redisTemplate.opsForZSet().zCard(indexKey);
        if (draftCount == null || draftCount <= MAX_DRAFT_COUNT) {
            return;
        }
        long overflowCount = draftCount - MAX_DRAFT_COUNT;
        Set<String> overflowDraftCodes = redisTemplate.opsForZSet().range(indexKey, 0, overflowCount - 1);
        if (overflowDraftCodes == null || overflowDraftCodes.isEmpty()) {
            return;
        }
        redisTemplate.opsForZSet().remove(indexKey, overflowDraftCodes.toArray());
        for (Object overflowDraftCode : overflowDraftCodes) {
            redisTemplate.delete(buildDraftKey(corpid, String.valueOf(overflowDraftCode)));
        }
    }

    private String buildDraftKey(String corpid, String draftCode) {
        return DRAFT_KEY_PREFIX + corpid + ":" + draftCode;
    }

    private String buildIndexKey(String corpid) {
        return DRAFT_INDEX_KEY_PREFIX + corpid;
    }

    private String toJson(CustomerSaveDraftPojo draft) {
        try {
            return objectMapper.writeValueAsString(draft);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("客户草稿序列化失败", ex);
        }
    }

    private CustomerSaveDraftPojo fromJson(String payload) {
        try {
            return objectMapper.readValue(payload, CustomerSaveDraftPojo.class);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("客户草稿反序列化失败", ex);
        }
    }
}
