package xbb.ai.erp.module.system.application.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import xbb.ai.erp.base.bizno.BizNoRuleTypeEnum;
import xbb.ai.erp.module.system.domain.model.BizNoRule;
import xbb.ai.erp.module.system.domain.repository.BizNoCounterRepository;
import xbb.ai.erp.module.system.domain.repository.BizNoRuleRepository;

@SuppressWarnings("unchecked")
class RedisBizNoGeneratorTest {

    private static final String CORPID = "corp-a";
    private static final String BUSINESS_CODE = "PRODUCT_SPU";
    private static final String KEY = "erp:biz-no:master-data:corp-a:PRODUCT_SPU";

    private final StringRedisTemplate stringRedisTemplate = Mockito.mock(StringRedisTemplate.class);
    private final HashOperations<String, Object, Object> hashOperations = Mockito.mock(HashOperations.class);
    private final ValueOperations<String, String> valueOperations = Mockito.mock(ValueOperations.class);
    private final BizNoRuleRepository ruleRepository = Mockito.mock(BizNoRuleRepository.class);
    private final BizNoCounterRepository counterRepository = Mockito.mock(BizNoCounterRepository.class);
    private final RedisBizNoGenerator generator = new RedisBizNoGenerator(
        stringRedisTemplate, ruleRepository, counterRepository);

    @Test
    void shouldCreateMasterDataSegmentWithoutLuaWhenRedisKeyIsMissing() {
        when(ruleRepository.findRequired(CORPID, BUSINESS_CODE))
            .thenReturn(new BizNoRule(CORPID, BUSINESS_CODE, "SPU", BizNoRuleTypeEnum.MASTER_DATA));
        when(stringRedisTemplate.opsForHash()).thenReturn(hashOperations);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(hashOperations.increment(KEY, "next", 1)).thenReturn(1L);
        when(hashOperations.get(KEY, "end")).thenReturn(null);
        when(valueOperations.setIfAbsent(Mockito.startsWith(KEY), Mockito.anyString(), Mockito.any()))
            .thenReturn(true);
        when(counterRepository.reserve(CORPID, BUSINESS_CODE, "GLOBAL", 1_000)).thenReturn(1_000L);
        when(valueOperations.get(Mockito.startsWith(KEY))).thenReturn(null);

        assertEquals("SPU-00001", generator.next(CORPID, BUSINESS_CODE));

        verify(hashOperations).put(KEY, "end", "1000");
        verify(stringRedisTemplate, never()).execute(Mockito.any(), Mockito.anyList());
    }

    @Test
    void should_use_configured_suffix_length() {
        when(ruleRepository.findRequired(CORPID, BUSINESS_CODE))
            .thenReturn(new BizNoRule(CORPID, BUSINESS_CODE, "SPU", 0, 3,
                xbb.ai.erp.module.system.domain.model.BizNoSerialModeEnum.CONTINUOUS, BizNoRuleTypeEnum.MASTER_DATA));
        when(stringRedisTemplate.opsForHash()).thenReturn(hashOperations);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(hashOperations.increment(KEY, "next", 1)).thenReturn(1L);
        when(hashOperations.get(KEY, "end")).thenReturn("1000");

        assertEquals("SPU-001", generator.next(CORPID, BUSINESS_CODE));
    }
}
