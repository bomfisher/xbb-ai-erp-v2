package xbb.ai.erp.base.idgen;

import java.util.concurrent.atomic.AtomicLong;

public class DefaultIdGenerator implements IdGenerator {

    private static final AtomicLong COUNTER = new AtomicLong(System.currentTimeMillis());

    @Override
    public Long nextId() {
        return COUNTER.incrementAndGet();
    }
}
