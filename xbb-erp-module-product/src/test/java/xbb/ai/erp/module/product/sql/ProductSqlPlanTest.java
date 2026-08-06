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

    @Test
    void should_include_product_spec_columns_in_init_sql() throws Exception {
        Path path = Path.of("../docs/sql/2026-07-22-init-product-module.sql");
        assertTrue(Files.exists(path));
        String sql = Files.readString(path);
        assertTrue(sql.contains("`spec_signature`"));
        assertTrue(sql.contains("`spec_snapshot`"));
        assertTrue(sql.contains("UNIQUE KEY `uk_spu_spec_signature`"));
    }

    @Test
    void should_include_sku_supplier_relation_tables_in_init_sql() throws Exception {
        Path path = Path.of("../docs/sql/2026-07-22-init-product-module.sql");
        assertTrue(Files.exists(path));
        String sql = Files.readString(path);
        assertTrue(sql.contains("CREATE TABLE `product_sku_supplier_rel`"));
        assertTrue(sql.contains("CREATE TABLE `product_sku_supplier_rel_history`"));
        assertTrue(sql.contains("UNIQUE KEY `uk_sku_supplier_rel_unique`"));
        assertTrue(sql.contains("KEY `idx_sku_supplier_rel_supplier`"));
        assertTrue(sql.contains("KEY `idx_sku_supplier_rel_history_relation`"));
    }
}
