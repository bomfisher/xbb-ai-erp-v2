package xbb.ai.erp.base.test.container;

import com.redis.testcontainers.RedisContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class RedisContainerSupport {

    protected static RedisContainer redisContainer() {
        return new RedisContainer(DockerImageName.parse("redis:7"));
    }
}
