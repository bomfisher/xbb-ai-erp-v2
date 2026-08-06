package xbb.ai.erp.module.customer.infrastructure.persistence.integration;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import xbb.ai.erp.module.customer.infrastructure.persistence.mapper.CustomerMapper;
import xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerPO;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers(disabledWithoutDocker = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerMapperMySqlIntegrationTest {

    @Container
    private static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0.36")
        .withDatabaseName("xbb_erp_test")
        .withUsername("xbb")
        .withPassword("xbb");

    private SqlSessionFactory sqlSessionFactory;

    @org.junit.jupiter.api.BeforeAll
    void setUpDatabase() throws Exception {
        Flyway.configure()
            .dataSource(MYSQL.getJdbcUrl(), MYSQL.getUsername(), MYSQL.getPassword())
            .locations("filesystem:" + projectRoot().resolve("xbb-erp-app-admin/src/main/resources/db/migration"))
            .load()
            .migrate();
        sqlSessionFactory = buildSqlSessionFactory();
    }

    @AfterAll
    void closeContainer() {
        MYSQL.stop();
    }

    @BeforeEach
    void resetFixture() throws Exception {
        try (Connection connection = DriverManager.getConnection(MYSQL.getJdbcUrl(), MYSQL.getUsername(), MYSQL.getPassword());
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM customer");
            statement.executeUpdate(readFixture());
        }
    }

    @Test
    void should_apply_baseline_and_read_fixture_with_real_mapper() throws Exception {
        try (Connection connection = DriverManager.getConnection(MYSQL.getJdbcUrl(), MYSQL.getUsername(), MYSQL.getPassword());
             java.sql.ResultSet tables = connection.getMetaData().getTables(null, null, "customer", new String[]{"TABLE"})) {
            org.junit.jupiter.api.Assertions.assertTrue(tables.next());
        }

        try (SqlSession session = sqlSessionFactory.openSession()) {
            CustomerMapper mapper = session.getMapper(CustomerMapper.class);
            List<CustomerPO> customers = mapper.findByCondition(Map.of("corpid", "corp-fixture", "conditions", List.of()));

            assertEquals(1, customers.size());
            assertEquals("CUST-FIXTURE", customers.get(0).getCustomerCode());
            assertEquals("Fixture 客户", customers.get(0).getCustomerName());
        }
    }

    @Test
    void should_insert_and_load_customer_with_real_mapper_xml() {
        CustomerPO customer = new CustomerPO();
        customer.setCorpid("corp-integration");
        customer.setCustomerCode("CUST-INTEGRATION");
        customer.setCustomerName("集成测试客户");
        customer.setCustomerCategory("A");
        customer.setBizStatus("1");
        customer.setRefStatus("0");
        customer.setDel(0);
        customer.setAddTime(2L);
        customer.setUpdateTime(2L);
        customer.setCreatorId("integration-user");
        customer.setModifyId("integration-user");
        customer.setVersion(0);

        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            CustomerMapper mapper = session.getMapper(CustomerMapper.class);
            mapper.insert(customer);

            assertNotNull(customer.getId());
            CustomerPO loaded = mapper.findById("corp-integration", customer.getId());
            assertNotNull(loaded);
            assertEquals("CUST-INTEGRATION", loaded.getCustomerCode());
            assertEquals("集成测试客户", loaded.getCustomerName());
        }
    }

    private SqlSessionFactory buildSqlSessionFactory() throws Exception {
        PooledDataSource dataSource = new PooledDataSource(
            "com.mysql.cj.jdbc.Driver",
            MYSQL.getJdbcUrl(),
            MYSQL.getUsername(),
            MYSQL.getPassword()
        );
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setEnvironment(new Environment("test", new JdbcTransactionFactory(), dataSource));
        configuration.addMapper(CustomerMapper.class);
        parseMapper(configuration, projectRoot().resolve("xbb-erp-module-common/src/main/resources/mapper/common/CommonListFilterMapper.xml"));
        parseMapper(configuration, projectRoot().resolve("xbb-erp-module-customer/src/main/resources/mapper/customer/CustomerMapper.xml"));
        return new MybatisSqlSessionFactoryBuilder().build(configuration);
    }

    private void parseMapper(MybatisConfiguration configuration, Path mapperPath) throws Exception {
        try (InputStream inputStream = Files.newInputStream(mapperPath)) {
            new XMLMapperBuilder(inputStream, configuration, mapperPath.toString(), configuration.getSqlFragments()).parse();
        }
    }

    private String readFixture() throws Exception {
        try (InputStream inputStream = getClass().getResourceAsStream("/fixtures/customer-repository.sql")) {
            assertNotNull(inputStream);
            return new String(inputStream.readAllBytes());
        }
    }

    private Path projectRoot() {
        return Path.of(System.getProperty("maven.multiModuleProjectDirectory", ".")).toAbsolutePath();
    }
}
