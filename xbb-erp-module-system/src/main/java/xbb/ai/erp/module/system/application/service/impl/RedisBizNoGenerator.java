package xbb.ai.erp.module.system.application.service.impl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.system.domain.model.BizNoRule;
import xbb.ai.erp.module.system.domain.model.BizNoSerialModeEnum;
import xbb.ai.erp.module.system.domain.repository.BizNoCounterRepository;
import xbb.ai.erp.module.system.domain.repository.BizNoRuleRepository;

@Service
@RequiredArgsConstructor
public class RedisBizNoGenerator implements BizNoGenerator {

    private static final int MASTER_DATA_SEGMENT_SIZE = 1_000;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.BASIC_ISO_DATE;
    private static final String NEXT_FIELD = "next";
    private static final String END_FIELD = "end";
    private static final String SEGMENT_LOCK_SUFFIX = ":segment-lock";
    private static final Duration SEGMENT_LOCK_TTL = Duration.ofSeconds(30);
    private static final long SEGMENT_LOCK_WAIT_MILLIS = 10L;

    private final StringRedisTemplate stringRedisTemplate;
    private final BizNoRuleRepository ruleRepository;
    private final BizNoCounterRepository counterRepository;

    @Override
    public String next(String corpid, String businessCode) {
        validate(corpid, businessCode);
        BizNoRule rule = ruleRepository.findRequired(corpid, businessCode);
        long serial = rule.serialMode() == BizNoSerialModeEnum.CONTINUOUS
            ? nextMasterDataSerial(corpid, businessCode)
            : nextDocumentSerial(corpid, businessCode);
        String datePart = rule.includeDate() == 1 ? "-" + LocalDate.now().format(DATE_FORMATTER) : "";
        return rule.prefix() + datePart + "-" + formatSerial(serial, rule.suffixLength());
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
        String lockKey = key + SEGMENT_LOCK_SUFFIX;
        long serial = requireSerial(stringRedisTemplate.opsForHash().increment(key, NEXT_FIELD, 1));
        while (true) {
            Long end = parseSerial(stringRedisTemplate.opsForHash().get(key, END_FIELD));
            if (end != null && serial <= end) {
                return serial;
            }
            String lockValue = UUID.randomUUID().toString();
            Boolean lockAcquired = stringRedisTemplate.opsForValue()
                .setIfAbsent(lockKey, lockValue, SEGMENT_LOCK_TTL);
            if (!Boolean.TRUE.equals(lockAcquired)) {
                waitForSegmentLock();
                continue;
            }
            try {
                end = parseSerial(stringRedisTemplate.opsForHash().get(key, END_FIELD));
                if (end == null || serial > end) {
                    end = counterRepository.reserve(corpid, businessCode, "GLOBAL", MASTER_DATA_SEGMENT_SIZE);
                    stringRedisTemplate.opsForHash().put(key, END_FIELD, Long.toString(end));
                }
                if (serial <= end) {
                    return serial;
                }
            } finally {
                releaseSegmentLock(lockKey, lockValue);
            }
        }
    }

    private void waitForSegmentLock() {
        try {
            Thread.sleep(SEGMENT_LOCK_WAIT_MILLIS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new BizException("业务编号号段锁等待被中断");
        }
    }

    private long requireSerial(Long serial) {
        if (serial == null) {
            throw new BizException("业务编号序号生成失败");
        }
        return serial;
    }

    private Long parseSerial(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException exception) {
            throw new BizException("业务编号缓存序号格式无效");
        }
    }

    private void releaseSegmentLock(String lockKey, String lockValue) {
        String currentLockValue = stringRedisTemplate.opsForValue().get(lockKey);
        if (lockValue.equals(currentLockValue)) {
            stringRedisTemplate.delete(lockKey);
        }
    }

    private Duration documentKeyTtl() {
        LocalDateTime tomorrow = LocalDate.now().plusDays(1).atStartOfDay();
        return Duration.between(LocalDateTime.now(), tomorrow).plusDays(1);
    }

    private String formatSerial(long serial, int suffixLength) {
        String serialText = Long.toString(serial);
        if (serialText.length() > suffixLength) {
            throw new BizException("业务编号序号超过后缀位数上限");
        }
        return "0".repeat(suffixLength - serialText.length()) + serialText;
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
