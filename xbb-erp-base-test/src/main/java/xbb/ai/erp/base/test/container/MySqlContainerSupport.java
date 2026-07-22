package xbb.ai.erp.base.test.container;

import org.testcontainers.containers.MySQLContainer;

public abstract class MySqlContainerSupport {

    protected static MySQLContainer<?> mysqlContainer() {
        return new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("xbb_erp")
            .withUsername("test")
            .withPassword("test");
    }
}
