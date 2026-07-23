package xbb.ai.erp.module.customer.sql;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerSqlPlanTest {

    @Test
    void should_generate_customer_sql_script() throws Exception {
        Path path = Path.of("../docs/sql/2026-07-22-init-customer-module.sql");
        assertTrue(Files.exists(path));
        String sql = Files.readString(path);
        assertTrue(sql.contains("CREATE TABLE `customer_bank_account`"));
        assertTrue(sql.contains("CREATE TABLE `customer_invoice_profile`"));
        assertTrue(sql.contains("idx_corpid_customer_default_flag"));
    }
}
