package xbb.ai.erp.module.supplier.infrastructure.persistence;

import com.baomidou.mybatisplus.annotation.TableName;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.SupplierPO;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SupplierPersistenceStructureTest {

    @Test
    void should_use_supplier_table_and_mapper_file() throws Exception {
        TableName tableName = SupplierPO.class.getAnnotation(TableName.class);
        assertEquals("supplier", tableName.value());

        String xml = Files.readString(Path.of("src/main/resources/mapper/supplier/SupplierMapper.xml"));
        assertTrue(xml.contains("from supplier"));
        assertTrue(xml.contains("supplier_code"));
    }
}
