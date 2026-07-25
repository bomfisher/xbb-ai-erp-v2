package xbb.ai.erp.module.product.sql;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductSqlPlanTest {

    @Test
    void should_include_warehouse_sql_script() throws Exception {
        Path path = Path.of("../docs/sql/2026-07-22-init-product-module.sql");
        assertTrue(Files.exists(path));
        String sql = Files.readString(path);
        assertTrue(sql.contains("CREATE TABLE `warehouse`"));
        assertTrue(sql.contains("UNIQUE KEY `uk_warehouse_code`"));
        assertTrue(sql.contains("KEY `idx_warehouse_list`"));
    }
}
