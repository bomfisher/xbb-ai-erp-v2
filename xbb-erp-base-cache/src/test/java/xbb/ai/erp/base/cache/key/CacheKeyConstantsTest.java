package xbb.ai.erp.base.cache.key;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CacheKeyConstantsTest {

    @Test
    void should_expose_user_info_prefix() {
        assertEquals("erp:user:info:", CacheKeyConstants.USER_INFO);
    }
}
