package xbb.ai.erp.base.test;

import org.junit.jupiter.api.Test;
import org.testcontainers.DockerClientFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ContainerRuntimeAvailabilityTest {

    @Test
    void should_provide_docker_runtime_in_ci() {
        if (!Boolean.parseBoolean(System.getenv("CI"))) {
            return;
        }
        assertTrue(DockerClientFactory.instance().isDockerAvailable(), "CI 必须提供 Docker 以运行容器集成测试");
    }
}
