package xbb.ai.erp.module.purchase.infrastructure.persistence.repository;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PurchaseDraftRepositoryRedisStructureTest {

    @Test
    void should_keep_purchase_request_draft_repository_as_redis_based_implementation() throws Exception {
        assertRedisDraftRepository(
            PurchaseRequestDraftRepositoryImpl.class,
            "purchase:request:draft:",
            "purchase:request:draft:index:"
        );
    }

    @Test
    void should_keep_purchase_order_draft_repository_as_redis_based_implementation() throws Exception {
        assertRedisDraftRepository(
            PurchaseOrderDraftRepositoryImpl.class,
            "purchase:order:draft:",
            "purchase:order:draft:index:"
        );
    }

    private void assertRedisDraftRepository(
        Class<?> repositoryClass,
        String expectedDraftPrefix,
        String expectedIndexPrefix
    ) throws Exception {
        Field draftKeyPrefix = repositoryClass.getDeclaredField("DRAFT_KEY_PREFIX");
        Field indexKeyPrefix = repositoryClass.getDeclaredField("DRAFT_INDEX_KEY_PREFIX");
        Field draftTtl = repositoryClass.getDeclaredField("DRAFT_TTL");
        Field maxDraftCount = repositoryClass.getDeclaredField("MAX_DRAFT_COUNT");
        Field redisTemplate = repositoryClass.getDeclaredField("redisTemplate");

        assertTrue(Modifier.isStatic(draftKeyPrefix.getModifiers()));
        assertTrue(Modifier.isStatic(indexKeyPrefix.getModifiers()));
        assertTrue(Modifier.isStatic(draftTtl.getModifiers()));
        assertTrue(Modifier.isStatic(maxDraftCount.getModifiers()));

        draftKeyPrefix.setAccessible(true);
        indexKeyPrefix.setAccessible(true);
        draftTtl.setAccessible(true);
        maxDraftCount.setAccessible(true);

        assertEquals(expectedDraftPrefix, draftKeyPrefix.get(null));
        assertEquals(expectedIndexPrefix, indexKeyPrefix.get(null));
        assertEquals(Duration.ofDays(7), draftTtl.get(null));
        assertEquals(10, maxDraftCount.get(null));
        assertEquals("StringRedisTemplate", redisTemplate.getType().getSimpleName());
    }
}
