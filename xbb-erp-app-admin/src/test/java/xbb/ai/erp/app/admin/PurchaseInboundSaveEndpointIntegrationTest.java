package xbb.ai.erp.app.admin;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Map;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers(disabledWithoutDocker = true)
@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
class PurchaseInboundSaveEndpointIntegrationTest {

    private static final String CORPID = "purchase-inbound-it";

    @Container
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.4")
        .withDatabaseName("erp_test")
        .withUsername("erp")
        .withPassword("erp");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
        registry.add("spring.datasource.driver-class-name", MYSQL::getDriverClassName);
    }

    @BeforeEach
    void setUpDatabase() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        new ResourceDatabasePopulator(new ClassPathResource("purchase-inbound-save-integration-schema.sql"))
            .execute(dataSource);
        jdbcTemplate.update("insert into purchase_order (corpid, order_no, supplier_id, supplier_name, total_amount, del, add_time, update_time, creator_id, modify_id) values (?, ?, ?, ?, ?, 0, 1, 1, ?, ?)",
            CORPID, "PO-IT-001", 101L, "集成测试供应商", new BigDecimal("50.00"), "it-user", "it-user");
        jdbcTemplate.update("insert into purchase_order_item (corpid, purchase_order_id, line_no, sku_id, sku_code, sku_name, unit_name, qty, inbound_qty, unit_price, amount, del, add_time, update_time, creator_id, modify_id) values (?, 1, 1, 1001, 'SKU-1001', '测试物料', '件', 5, 0, 10, 50, 0, 1, 1, ?, ?)",
            CORPID, "it-user", "it-user");
    }

    @Test
    void should_persist_inbound_and_item_from_save_endpoint() throws Exception {
        Map<String, Object> body = Map.of(
            "corpid", CORPID,
            "userId", "it-user",
            "idempotentNo", "purchase-inbound-it-001",
            "main", Map.of(
                "purchaseOrderId", 1,
                "supplierId", 101,
                "supplierName", "集成测试供应商",
                "warehouseId", 201,
                "inboundDate", 1735689600000L
            ),
            "items", java.util.List.of(Map.of(
                "purchaseOrderItemId", 1,
                "skuId", 1001,
                "skuName", "测试物料",
                "unitName", "件",
                "warehouseId", 201,
                "qty", 2,
                "unitPrice", 10
            ))
        );

        mockMvc.perform(post("/erp/v1/purchase/purchaseInbound/saveAndSubmit")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(0));

        org.junit.jupiter.api.Assertions.assertEquals(1,
            jdbcTemplate.queryForObject("select count(*) from purchase_inbound where corpid = ? and total_amount = 20.00 and status = 'SUBMITTED'", Integer.class, CORPID));
        org.junit.jupiter.api.Assertions.assertEquals(1,
            jdbcTemplate.queryForObject("select count(*) from purchase_inbound_item where corpid = ? and qty = 2.000000 and amount = 20.00 and cost_unit = 10.000000 and cost_amount = 20.00", Integer.class, CORPID));
    }

    @Test
    void should_reject_quantity_that_exceeds_pending_purchase_order_quantity() throws Exception {
        Map<String, Object> body = Map.of(
            "corpid", CORPID,
            "userId", "it-user",
            "idempotentNo", "purchase-inbound-it-overflow",
            "main", Map.of("purchaseOrderId", 1, "supplierId", 101),
            "items", java.util.List.of(Map.of(
                "purchaseOrderItemId", 1, "skuId", 1001, "skuName", "测试物料", "unitName", "件",
                "warehouseId", 201, "qty", 6, "unitPrice", 10
            ))
        );

        mockMvc.perform(post("/erp/v1/purchase/purchaseInbound/saveAndSubmit")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(false));

        org.junit.jupiter.api.Assertions.assertEquals(0,
            jdbcTemplate.queryForObject("select count(*) from purchase_inbound where corpid = ?", Integer.class, CORPID));
    }
}
