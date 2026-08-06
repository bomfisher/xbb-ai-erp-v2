package xbb.ai.erp.module.customer.infrastructure.persistence.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import xbb.ai.erp.module.customer.admin.dto.CustomerMainDTO;
import xbb.ai.erp.module.customer.application.pojo.CustomerSaveDraftPojo;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers(disabledWithoutDocker = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerDraftRepositoryRedisIntegrationTest {

    @Container
    private static final GenericContainer<?> REDIS = new GenericContainer<>(DockerImageName.parse("redis:7.2.5-alpine"))
        .withExposedPorts(6379);

    private LettuceConnectionFactory connectionFactory;
    private StringRedisTemplate redisTemplate;
    private CustomerDraftRepositoryImpl repository;

    @BeforeAll
    void setUpRedis() {
        connectionFactory = new LettuceConnectionFactory(REDIS.getHost(), REDIS.getMappedPort(6379));
        connectionFactory.afterPropertiesSet();
        redisTemplate = new StringRedisTemplate(connectionFactory);
        redisTemplate.afterPropertiesSet();
        repository = new CustomerDraftRepositoryImpl(redisTemplate, new ObjectMapper());
    }

    @AfterAll
    void closeRedis() {
        connectionFactory.destroy();
        REDIS.stop();
    }

    @BeforeEach
    void clearRedis() {
        connectionFactory.getConnection().serverCommands().flushAll();
    }

    @Test
    void should_save_load_and_index_draft_in_real_redis() {
        CustomerSaveDraftPojo draft = draft("corp-redis", "draft-redis", "Redis 草稿", 100L);

        String draftCode = repository.saveDraft(draft);
        CustomerSaveDraftPojo loaded = repository.loadDraft("corp-redis", draftCode);
        List<CustomerSaveDraftPojo> drafts = repository.listDrafts("corp-redis", 10);

        assertEquals("draft-redis", draftCode);
        assertNotNull(loaded);
        assertEquals("Redis 草稿", loaded.getDraftTitle());
        assertEquals(1, drafts.size());
        assertEquals("draft-redis", drafts.get(0).getDraftCode());
        Long ttlSeconds = redisTemplate.getExpire(
            CustomerDraftRepositoryImpl.DRAFT_KEY_PREFIX + "corp-redis:draft-redis",
            java.util.concurrent.TimeUnit.SECONDS
        );
        assertNotNull(ttlSeconds);
        assertTrue(ttlSeconds > Duration.ofDays(6).toSeconds());
        assertTrue(ttlSeconds <= CustomerDraftRepositoryImpl.DRAFT_TTL.toSeconds());
    }

    @Test
    void should_keep_only_latest_ten_drafts_in_real_redis() {
        for (int index = 1; index <= 11; index++) {
            repository.saveDraft(draft("corp-overflow", "draft-" + index, "草稿" + index, (long) index));
        }

        List<CustomerSaveDraftPojo> drafts = repository.listDrafts("corp-overflow", 20);

        assertEquals(10, drafts.size());
        assertEquals("draft-11", drafts.get(0).getDraftCode());
        assertNull(repository.loadDraft("corp-overflow", "draft-1"));
    }

    private CustomerSaveDraftPojo draft(String corpid, String draftCode, String draftTitle, Long updatedTime) {
        CustomerMainDTO main = new CustomerMainDTO();
        main.setCustomerCode("CUST-REDIS");
        main.setCustomerName("Redis 客户");

        CustomerSaveDraftPojo draft = new CustomerSaveDraftPojo();
        draft.setCorpid(corpid);
        draft.setDraftCode(draftCode);
        draft.setDraftTitle(draftTitle);
        draft.setMain(main);
        draft.setUpdatedTime(updatedTime);
        return draft;
    }
}
