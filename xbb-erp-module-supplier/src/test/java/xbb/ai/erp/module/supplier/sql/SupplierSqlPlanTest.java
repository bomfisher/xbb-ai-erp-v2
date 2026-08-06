package xbb.ai.erp.module.supplier.sql;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SupplierSqlPlanTest {

    @Test
    void should_include_supplier_rename_sql() throws Exception {
        String sql = Files.readString(resolveRepoFile("docs/sql/2026-08-03-refactor-supplier-module.sql"));
        assertTrue(sql.contains("ALTER TABLE `supplier` CHANGE COLUMN `vendor" + "_code` `supplier_code`"));
        assertTrue(sql.contains("ALTER TABLE `supplier_contact` CHANGE COLUMN `vendor" + "_id` `supplier_id`"));
    }

    private Path resolveRepoFile(String relativePath) {
        Path current = Path.of("").toAbsolutePath();
        Path direct = current.resolve(relativePath).normalize();
        if (Files.exists(direct)) {
            return direct;
        }
        return current.resolve("..").resolve(relativePath).normalize();
    }
}
