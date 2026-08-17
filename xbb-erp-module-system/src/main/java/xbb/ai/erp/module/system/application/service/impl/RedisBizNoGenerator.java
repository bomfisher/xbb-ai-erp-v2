package xbb.ai.erp.module.system.application.service.impl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.bizno.BizNoRuleTypeEnum;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.system.domain.model.BizNoRule;
import xbb.ai.erp.module.system.domain.repository.BizNoCounterRepository;
import xbb.ai.erp.module.system.domain.repository.BizNoRuleRepository;

@Service
@RequiredArgsConstructor
public class RedisBizNoGenerator implements BizNoGenerator {

    private static final int MASTER_DATA_SEGMENT_SIZE = 1_000;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.BASIC_ISO_DATE;
    private static final DefaultRedisScript<Long> TAKE_SEGMENT_VALUE = new DefaultRedisScript<>(
        "local nextValue = redis.call('HGET', KEYS[1], 'next'); "
            + "local endValue = redis.call('HGET', KEYS[1], 'end'); "
            + "if (not nextValue) or (tonumber(nextValue) > tonumber(endValue)) then return nil; end; "
            + "redis.call('HINCRBY', KEYS[1], 'next', 1); return tonumber(nextValue);", Long.class);
    private static final DefaultRedisScript<Long> INSTALL_SEGMENT_IF_EMPTY = new DefaultRedisScript<>(
        "local nextValue = redis.call('HGET', KEYS[1], 'next'); "
            + "local endValue = redis.call('HGET', KEYS[1], 'end'); "
            + "if (not nextValue) or (tonumber(nextValue) > tonumber(endValue)) then "
            + "redis.call('HSET', KEYS[1], 'next', ARGV[1], 'end', ARGV[2]); return 1; end; return 0;", Long.class);

    private final StringRedisTemplate stringRedisTemplate;
    private final BizNoRuleRepository ruleRepository;
    private final BizNoCounterRepository counterRepository;

    @Override
    public String next(String corpid, String businessCode) {
        validate(corpid, businessCode);
        BizNoRule rule = ruleRepository.findRequired(corpid, businessCode);
        long serial = rule.ruleType() == BizNoRuleTypeEnum.MASTER_DATA
            ? nextMasterDataSerial(corpid, businessCode)
            : nextDocumentSerial(corpid, businessCode);
        if (rule.ruleType() == BizNoRuleTypeEnum.MASTER_DATA) {
            return rule.prefix() + "-" + formatSerial(serial);
        }
        return rule.prefix() + "-" + LocalDate.now().format(DATE_FORMATTER) + "-" + formatSerial(serial);
    }

    private long nextDocumentSerial(String corpid, String businessCode) {
        String date = LocalDate.now().format(DATE_FORMATTER);
        String key = "erp:biz-no:document:" + corpid + ":" + businessCode + ":" + date;
        Long serial = stringRedisTemplate.opsForValue().increment(key);
        if (serial == null) {
            throw new BizException("业务编号序号生成失败");
        }
        if (serial == 1) {
            stringRedisTemplate.expire(key, documentKeyTtl());
        }
        return serial;
    }

    private long nextMasterDataSerial(String corpid, String businessCode) {
        String key = "erp:biz-no:master-data:" + corpid + ":" + businessCode;
        while (true) {
            Long serial = stringRedisTemplate.execute(TAKE_SEGMENT_VALUE, java.util.List.of(key));
            if (serial != null) {
                return serial;
            }
            Long end = counterRepository.reserve(corpid, businessCode, "GLOBAL", MASTER_DATA_SEGMENT_SIZE);
            long start = end - MASTER_DATA_SEGMENT_SIZE + 1;
            stringRedisTemplate.execute(INSTALL_SEGMENT_IF_EMPTY, java.util.List.of(key), Long.toString(start), Long.toString(end));
        }
    }

    private Duration documentKeyTtl() {
        LocalDateTime tomorrow = LocalDate.now().plusDays(1).atStartOfDay();
        return Duration.between(LocalDateTime.now(), tomorrow).plusDays(1);
    }

    private String formatSerial(long serial) {
        if (serial > 99_999) {
            throw new BizException("业务编号序号超过五位上限");
        }
        return String.format("%05d", serial);
    }

    private void validate(String corpid, String businessCode) {
        if (corpid == null || corpid.isBlank()) {
            throw new BizException("公司标识不能为空");
        }
        if (businessCode == null || businessCode.isBlank()) {
            throw new BizException("业务编码不能为空");
        }
    }
}
