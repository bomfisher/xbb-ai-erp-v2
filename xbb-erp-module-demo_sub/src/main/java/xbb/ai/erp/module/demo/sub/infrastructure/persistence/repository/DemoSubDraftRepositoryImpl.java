package xbb.ai.erp.module.demo.sub.infrastructure.persistence.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.demo.sub.application.pojo.DemoSubSaveDraftPojo;
import xbb.ai.erp.module.demo.sub.application.port.DemoSubDraftRepository;

@Repository
@RequiredArgsConstructor
public class DemoSubDraftRepositoryImpl implements DemoSubDraftRepository {
  private static final String PREFIX = "demo_sub:draft:";
  private static final Duration TTL = Duration.ofDays(7);
  private final StringRedisTemplate redis;
  private final ObjectMapper mapper;

  public String saveDraft(DemoSubSaveDraftPojo draft) {
    String code = draft.getDraftCode();
    if (code == null || code.isBlank()) code = UUID.randomUUID().toString();
    draft.setDraftCode(code);
    draft.setUpdatedTime(System.currentTimeMillis());
    redis.opsForValue().set(key(draft.getCorpid(), code), json(draft), TTL);
    redis.opsForZSet().add(index(draft.getCorpid()), code, draft.getUpdatedTime());
    redis.expire(index(draft.getCorpid()), TTL);
    return code;
  }

  public List<DemoSubSaveDraftPojo> listDrafts(String corpid, int limit) {
    Set<String> codes = redis.opsForZSet().reverseRange(index(corpid), 0, limit - 1L);
    if (codes == null) return List.of();
    return codes.stream().map(code -> loadDraft(corpid, code)).filter(Objects::nonNull).toList();
  }

  public DemoSubSaveDraftPojo loadDraft(String corpid, String code) {
    String value = redis.opsForValue().get(key(corpid, code));
    if (value == null) return null;
    try {
      return mapper.readValue(value, DemoSubSaveDraftPojo.class);
    } catch (JsonProcessingException ex) {
      throw new BizException("DEMO_SUB 草稿反序列化失败");
    }
  }

  public void removeDraft(String corpid, String code) {
    redis.delete(key(corpid, code));
    redis.opsForZSet().remove(index(corpid), code);
  }

  private String key(String corpid, String code) {
    return PREFIX + corpid + ":" + code;
  }

  private String index(String corpid) {
    return PREFIX + "index:" + corpid;
  }

  private String json(DemoSubSaveDraftPojo draft) {
    try {
      return mapper.writeValueAsString(draft);
    } catch (JsonProcessingException ex) {
      throw new BizException("DEMO_SUB 草稿序列化失败");
    }
  }
}
